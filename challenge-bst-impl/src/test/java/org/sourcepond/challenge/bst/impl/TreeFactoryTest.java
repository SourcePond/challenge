package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.lang.System.arraycopy;
import static java.util.Arrays.sort;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sourcepond.challenge.bst.impl.BstClassifierBuilder.MAX_COMPARATOR;
import static org.sourcepond.challenge.bst.impl.BstClassifierBuilder.MIN_COMPARATOR;

public class TreeFactoryTest {
    private Range[] minSortedRanges = new Range[]{
            new Range(10, 20),
            new Range(10, 25),
            new Range(30, 35),
            new Range(40, 50),
            new Range(20, 100),
            new Range(5, 60)
    };
    private Range[] maxSortedRanges;
    private final ExecutorService executor = newSingleThreadExecutor();
    private final TreeFactory factory = new TreeFactory(executor);

    @BeforeEach
    public void setup() {
        maxSortedRanges = new Range[minSortedRanges.length];
        arraycopy(minSortedRanges, 0, maxSortedRanges, 0, minSortedRanges.length);
        sort(minSortedRanges, MIN_COMPARATOR);
        sort(maxSortedRanges, MAX_COMPARATOR);
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }

    @Test
    public void buildMinEvenLengthArray() throws ExecutionException, InterruptedException {
        var tree = factory.buildMin(minSortedRanges).get();
        assertEquals(20, tree.getValue());
        assertEquals(5, tree.getLeft().getValue());
        assertNull(tree.getLeft().getLeft());
        assertEquals(10, tree.getLeft().getRight().getValue());
        assertEquals(30, tree.getRight().getValue());
        assertEquals(40, tree.getRight().getRight().getValue());
    }

    @Test
    public void buildMaxEvenLengthArray() throws ExecutionException, InterruptedException {
        var tree = factory.buildMax(maxSortedRanges).get();
        assertEquals(50, tree.getValue());
        assertEquals(20, tree.getLeft().getValue());
        assertNull(tree.getLeft().getLeft());
        assertEquals(25, tree.getLeft().getRight().getValue());
        assertEquals(60, tree.getRight().getValue());
        assertEquals(100, tree.getRight().getRight().getValue());
    }

    @Test
    public void close() {
        factory.close();
        assertTrue(executor.isShutdown());
    }
}
