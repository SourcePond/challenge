package org.sourcepond.challenge.api;

/**
 * Instances will be informed about the result of a classification (see {@link Classifier#classify(int, ResultSink)}).
 * Implementations of this interface must be provided by the client.
 */
public interface ResultSink {

    /**
     * Called when a classification produced a result.
     *
     * @param num Number which has been classified (validated)
     * @param matchingRanges Number of matching ranges.
     */
    void found(int num, int matchingRanges);
}
