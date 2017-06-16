package org.k2.processmining.support.algorithm;

import org.k2.processmining.support.mining.Miner;

/**
 * Created by nyq on 2017/6/15.
 */
public class MinerFactory extends AlgorithmFactory<Miner> {
    private MinerFactory() {}

    public static MinerFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final MinerFactory INSTANCE = new MinerFactory();
    }
}
