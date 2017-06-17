package org.k2.processmining.support.spring;

import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.merge.Merger;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.reflect.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by nyq on 2017/6/16.
 */
@Component
public class DatabaseFillerOnStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFillerOnStartup.class);

    @Value("${miner.jar.path}")
    private String minerJarDir;

    @Value("${merger.jar.path}")
    private String mergerJarPath;

    @Value("${algorithm.config.path}")
    private String algorithmConfigPath;

    @Value("${impl.path.key}")
    private String implPathKey;

    @Value("${algorithm.adapter.jar.suffix}")
    private String algorithmAdapterJarSuffix;

    @Autowired
    private MiningMethodService miningMethodService;

    private static final String WEB_INF_PATH = DatabaseFillerOnStartup.class.getClassLoader().getResource("/").getPath().replace("classes", "");

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.debug("start to load algorithm...");
        List<MiningMethod> miningMethods = miningMethodService.getAllMethods();
        List<MergeMethod> mergeMethods = new LinkedList<>();
        loadMiner(miningMethods);
        loadMerger(mergeMethods);
        LOGGER.debug("finish to load algorithm...");
    }

    @SuppressWarnings("unchecked")
    private void loadMerger(List<MergeMethod> mergeMethods) {
        Map<String, Object> configs;
        Merger merger = null;
        for (MergeMethod mergeMethod : mergeMethods) {
            String dirPath = WEB_INF_PATH + minerJarDir + File.separatorChar + mergeMethod.getId();
            String[] jarPaths = new File(dirPath).list();
            if (jarPaths == null || jarPaths.length == 0) {
                LOGGER.error("merger jar dir does not exist or is empty: {}", dirPath);
                continue;
            }
            configs = (Map<String, Object>) loadAlgorithmConfigMapFromJar(jarPaths);
            if (configs == null) {
                LOGGER.error("fail to load config: {}", dirPath);
                continue;
            }
            try {
                merger = ReflectUtil.getInstanceFromJar(jarPaths,configs.get(implPathKey).toString());
            }
            catch (Exception e) {
                LOGGER.error("fail to reflect for MergerImpl: {}", dirPath, e);
                continue;
            }
            MergerFactory.getInstance().put(mergeMethod.getId(), new Algorithm(merger, configs));
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMiner(List<MiningMethod> miningMethods) {
        Map<String, Object> configs;
        Miner miner = null;
        for (MiningMethod miningMethod : miningMethods) {
            String dirPath = WEB_INF_PATH + minerJarDir + File.separatorChar + miningMethod.getId();
            File dir = new File(dirPath);
            String[] jarPaths = dir.list();
            if (jarPaths == null || jarPaths.length == 0) {
                System.out.println(new File(".").getAbsolutePath());
                LOGGER.error("miner jar dir does not exist or is empty: {}", dirPath);
                continue;
            }
            for (int i = 0; i < jarPaths.length; i++) {
                jarPaths[i] = dir.getAbsolutePath() + File.separatorChar + jarPaths[i];
            }
            configs = (Map<String, Object>) loadAlgorithmConfigMapFromJar(jarPaths);
            if (configs == null) {
                LOGGER.error("fail to load config: {}", dirPath);
                continue;
            }
            try {
                miner = ReflectUtil.getInstanceFromJar(jarPaths,configs.get(implPathKey).toString());
            }
            catch (Exception e) {
                LOGGER.error("fail to reflect for MinerImpl: {}", dirPath, e);
                continue;
            }
            MinerFactory.getInstance().put(miningMethod.getId(), new Algorithm(miner, configs));
        }
    }

    private Object loadAlgorithmConfigMapFromJar(String[] jarPaths)  {
        for (String jarPath : jarPaths) {
            File f = new File(jarPath);
            if (f.getName().endsWith(algorithmAdapterJarSuffix)) {
                try {
                    ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(f));
                    ZipEntry zipEntry;
                    while ((zipEntry=zipInputStream.getNextEntry()) != null) {
                        if (zipEntry.getName().equals(algorithmConfigPath)) {
                            ZipFile zipFile = new ZipFile(f);
                            Yaml yaml = new Yaml();
                            try (InputStream inputStream = zipFile.getInputStream(zipEntry)){
                                return yaml.load(inputStream);
                            }
                        }
                    }
                }
                catch (IOException e) {
                    LOGGER.error("fail to load algorithm-config.yml from: {}",jarPath, e);
                    return null;
                }
            }
        }
        LOGGER.error("*-k2.jar does not exist");
        return null;
    }
}
