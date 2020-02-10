package org.sourcepond.challenge.bst.impl;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import static java.util.Arrays.spliterator;
import static org.sourcepond.challenge.utils.InterruptedCheck.checkInterrupted;

class TreeFactory implements Closeable {
    private final ExecutorService executor;

    public TreeFactory(ExecutorService executor) {
        this.executor = executor;
    }

    private Future<Node> buildTree(Range[] ranges, Function<Range, Integer> rangeValueGetter) {
        return executor.submit(() -> {
            // It's validated the range array has at least 1 entry
            int rootIndex = ranges.length / 2;

            // We dispense of implementing a complicated balancing algorithm now.
            // For the moment, stick with a naive initial balance.
            var root = ranges[rootIndex];
            var tree = new Node(rangeValueGetter, rangeValueGetter.apply(root));
            tree.add(root);
            checkInterrupted();

            var left = spliterator(ranges, 0, rootIndex);
            var right = spliterator(ranges, rootIndex + 1, ranges.length);

            while (checkInterrupted() && left.tryAdvance(range -> tree.add(range))) ;
            while (checkInterrupted() && right.tryAdvance(range -> tree.add(range))) ;


            return tree;
        });
    }

    public Future<Node> buildMin(Range[] ranges) {
        return buildTree(ranges, r -> r.getMin());
    }

    public Future<Node> buildMax(Range[] ranges) {
        return buildTree(ranges, r -> r.getMax());
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
