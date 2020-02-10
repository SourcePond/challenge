package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Function;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectorTest {
    private final Range r1 = mock(Range.class);
    private final Range r2 = mock(Range.class);
    private final Range r3 = mock(Range.class);
    private final Range r4 = mock(Range.class);
    private final Range r5 = mock(Range.class);
    private final Set<Range> ranges1 = of(r1, r3, r5);
    private final Set<Range> ranges2 = of(r2, r3, r4, r5);
    private final Node node1 = mock(Node.class);
    private final Node node2 = mock(Node.class);
    private final Function<Node, Boolean> check = mock(Function.class);
    private Collector c1 = new Collector(check);
    private Collector c2 = new Collector(check);

    @BeforeEach
    public void setup() {
        when(node1.getRanges()).thenReturn(ranges1);
        when(node2.getRanges()).thenReturn(ranges2);
    }

    @Test
    public void accepted() {
        when(check.apply(node1)).thenReturn(true);
        when(check.apply(node2)).thenReturn(true);
        assertTrue(c1.accept(node1));
        assertTrue(c2.accept(node2));
        var merged = c1.merge(c2);
        assertEquals(2, merged.size());
        assertTrue(merged.contains(r5));
        assertTrue(merged.contains(r3));
    }

    @Test
    public void refused() {
        when(check.apply(node1)).thenReturn(false);
        when(check.apply(node2)).thenReturn(true);
        assertFalse(c1.accept(node1));
        assertTrue(c2.accept(node2));
        assertTrue(c1.merge(c2).isEmpty());
    }
}
