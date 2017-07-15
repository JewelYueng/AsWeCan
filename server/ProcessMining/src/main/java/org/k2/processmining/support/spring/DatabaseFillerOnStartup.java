package org.k2.processmining.support.spring;

import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.support.algorithm.*;
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
import java.net.URL;
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

    @Autowired
    private MethodManage methodManage;

    @Autowired
    private MiningMethodService miningMethodService;

    @Autowired
    private MergeMethodService mergeMethodService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        methodManage.init();
        LOGGER.debug("start to load algorithm...");
        List<MiningMethod> miningMethods = miningMethodService.getAllMethods();
        List<MergeMethod> mergeMethods = mergeMethodService.getAllMethods();
        loadMiner(miningMethods);
        loadMerger(mergeMethods);
        LOGGER.debug("finish to load algorithm...");
    }


    private void loadMerger(List<MergeMethod> mergeMethods) {
        Algorithm<Merger> algorithm;
        for (MergeMethod mergeMethod : mergeMethods) {
            try {
                algorithm = methodManage.loadMergerById(mergeMethod.getId());
                MergerFactory.getInstance().put(mergeMethod.getId(), algorithm);
            }
            catch (LoadMethodException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void loadMiner(List<MiningMethod> miningMethods) {
        for (MiningMethod miningMethod : miningMethods) {
            try {
                MinerFactory.getInstance().put(miningMethod.getId(), methodManage.loadMinerById(miningMethod.getId()));
            }
            catch (LoadMethodException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
