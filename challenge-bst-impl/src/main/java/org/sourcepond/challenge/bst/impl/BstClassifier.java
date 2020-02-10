package org.sourcepond.challenge.bst.impl;

import org.sourcepond.challenge.api.Classifier;
import org.sourcepond.challenge.api.ResultSink;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class BstClassifier implements Classifier {
    public static final String EXECUTION_FAILED_MESSAGE_KEY = "classification.execution.failed";
    public static final String INTERRUPTED_MESSAGE_KEY = "classification.interrupted";
    private final ForkJoinPool pool;
    private final ResourceBundle bundle;
    private final CollectorFactory collectorFactory;
    private final Future<Node> minRoot;
    private final Future<Node> maxRoot;

    BstClassifier(ForkJoinPool pool, ResourceBundle bundle, CollectorFactory collectorFactory, Future<Node> minRoot, Future<Node> maxRoot) {
        this.pool = pool;
        this.bundle = bundle;
        this.collectorFactory = collectorFactory;
        this.minRoot = minRoot;
        this.maxRoot = maxRoot;
    }

    @Override
    public void classify(int num, ResultSink sink) throws InterruptedException {
        var minCollector = collectorFactory.createMinCollector(num);
        var maxCollector = collectorFactory.createMaxCollector(num);

        try {
            var minTask = pool.submit(collectorFactory.createAction(minRoot.get(), minCollector));
            var maxTask = pool.submit(collectorFactory.createAction(maxRoot.get(), maxCollector));
            minTask.get();
            maxTask.get();
        } catch (ExecutionException e) {
            // Handle unexpected exception as runtime exception
            throw new IllegalStateException(bundle.getString(EXECUTION_FAILED_MESSAGE_KEY), e);
        }

        // Pass results to the client
        sink.found(num, minCollector.merge(maxCollector).size());
    }

    @Override
    public void close() {
        pool.shutdown();
    }
}
