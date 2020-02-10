package org.sourcepond.challenge.bst.impl;

class RangeFactory {

    public Range create(Integer min, Integer max) {
        return new Range(min, max);
    }
}
