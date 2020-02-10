package org.sourcepond.challenge.api;

/**
 * Responsible for building {@link Classifier} instances. Instances of this class can be reused.
 */
public interface ClassifierBuilder {

    /**
     * Adds the range specified to this builder. Implementations must be
     * thread-safe.
     *
     * @param min The lower bound of the range. Must not be 0 or negative.
     * @param max The upper bound of the range. Must be equal or greater than {@code min}
     * @throws InterruptedException Thrown, if the current thread has been interrupted.
     */
    void addRange(int min, int max) throws InterruptedException;

    /**
     * Builds a new {@link Classifier} instance.
     *
     * @return New instance, never {@code null}
     * @throws InterruptedException Thrown, if the current thread has been interrupted.
     */
    Classifier build() throws InterruptedException;
}
