package org.k2.processmining.support.algorithm;

import org.k2.processmining.support.merge.Merger;

/**
 * Created by nyq on 2017/6/15.
 */
public class MergerFactory extends AlgorithmFactory<Merger> {
    private MergerFactory() {}

    public static MergerFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final MergerFactory INSTANCE = new MergerFactory();
    }
}
