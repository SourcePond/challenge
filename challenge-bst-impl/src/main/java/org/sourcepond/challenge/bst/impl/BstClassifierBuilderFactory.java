package org.sourcepond.challenge.bst.impl;

import org.sourcepond.challenge.api.ClassifierBuilder;
import org.sourcepond.challenge.api.ClassifierBuilderFactory;

import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;

import static java.util.ResourceBundle.getBundle;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.ForkJoinPool.commonPool;

public class BstClassifierBuilderFactory implements ClassifierBuilderFactory {
    private final ResourceBundle bundle;
    private final ForkJoinPool pool;
    private final CollectorFactory collectorFactory;
    private final RangeFactory rangeFactory;
    private final TreeFactory treeFactory;

    public BstClassifierBuilderFactory() {
        this(getBundle("bst"),
                commonPool(),
                new CollectorFactory(),
                new TreeFactory(newFixedThreadPool(2)), new RangeFactory()
        );
    }

    BstClassifierBuilderFactory(ResourceBundle bundle,
                                ForkJoinPool pool,
                                CollectorFactory collectorFactory,
                                TreeFactory treeFactory, RangeFactory rangeFactory) {
        this.bundle = bundle;
        this.pool = pool;
        this.collectorFactory = collectorFactory;
        this.rangeFactory = rangeFactory;
        this.treeFactory = treeFactory;
    }

    @Override
    public ClassifierBuilder create() {
        return new BstClassifierBuilder(bundle,
                pool,
                collectorFactory,
                treeFactory,
                rangeFactory);
    }
}
