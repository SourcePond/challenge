package org.sourcepond.challenge.api;

/**
 * Implementing instance are responsible to match a number to a (possibly big) set
 * of ranges.
 */
public interface Classifier extends AutoCloseable {

    /**
     *
     * @param num Number to classify, must not be negative.
     * @param sink Where to report results, must not be {@code null}
     * @throws InterruptedException Thrown, if classification has been interrupted
     */
    void classify(int num, ResultSink sink) throws InterruptedException;

    /**
     * Closes this classifier, see {@link AutoCloseable}
     */
    @Override
    default void close() { }
}
