package org.sourcepond.challenge.bst.impl;

import org.sourcepond.challenge.api.Classifier;
import org.sourcepond.challenge.api.ClassifierBuilder;

import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static org.sourcepond.challenge.utils.InterruptedCheck.checkInterrupted;

class BstClassifierBuilder implements ClassifierBuilder {
    static final Comparator<Range> MIN_COMPARATOR = (r1, r2) -> r1.compareMin(r2);
    static final Comparator<Range> MAX_COMPARATOR = (r1, r2) -> r1.compareMax(r2);
    private final ResourceBundle bundle;
    private final ForkJoinPool pool;
    private final TreeFactory treeFactory;
    private final RangeFactory rangeFactory;
    private final CollectorFactory collectorFactory;
    private final Set<Range> ranges = newKeySet();

    public BstClassifierBuilder(ResourceBundle bundle,
                                ForkJoinPool pool,
                                CollectorFactory collectorFactory,
                                TreeFactory treeFactory,
                                RangeFactory rangeFactory) {
        this.bundle = bundle;
        this.pool = pool;
        this.treeFactory = treeFactory;
        this.rangeFactory = rangeFactory;
        this.collectorFactory = collectorFactory;
    }

    @Override
    public void addRange(int min, int max) throws InterruptedException {
        checkInterrupted();
        ranges.add(rangeFactory.create(min, max));
    }

    @Override
    public Classifier build() throws InterruptedException {
        checkInterrupted();
        var minSortedArr = ranges.toArray(new Range[ranges.size()]);
        var maxSortedArr = ranges.toArray(new Range[ranges.size()]);
        try (var t = treeFactory) {
            var minTree = treeFactory.buildMin(minSortedArr);
            var maxTree = treeFactory.buildMax(maxSortedArr);
            return new BstClassifier(pool,
                    bundle,
                    collectorFactory,
                    minTree,
                    maxTree);
        }
    }
}
