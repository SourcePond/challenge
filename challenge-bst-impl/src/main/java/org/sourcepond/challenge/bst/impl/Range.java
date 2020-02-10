package org.sourcepond.challenge.bst.impl;

class Range {
    private final Integer min;
    private final Integer max;

    public Range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public int compareMin(Range other) {
        return min.compareTo(other.min);
    }

    public int compareMax(Range other) {
        return max.compareTo(other.max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        if (min != range.min) return false;
        return max == range.max;
    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + max;
        return result;
    }
}
