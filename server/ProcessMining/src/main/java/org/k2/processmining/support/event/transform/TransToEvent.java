package org.k2.processmining.support.event.transform;

import org.k2.processmining.support.event.entity.AtomicCorrelation;
import org.k2.processmining.support.event.entity.ConjunctiveCorrelation;
import org.k2.processmining.support.event.entity.DisjunctiveCorrelation;
import org.k2.processmining.support.event.entity.Log4Normal;
import org.k2.processmining.support.event.util.CorRatio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by nyq on 2017/6/13.
 */
public class TransToEvent {
    private static Logger LOGGER = LoggerFactory.getLogger(TransToEvent.class);

    public static File transToEvent(InputStream normalLogInputStream, String path, String name) throws TransToEventException, IOException {
        try {
            if (!path.endsWith("/") && !path.endsWith("\\")) path += File.separatorChar;
            Log4Normal log4Normal = getLog(normalLogInputStream, name);
            return runEC(log4Normal, path);
        }
        catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new TransToEventException(e);
        }
    }

    private static Log4Normal getLog(InputStream normalLogInputStream, String outputFileName) throws IOException, TransToEventException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(normalLogInputStream));

        String line;
        String nulVal = Log4Normal.nulVal;
        String separator = Log4Normal.seperator;
        if ((line=reader.readLine()) == null) {
            throw new TransToEventException("The normalLog is illegal!");
        }
        String[] indexs = line.split(separator);
        ArrayList<String> events = new ArrayList<String>();
        while ((line=reader.readLine()) != null && !line.equals(nulVal)) {
            events.add(line);
        }
        // 将每一列数据放入内存
        Map<Integer, ArrayList<String>> attValsMap = new HashMap<Integer, ArrayList<String>>();
        for (int q = 0; q < indexs.length; q++) {
            ArrayList<String> attVals = new ArrayList<String>();
            for (String e : events)
                attVals.add(e.split(separator)[q]);
            attValsMap.put(q, attVals);
        }
        return new Log4Normal(outputFileName, indexs, events, attValsMap);
    }

    private static File runEC(Log4Normal log, String path) throws IOException, ParseException {
        Long tStart = System.currentTimeMillis();
        System.out.println("Mission Start!");
        String nulVal = Log4Normal.nulVal;
        String seperator = Log4Normal.seperator;
        int timeIndex = Log4Normal.timeIndex;
        String sdfStr = Log4Normal.sdfStr;
        String indexs[] = log.getIndexs();
        ArrayList<String> events = log.getEvents();
        Map<Integer, ArrayList<String>> attValsMap = log.getAttValsMap();

        File outputFile = new File(path + log.getOutputFile());
        // 计算最大 alpha
        System.out.println("计算alpha");
        double max_alpha = 0;
        for (int q = 0; q < indexs.length; q++) {
            // 如果时间戳这一列存在相同的值，会生成一个很大的alpha(接近1)，
            // 会对结果照成影响，所以不比较时间戳这一列的alpha值
            // 但是计算关联时仍然可以将时间戳这一列包含进来
            if (q == timeIndex)
                continue;
            ArrayList<String> attVals = attValsMap.get(q);
            double tmp_alpha = CorRatio.alpha_Att(nulVal, attVals,
                    events.size());
            System.out.println(indexs[q] + " " + tmp_alpha);
            if (tmp_alpha != 1 && tmp_alpha > max_alpha)
                max_alpha = tmp_alpha;
        }
        System.out.println("max_alpha : " + max_alpha);

        System.out.println();
        System.out.println("计算原子关联(AC)");

        // 遍历所有属性组合，计算真原子关联
        List<AtomicCorrelation> AC = new ArrayList<AtomicCorrelation>();
        for (int attIndex1 = 0; attIndex1 < indexs.length; attIndex1++)
            for (int attIndex2 = attIndex1; attIndex2 < indexs.length; attIndex2++) {
                // 通过distinctRatio和sharedRatio过滤
                if (attIndex1 == attIndex2) {// 同属性原子关联
                    ArrayList<String> attVals = attValsMap.get(attIndex1);
                    if (CorRatio.distinctRatio(nulVal, attVals, null) == 1)
                        continue;
                } else {// 异属性原子关联
                    ArrayList<String> attVals1 = attValsMap.get(attIndex1);
                    ArrayList<String> attVals2 = attValsMap.get(attIndex2);
                    if (CorRatio.sharedRatio(nulVal, attVals1, attVals2) < max_alpha)
                        continue;
                }
                // 通过piRatio验证真伪
                AtomicCorrelation r = new AtomicCorrelation(attIndex1,
                        attIndex2);
                double piRatio = CorRatio.piRatio_ac(seperator, nulVal, r,
                        events);
                if (piRatio < 0.5 && piRatio >= max_alpha)
                    AC.add(r);
            }

        // 遍历AC（原子关联集合）,计算真连接关联CC
        int level = 2;
        List<ConjunctiveCorrelation> CC = new ArrayList<ConjunctiveCorrelation>();
        List<AtomicCorrelation> CAND = AC;
        if (CAND.size() == 0) {// 没有任何关联
            System.out.println("没有找到关联！");
            return null;
        }
        if (CAND.size() == 1) {// 只有一个真原子关联
            System.out.println("结果:");
            System.out.println("[" + CAND.get(0).attIndex1 + ","
                    + CAND.get(0).attIndex2 + "]");
            System.out.println("生成事件日志...");
            CorRatio.generate_EventLog(seperator, nulVal, events,
                    new DisjunctiveCorrelation(CAND.get(0)), outputFile,
                    timeIndex, sdfStr, indexs);
            System.out.println("Mission Accomplished!");
            Long tEnd = System.currentTimeMillis();
            Long dur = tEnd - tStart;
            System.out.println("Spare Time: " + dur + " ms");
            return outputFile;
        }

        System.out.println();
        System.out.println("计算连接关联(CC)");

        // 初始化CC 第一级的CC为AC 第二级的CC为两个原子关联组成的连接关联 初始化后的CC为第二级CC
        for (int i = 0; i < CAND.size() - 1; ) {
            ConjunctiveCorrelation cr = new ConjunctiveCorrelation(AC.get(i));
            boolean addflag = true;
            for (int j = i + 1; j < CAND.size(); j++) {
                // 用piRatio验证真伪
                // 如果连接关联中的某个属性与新加入的原子关联中的某个属性相同 该连接关联无意义 舍弃该连接关联
                boolean equalflag = false;
                for (AtomicCorrelation aor : cr.AC) {
                    if (aor.attIndex1 == CAND.get(j).attIndex1
                            || aor.attIndex1 == CAND.get(j).attIndex2
                            || aor.attIndex2 == CAND.get(j).attIndex1
                            || aor.attIndex2 == CAND.get(j).attIndex2) {
                        equalflag = true;
                        break;
                    }
                }
                if (equalflag)
                    continue;
                double piRatio = CorRatio.piRatio_cc(seperator, nulVal, cr,
                        CAND.get(j), events);
                /**
                 * 使用连接关联对日志划分 是将已分组事件再按照新的属性进行分组的过程
                 * 所以新的连接关联生成后应该比之前的关联有更多的分组,即caseSize增大
                 */
                if (piRatio < 0.5 && piRatio >= max_alpha
                        && cr.getCaseSize() > CAND.get(i).getCaseSize()
                        && cr.getCaseSize() > CAND.get(j).getCaseSize()) {
                    cr.addRelation(CAND.get(j));
                    CC.add(cr);
                    CAND.remove(j);
                    CAND.remove(i);
                    addflag = false;
                    break;
                }
            }
            if (addflag)
                i++;
        }

        if (CAND.size() == 0) {// 只有一个真连接关联,无剩余的候选关联
            System.out.println("结果:");
            System.out.println("[" + CC.get(0).AC.get(0).attIndex1 + ","
                    + CC.get(0).AC.get(0).attIndex2 + "] ∧ ["
                    + CC.get(0).AC.get(1).attIndex1 + ","
                    + CC.get(0).AC.get(1).attIndex2 + "]");
            System.out.println("生成事件日志...");
            CorRatio.generate_EventLog(seperator, nulVal, events,
                    new DisjunctiveCorrelation(CC.get(0)), outputFile,
                    timeIndex, sdfStr, indexs);
            System.out.println("Mission Accomplished!");
            Long tEnd = System.currentTimeMillis();
            Long dur = tEnd - tStart;
            System.out.println("Spare Time: " + dur + " ms");
            return outputFile;
        }
        // 迭代计算每一层CC
        System.out.println("迭代CC");
        while (!CAND.isEmpty() && level <= AC.size()) {
            for (int i = 0; i < CC.size(); i++) {
                ConjunctiveCorrelation cr = CC.get(i);
                for (int j = 0; j < CAND.size(); j++) {
                    // 用piRatio验证真伪
                    // 如果连接关联中的某个属性与新加入的原子关联中的某个属性相同 该连接关联无意义 舍弃该连接关联
                    boolean equalflag = false;
                    for (AtomicCorrelation aor : cr.AC) {
                        if (aor.attIndex1 == CAND.get(j).attIndex1
                                || aor.attIndex1 == CAND.get(j).attIndex2
                                || aor.attIndex2 == CAND.get(j).attIndex1
                                || aor.attIndex2 == CAND.get(j).attIndex2) {
                            equalflag = true;
                            break;
                        }
                    }
                    if (equalflag)
                        continue;

                    double piRatio = CorRatio.piRatio_cc(seperator, nulVal, cr,
                            CAND.get(j), events);
                    /**
                     * 使用连接关联对日志划分 是将已分组事件再按照新的属性进行分组的过程
                     * 所以新的连接关联生成后应该比之前的关联有更多的分组,即caseSize增大
                     */
                    if (piRatio < 0.5 && piRatio >= max_alpha
                            && cr.getTmpCszie() > cr.getCaseSize()
                            && cr.getTmpCszie() > CAND.get(j).getCaseSize()) {
                        cr.addRelation(CAND.get(j));
                        cr.setCaseSize(cr.getTmpCszie());
                        cr.setNonNullnum(cr.getTmpNNnum());
                        CAND.remove(j);
                        break;
                    }
                }
            }

            level++;
        }
        System.out.println("CC计算完毕");
        // System.out.println("CC");
        // for (ConjunctiveCorrelation cor : CC) {
        // for (AtomicCorrelation aor : cor.AC) {
        // System.out.print("[" + indexs[aor.attIndex1] + ","
        // + indexs[aor.attIndex2] + "]∧");
        // }
        // System.out.println("∧∧∧∧∧∧∧∧∧∧∧∧");
        // }
        // System.out.println("CAND");
        // for (AtomicCorrelation aor : CAND) {
        // System.out.print("[" + indexs[aor.attIndex1] + ","
        // + indexs[aor.attIndex2] + "]");
        // }
        // System.out.println();

        System.out.println();
        System.out.println("计算分离关联(DC)");

        // 遍历CAND(原子关联集合)与CC(连接关联集合),计算真分离关联DC
        level = 2;
        int levelsize = CC.size() + CAND.size();
        List<DisjunctiveCorrelation> DC = new ArrayList<DisjunctiveCorrelation>();

        // 初始化DC 第一级的DC为CC 第二级的DC为CAND与DC组成的分离关联 初始化后的DC为第二级DC
        // 初始化分三个步骤
        // step1:CC之中的元素合并
        System.out.println("初始化DC:CC之中的元素合并");
        if (CC.size() > 1) {
            for (int i = 0; i < CC.size() - 1; ) {
                boolean addflag = true;
                ConjunctiveCorrelation cr1 = CC.get(i);
                for (int j = i + 1; j < CC.size(); j++) {
                    ConjunctiveCorrelation cr2 = CC.get(j);
                    // 用piRatio验证真伪
                    DisjunctiveCorrelation dr = new DisjunctiveCorrelation();
                    double piRatio = CorRatio.piRatio_dc(seperator, nulVal,
                            cr1, cr2, events, dr);

                    /**
                     * 使用分离关联对日志划分 是将未分组事件加入已分组的事件中的过程
                     * 所以新的分离关联生成后应该比之前的关联有更大的nonNullnum(已分组事件更多了)
                     */
                    if (piRatio < 0.5 && piRatio >= max_alpha
                            && dr.getTmpNNnum() > cr1.getNonNullnum()
                            && dr.getTmpNNnum() > cr2.getNonNullnum()) {
                        dr.addConjunctiveRelation(cr1);
                        dr.addConjunctiveRelation(cr2);
                        dr.setNonNullnum(dr.getTmpNNnum());
                        dr.setCaseSize(dr.getTmpCszie());
                        DC.add(dr);
                        CC.remove(j);
                        CC.remove(i);
                        addflag = false;
                        break;
                    }
                }
                if (addflag)
                    i++;
            }
        }
        // step2:CAND中元素合并
        System.out.println("初始化DC:CAND之中的元素合并");
        if (CAND.size() > 1) {
            for (int i = 0; i < CAND.size() - 1; ) {
                boolean addflag = true;
                AtomicCorrelation ar1 = CAND.get(i);
                for (int j = i + 1; j < CAND.size(); j++) {
                    AtomicCorrelation ar2 = CAND.get(j);
                    // 用piRatio验证真伪
                    DisjunctiveCorrelation dr = new DisjunctiveCorrelation();
                    double piRatio = CorRatio.piRatio_dc(seperator, nulVal,
                            ar1, ar2, events, dr);

                    /**
                     * 使用分离关联对日志划分 是将未分组事件加入已分组的事件中的过程
                     * 所以新的分离关联生成后应该比之前的关联有更大的nonNullnum(已分组事件更多了)
                     */
                    if (piRatio < 0.5 && piRatio >= max_alpha
                            && dr.getTmpNNnum() > ar1.getNonNullnum()
                            && dr.getTmpNNnum() > ar2.getNonNullnum()) {
                        dr.addAtomicRelation(ar1);
                        dr.addAtomicRelation(ar2);
                        dr.setNonNullnum(dr.getTmpNNnum());
                        dr.setCaseSize(dr.getTmpCszie());
                        DC.add(dr);
                        CAND.remove(j);
                        CAND.remove(i);
                        addflag = false;
                        break;
                    }
                }
                if (addflag)
                    i++;
            }
        }
        // step3:CC与CAND中两者剩余元素合并
        System.out.println("初始化DC:CC与CAND的元素合并");
        if (CAND.size() > 0 && CC.size() > 0) {
            for (int i = 0; i < CC.size(); ) {
                boolean addflag = true;
                ConjunctiveCorrelation cr1 = CC.get(i);
                for (int j = 0; j < CAND.size(); j++) {
                    AtomicCorrelation ar2 = CAND.get(j);
                    // 用piRatio验证真伪
                    DisjunctiveCorrelation dr = new DisjunctiveCorrelation();
                    double piRatio = CorRatio.piRatio_dc(seperator, nulVal,
                            cr1, ar2, events, dr);

                    /**
                     * 使用分离关联对日志划分 是将未分组事件加入已分组的事件中的过程
                     * 所以新的分离关联生成后应该比之前的关联有更大的nonNullnum(已分组事件更多了)
                     */
                    if (piRatio < 0.5 && piRatio >= max_alpha
                            && dr.getTmpNNnum() > cr1.getNonNullnum()
                            && dr.getTmpNNnum() > ar2.getNonNullnum()) {
                        dr.addConjunctiveRelation(cr1);
                        dr.addAtomicRelation(ar2);
                        dr.setNonNullnum(dr.getTmpNNnum());
                        dr.setCaseSize(dr.getTmpCszie());
                        DC.add(dr);
                        CAND.remove(j);
                        CC.remove(i);
                        addflag = false;
                        break;
                    }
                }
                if (addflag)
                    i++;
            }
        }

        // 迭代计算每一层DC
        System.out.println("迭代DC");

        while ((!CAND.isEmpty() || !CC.isEmpty()) && level <= levelsize) {
            for (int i = 0; i < DC.size(); i++) {
                DisjunctiveCorrelation dr = DC.get(i);
                boolean setflag = false;
                for (int j = 0; j < CAND.size(); j++) {
                    AtomicCorrelation ar = CAND.get(j);
                    // 用piRatio验证真伪
                    double piRatio = CorRatio.piRatio_ddc(seperator, nulVal,
                            dr, ar, events);
                    /**
                     * 使用分离关联对日志划分 是将未分组事件加入已分组的事件中的过程
                     * 所以新的分离关联生成后应该比之前的关联有更大的nonNullnum(已分组事件更多了)
                     */
                    if (piRatio < 0.5 && piRatio >= max_alpha
                            && dr.getTmpNNnum() > dr.getNonNullnum()
                            && dr.getTmpNNnum() > ar.getNonNullnum()) {
                        dr.addAtomicRelation(CAND.get(j));
                        dr.setCaseSize(dr.getTmpCszie());
                        dr.setNonNullnum(dr.getTmpNNnum());
                        CAND.remove(j);
                        setflag = true;
                        break;
                    }
                }
                if (setflag)
                    continue;
                for (int j = 0; j < CC.size(); j++) {
                    ConjunctiveCorrelation cr = CC.get(j);
                    // 用piRatio验证真伪
                    double piRatio = CorRatio.piRatio_ddc(seperator, nulVal,
                            dr, cr, events);
                    /**
                     * 使用分离关联对日志划分 是将未分组事件加入已分组的事件中的过程
                     * 所以新的分离关联生成后应该比之前的关联有更大的nonNullnum(已分组事件更多了)
                     */
                    if (piRatio < 0.5 && piRatio >= max_alpha
                            && dr.getTmpNNnum() > dr.getNonNullnum()
                            && dr.getTmpNNnum() > cr.getNonNullnum()) {
                        dr.addConjunctiveRelation(CC.get(j));
                        dr.setCaseSize(dr.getTmpCszie());
                        dr.setNonNullnum(dr.getTmpNNnum());
                        CC.remove(j);
                        break;
                    }
                }
            }
            level++;
        }

        // 打印最后的DC、CC、CAND
        System.out.println("DC计算完毕");
        System.out.println("结果:");
        for (DisjunctiveCorrelation dor : DC) {
            for (AtomicCorrelation aor : dor.AC) {
                System.out.print("[" + indexs[aor.attIndex1] + ","
                        + indexs[aor.attIndex2] + "]∨");
            }
            for (ConjunctiveCorrelation cor : CC) {
                System.out.print("(");
                for (AtomicCorrelation aor : cor.AC) {
                    System.out.print("[" + indexs[aor.attIndex1] + ","
                            + indexs[aor.attIndex2] + "]∧");
                }
                System.out.print(")∨");
            }
        }
        System.out.println();
        // System.out.println("剩余的CC");
        // for (ConjunctiveCorrelation cor : CC) {
        // System.out.print("cor: ");
        // for (AtomicCorrelation aor : cor.AC) {
        // System.out.print("[" + indexs[aor.attIndex1] + ","
        // + indexs[aor.attIndex2] + "]");
        // }
        // System.out.println();
        // }
        // System.out.println("剩余的CAND");
        // for (AtomicCorrelation aor : CAND) {
        // System.out.print("[" + indexs[aor.attIndex1] + ","
        // + indexs[aor.attIndex2] + "]");
        // }
        System.out.println();
        System.out.println("生成事件日志...");
        if (DC.size() > 0)
            CorRatio.generate_EventLog(seperator, nulVal, events, DC.get(0),
                    outputFile, timeIndex, sdfStr, indexs);
        else if (CC.size() > 0)
            CorRatio.generate_EventLog(seperator, nulVal, events,
                    new DisjunctiveCorrelation(CC.get(0)), outputFile,
                    timeIndex, sdfStr, indexs);
        else if (CAND.size() > 0)
            CorRatio.generate_EventLog(seperator, nulVal, events,
                    new DisjunctiveCorrelation(CAND.get(0)), outputFile,
                    timeIndex, sdfStr, indexs);
        System.out.println("Mission Accomplished!");
        Long tEnd = System.currentTimeMillis();
        Long dur = tEnd - tStart;
        System.out.println("Spare Time: " + dur + " ms");
        return outputFile;
    }
}
