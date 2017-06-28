package org.k2.processmining.support.algorithm;

import org.apache.commons.io.FileUtils;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.support.merge.Merger;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.reflect.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by nyq on 2017/6/21.
 */
@Component
public class MethodManage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodManage.class);

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

    private String rootPath;

    public MethodManage() {}

    public void init() {
        rootPath = this.getClass().getResource("/").getPath().replaceAll("/classes", "");
//        rootPath = "E:/IdeaProjects/AsWeCan/server/ProcessMining/src/main/webapp/WEB-INF";
    }

    public String getMinerDir(String miningMethodId) {
        return rootPath + File.separatorChar + minerJarDir + File.separatorChar + miningMethodId;
    }

    public String getMergerDir(String mergeMethodId) {
        return rootPath + File.separatorChar + mergerJarPath + File.separatorChar + mergeMethodId;
    }

    public void saveMinerJar(String methodId, String jarName, InputStream inputStream) throws IOException {
        String path = getMinerDir(methodId) + File.separatorChar + jarName;
        saveJar(path, inputStream);
    }

    public void saveMergerJar(String methodId, String jarName, InputStream inputStream) throws IOException {
        String path = getMergerDir(methodId) + File.separatorChar + jarName;
        saveJar(path, inputStream);
    }

    private void saveJar(String outputPath, InputStream inputStream) throws IOException {
        File file = new File(outputPath);
        if ((!file.getParentFile().isDirectory() && !file.getParentFile().mkdir())) {
            throw new IOException("fail to make dirs to save jars");
        }
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath))){
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int byteRead = 0;
            byte[] bytes = new byte[1024];
            while ((byteRead=inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0 ,byteRead);
            }
            outputStream.flush();
        }
    }

    public Algorithm<Miner> loadMinerById(String id) throws LoadMethodException {
        return loadMethod(id, getMinerDir(id), Miner.class);
    }

    public Algorithm<Merger> loadMergerById(String id) throws LoadMethodException {
        return loadMethod(id, getMergerDir(id), Merger.class);
    }

    @SuppressWarnings("unchecked")
    private  <T> Algorithm<T> loadMethod(String id, String methodDir, Class clazz) throws LoadMethodException {
        Map<String, Object> configs;
        T method;
        File dir = new File(methodDir);
        String[] jarPaths = dir.list();
        if (jarPaths == null || jarPaths.length == 0) {
            throw new LoadMethodException("method jar dir does not exist or is empty: " + methodDir);
        }
        for (int i = 0; i < jarPaths.length; i++) {
            jarPaths[i] = dir.getAbsolutePath() + File.separatorChar + jarPaths[i];
        }
        configs = (Map<String, Object>) loadAlgorithmConfigMapFromJar(jarPaths);
        if (configs == null) {
            throw new LoadMethodException("fail to load config: " + methodDir);
        }
        try {
            method = ReflectUtil.getInstance().getInstanceFromJar(id, jarPaths,configs.get(implPathKey).toString());
        }
        catch (Exception e) {
            throw new LoadMethodException("fail to reflect for interface implement: " + methodDir);
        }
        if (! clazz.isAssignableFrom(method.getClass())) {
            throw new LoadMethodException("the "+ method.getClass() + " is not the subclass of "+clazz);
        }
        return new Algorithm<>(id, method, configs);
    }


    public void deleteMerger(String methodId) {
        try {
            FileUtils.deleteDirectory(new File(getMergerDir(methodId)));
        }
        catch (IOException e) {
            LOGGER.error("fail to delete merger: {}", getMergerDir(methodId), e);
        }
    }

    public void deleteMiner(String methodId) {
        try {
            FileUtils.deleteDirectory(new File(getMinerDir(methodId)));
        }
        catch (IOException e) {
            LOGGER.error("fail to delete miner: {}", getMinerDir(methodId), e);
        }
    }

    private Object loadAlgorithmConfigMapFromJar(String[] jarPaths) throws LoadMethodException {
        for (String jarPath : jarPaths) {
            File f = new File(jarPath);
            if (f.getName().endsWith(algorithmAdapterJarSuffix)) {
                try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(f));){
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
                    throw new LoadMethodException("fail to load algorithm-config.yml from: " + jarPath);
                }
            }
        }
        LOGGER.error("*-k2.jar does not exist");
        throw new LoadMethodException("*-k2.jar does not exist");
    }
}
