package org.sourcepond.challenge.bst.impl;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

/**
 * Represents a node in a binary search tree.
 */
class Node {
    private final Function<Range, Integer> rangeValueGetter;
    private final Set<Range> ranges = newKeySet();
    private final Integer value;
    private Node left;
    private Node right;

    /**
     * Creates a new instance of this class.
     *
     * @param rangeValueGetter Delegator to the getter method to be called on the range object when passed to
     *                         the {@link #add(Range)} method on this node, see {@link Range#getMin()}
     *                         and {@link Range#getMax()}. Must not be {@code null}.
     * @param value            The value of this node.
     */
    public Node(Function<Range, Integer> rangeValueGetter, Integer value) {
        this.rangeValueGetter = rangeValueGetter;
        this.value = value;
    }

    public Set<Range> getRanges() {
        return ranges;
    }

    public Integer getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    private void setLeft(Node left) {
        this.left = left;
    }

    private void setRight(Node right) {
        this.right = right;
    }

    private Node node(Supplier<Node> getter, Consumer<Node> setter, Integer otherValue) {
        var node = getter.get();
        if (node == null) {
            node = new Node(rangeValueGetter, otherValue);
            setter.accept(node);
        }
        return node;
    }

    public void add(Range range) {
        var otherValue = rangeValueGetter.apply(range);

        if (value > otherValue) {
            node(this::getLeft, this::setLeft, otherValue).add(range);
        } else if (otherValue > value) {
            node(this::getRight, this::setRight, otherValue).add(range);
        } else {
            ranges.add(range);
        }
    }
}
