package org.sourcepond.challenge.bst.impl;

import java.util.Set;
import java.util.function.Function;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

class Collector {
    private final Set<Range> ranges = newKeySet();
    private final Function<Node, Boolean> check;

    public Collector(Function<Node, Boolean> check) {
        this.check = check;
    }

    public boolean accept(Node node) {
        boolean accept = check.apply(node);
        if (accept) {
            ranges.addAll(node.getRanges());
        }
        return accept;
    }

    public Set<Range> merge(Collector other) {
        ranges.retainAll(other.ranges);
        return ranges;
    }
}
