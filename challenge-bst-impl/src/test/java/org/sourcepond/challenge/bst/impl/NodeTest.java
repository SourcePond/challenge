package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeTest {
    private static final Integer EXPECTED_VALUE = 100;
    private static final Integer EXPECTED_LOWER_VALUE = 50;
    private static final Integer EXPECTED_HIGHER_VALUE = 150;
    private final Function<Range, Integer> rangeValueGetter = mock(Function.class);
    private final Range range = mock(Range.class);
    private final Node node = new Node(rangeValueGetter, EXPECTED_VALUE);

    @Test
    public void addLower() {
        when(rangeValueGetter.apply(range)).thenReturn(EXPECTED_LOWER_VALUE);
        node.add(range);
        assertNotNull(node.getLeft());
        assertNull(node.getRight());
        assertEquals(EXPECTED_LOWER_VALUE, node.getLeft().getValue());
        assertFalse(node.getRanges().contains(range));
        assertTrue(node.getLeft().getRanges().contains(range));
    }

    @Test
    public void addHigher() {
        when(rangeValueGetter.apply(range)).thenReturn(EXPECTED_HIGHER_VALUE);
        node.add(range);
        assertNotNull(node.getRight());
        assertNull(node.getLeft());
        assertEquals(EXPECTED_HIGHER_VALUE, node.getRight().getValue());
        assertFalse(node.getRanges().contains(range));
        assertTrue(node.getRight().getRanges().contains(range));
    }

    @Test
    public void addEqual() {
        when(rangeValueGetter.apply(range)).thenReturn(EXPECTED_VALUE);
        node.add(range);
        assertNull(node.getLeft());
        assertNull(node.getRight());
        assertTrue(node.getRanges().contains(range));
    }
}
