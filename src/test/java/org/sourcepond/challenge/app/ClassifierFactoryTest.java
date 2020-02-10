package org.sourcepond.challenge.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.sourcepond.challenge.api.Classifier;
import org.sourcepond.challenge.api.ClassifierBuilder;
import org.sourcepond.challenge.api.ClassifierBuilderFactory;
import org.sourcepond.challenge.testing.TestResourceBundle;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.sourcepond.challenge.app.CLI.MAX_VALUE;
import static org.sourcepond.challenge.app.ClassifierFactory.CREATE_RANGES_MESSAGE;

public class ClassifierFactoryTest {
    private static final String EXPECTED_CREATE_MESSAGE_FORMAT = "aaaa";
    private static final int EXPECTED_NUM_OF_RANGES = 1;
    private static final int EXPECTED_LOW = 10;
    private static final int EXPECTED_HIGH = 20;
    private static final int EXPECTED_COUNT = 5;
    private final TestResourceBundle bundle = mock(TestResourceBundle.class);
    private final ClassifierBuilderFactory builderFactory = mock(ClassifierBuilderFactory.class);
    private final ClassifierBuilder builder = mock(ClassifierBuilder.class);
    private final Classifier classifier = mock(Classifier.class);
    private final Random random = mock(Random.class);
    private final PrintWriter console = mock(PrintWriter.class);
    private final ExecutorService executor = newSingleThreadExecutor();
    private final ClassifierFactory factory = new ClassifierFactory(builderFactory, executor, random, bundle, console);

    @BeforeEach
    public void setup() throws InterruptedException {
        when(builderFactory.create()).thenReturn(builder);
        when(builder.build()).thenReturn(classifier);
        when(bundle.handleGetObject(CREATE_RANGES_MESSAGE)).thenReturn(EXPECTED_CREATE_MESSAGE_FORMAT);
        when(console.printf(EXPECTED_CREATE_MESSAGE_FORMAT, EXPECTED_NUM_OF_RANGES)).thenReturn(console);
    }

    @AfterEach
    public void tearDown() {
        executor.shutdown();
    }

    private void verifyMinMax(int min, int max) throws Exception {
        assertSame(classifier, factory.create(EXPECTED_NUM_OF_RANGES).get());
        InOrder order = inOrder(builder);
        order.verify(builder).addRange(min, max);
        order.verify(builder).build();
        order.verifyNoMoreInteractions();
    }

    private void verifyMinMax() throws Exception {
        verifyMinMax(EXPECTED_LOW, EXPECTED_HIGH);
    }

    @Test
    public void createNextIntLeftIsHigher() throws Exception {
        when(random.nextInt(MAX_VALUE))
                .thenReturn(EXPECTED_HIGH)
                .thenReturn(EXPECTED_LOW);
        verifyMinMax();
    }

    @Test
    public void createNextIntRightIsHigher() throws Exception {
        when(random.nextInt(MAX_VALUE))
                .thenReturn(EXPECTED_LOW)
                .thenReturn(EXPECTED_HIGH);
        verifyMinMax();
    }

    @Test
    public void createNextIntIsEqual() throws Exception {
        when(random.nextInt(MAX_VALUE))
                .thenReturn(EXPECTED_LOW)
                .thenReturn(EXPECTED_LOW);
        verifyMinMax(EXPECTED_LOW, EXPECTED_LOW);
    }

    @Test
    public void verifyAddRangeCallingCount() throws Exception {
        when(random.nextInt(MAX_VALUE)).thenReturn(EXPECTED_LOW);
        when(console.printf(EXPECTED_CREATE_MESSAGE_FORMAT, EXPECTED_COUNT)).thenReturn(console);
        factory.create(EXPECTED_COUNT).get();
        verify(builder, times(EXPECTED_COUNT)).addRange(EXPECTED_LOW, EXPECTED_LOW);
    }
}
