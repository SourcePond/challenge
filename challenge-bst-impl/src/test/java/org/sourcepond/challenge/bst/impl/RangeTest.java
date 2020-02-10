package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangeTest {
    private static final int EXPECTED_MIN = 10;
    private static final int EXPECTED_MAX = 20;
    private final Range range = new RangeFactory().create(EXPECTED_MIN, EXPECTED_MAX);

    @Test
    public void equal() {
        var r1 = new Range(EXPECTED_MIN, EXPECTED_MAX);
        assertTrue(r1.equals(r1));
        assertTrue(r1.equals(new Range(EXPECTED_MIN, EXPECTED_MAX)));
    }

    @Test
    public void notEqual() {
        var r1 = new Range(EXPECTED_MIN, EXPECTED_MAX);
        assertFalse(r1.equals(null));
        assertFalse(r1.equals(new Object()));
        assertFalse(r1.equals(new Range(EXPECTED_MIN, 30)));
        assertFalse(r1.equals(new Range(0, EXPECTED_MAX)));
    }

    @Test
    public void getMin() {
        assertEquals(EXPECTED_MIN, range.getMin());
    }

    @Test
    public void getMax() {
        assertEquals(EXPECTED_MAX, range.getMax());
    }

    @Test
    public void compareMin() {
        var r1 = new Range(EXPECTED_MIN - 1, EXPECTED_MAX);
        assertEquals(1, range.compareMin(r1));
        r1 = new Range(EXPECTED_MIN, EXPECTED_MAX);
        assertEquals(0, range.compareMin(r1));
        r1 = new Range(EXPECTED_MIN + 1, EXPECTED_MAX);
        assertEquals(-1, range.compareMin(r1));
    }

    @Test
    public void compareMax() {
        var r1 = new Range(EXPECTED_MIN, EXPECTED_MAX - 1);
        assertEquals(1, range.compareMax(r1));
        r1 = new Range(EXPECTED_MIN, EXPECTED_MAX);
        assertEquals(0, range.compareMax(r1));
        r1 = new Range(EXPECTED_MIN + 1, EXPECTED_MAX + 1);
        assertEquals(-1, range.compareMax(r1));
    }

    @Test
    public void verifyHashCode() {
        var r1 = new Range(EXPECTED_MIN, EXPECTED_MAX);
        assertEquals(330, r1.hashCode());
    }
}
