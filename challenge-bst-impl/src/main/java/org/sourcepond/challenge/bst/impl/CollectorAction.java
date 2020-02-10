package org.sourcepond.challenge.bst.impl;

import java.util.concurrent.RecursiveAction;

/**
 * Recursive action (fork/join) to find matching ranges within a {@link Node}.
 */
class CollectorAction extends RecursiveAction {
    private Node node;
    private Collector collector;

    public CollectorAction(Node node, Collector collector) {
        this.node = node;
        this.collector = collector;
    }

    private CollectorAction createTask(Node node) {
        return new CollectorAction(node, collector);
    }

    @Override
    protected void compute() {
        var accepted = collector.accept(node);
        if (accepted) {
            var left = node.getLeft();
            var right = node.getRight();

            if (left != null && right != null) {
                var leftTask = createTask(left);
                var rightTask = createTask(right);
                rightTask.fork();
                leftTask.compute();
                rightTask.join();
            } else if (left != null) {
                createTask(left).compute();
            } else if (right != null) {
                createTask(right).compute();
            }
        }
    }
}
