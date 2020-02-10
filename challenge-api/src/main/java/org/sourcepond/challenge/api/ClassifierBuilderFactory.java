package org.sourcepond.challenge.api;

/**
 * Service interface to be implemented by classifier implementors, see {@link java.util.ServiceLoader}
 * for more details. This is the SPI entry point.
 */
public interface ClassifierBuilderFactory {

    /**
     * Creates a new {@link ClassifierBuilder} instance.
     *
     * @return New instance, never {@code null}
     */
    ClassifierBuilder create();
}
