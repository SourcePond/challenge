package org.sourcepond.challenge.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.sourcepond.challenge.api.Classifier;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.sourcepond.challenge.app.CLI.MAX_VALUE;
import static org.sourcepond.challenge.app.ResultSinkImpl.PRINT_FORMAT;

public class ResultSinkImplTest {
    private static final int EXPECTED_NEXT_INT = 1234;
    private static final int EXPECTED_NUM = 4560;
    private static final int EXPECTED_MATCHING_RANGES = 5678;
    private final Future<Classifier> classifierFuture = mock(Future.class);
    private final Classifier classifier = mock(Classifier.class);
    private final Random random = mock(Random.class);
    private final PrintWriter console = mock(PrintWriter.class);
    private final ResultSinkImpl sink = new ResultSinkFactory().createResultSink(classifierFuture, random, console);

    @BeforeEach
    public void setup() throws Exception {
        when(classifierFuture.get()).thenReturn(classifier);
        when(random.nextInt(MAX_VALUE)).thenReturn(EXPECTED_NEXT_INT);
        when(console.printf(PRINT_FORMAT, EXPECTED_NUM, EXPECTED_MATCHING_RANGES)).thenReturn(console);
    }

    @Test
    @Timeout(3)
    public void verifyRun() throws Exception {
        var latch = new CountDownLatch(1);
        var th = new Thread(sink);
        doAnswer(a -> {
            th.interrupt();
            latch.countDown();
            return null;
        }).when(classifier).classify(EXPECTED_NEXT_INT, sink);
        th.start();
        latch.await();
    }

    @Test
    public void found() {
        sink.found(EXPECTED_NUM, EXPECTED_MATCHING_RANGES);
        verify(console).printf(PRINT_FORMAT, EXPECTED_NUM, EXPECTED_MATCHING_RANGES);
    }
}
