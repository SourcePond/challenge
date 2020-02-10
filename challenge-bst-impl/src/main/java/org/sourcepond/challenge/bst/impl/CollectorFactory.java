package org.sourcepond.challenge.bst.impl;

class CollectorFactory {

    public Collector createMinCollector(int num) {
        return new Collector(node -> num >= node.getValue());
    }

    public Collector createMaxCollector(int num) {
        return new Collector(node -> node.getValue() > num);
    }

    public CollectorAction createAction(Node tree, Collector collector) {
        return new CollectorAction(tree, collector);
    }
}
