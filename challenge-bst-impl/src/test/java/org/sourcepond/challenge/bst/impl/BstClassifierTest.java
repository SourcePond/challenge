package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.sourcepond.challenge.api.ResultSink;
import org.sourcepond.challenge.testing.TestResourceBundle;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import static java.lang.Thread.currentThread;
import static java.util.Set.of;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.sourcepond.challenge.bst.impl.BstClassifier.EXECUTION_FAILED_MESSAGE_KEY;
import static org.sourcepond.challenge.bst.impl.BstClassifier.INTERRUPTED_MESSAGE_KEY;

public class BstClassifierTest {
    private static final String EXPECTED_INTERRUPTED_MESSAGE = "aaa";
    private static final String EXPECTED_FAILED_MESSAGE = "bbb";
    private static final int EXPECTED_NUM = 100;
    private final TestResourceBundle bundle = mock(TestResourceBundle.class);
    private final CollectorFactory collectorFactory = new CollectorFactory();
    private final Range range = mock(Range.class);
    private final Set<Range> ranges = of(range);
    private final Future<Node> minRootFuture = mock(Future.class);
    private final Future<Node> maxRootFuture = mock(Future.class);
    private final Node minRoot = mock(Node.class);
    private final Node maxRoot = mock(Node.class);
    private final ResultSink sink = mock(ResultSink.class);
    private BstClassifier classifier =
            new BstClassifier(commonPool(), bundle, collectorFactory, minRootFuture, maxRootFuture);

    @BeforeEach
    public void setup() throws Exception {
        when(minRootFuture.get()).thenReturn(minRoot);
        when(maxRootFuture.get()).thenReturn(maxRoot);
        when(bundle.handleGetObject(INTERRUPTED_MESSAGE_KEY)).thenReturn(EXPECTED_INTERRUPTED_MESSAGE);
        when(bundle.handleGetObject(EXECUTION_FAILED_MESSAGE_KEY)).thenReturn(EXPECTED_FAILED_MESSAGE);
    }

    @Test
    public void classify() throws Exception {
        when(minRoot.getValue()).thenReturn(50);
        when(maxRoot.getValue()).thenReturn(150);
        when(minRoot.getRanges()).thenReturn(ranges);
        when(maxRoot.getRanges()).thenReturn(ranges);

        classifier.classify(EXPECTED_NUM, sink);
        verify(sink).found(EXPECTED_NUM, 1);
    }

    @Test
    @Timeout(3)
    public void interrupted() {
        currentThread().interrupt();
        assertThrows(InterruptedException.class, () -> classifier.classify(EXPECTED_NUM, sink), EXPECTED_INTERRUPTED_MESSAGE);
        verifyNoInteractions(sink);
    }

    @Test
    public void failed() {
        doThrow(RuntimeException.class).when(minRoot).getValue();
        assertThrows(IllegalStateException.class, () -> classifier.classify(EXPECTED_NUM, sink), EXPECTED_FAILED_MESSAGE);
        verifyNoInteractions(sink);
    }

    @Test
    public void close() {
        var pool = mock(ForkJoinPool.class);
        classifier =
                new BstClassifier(pool, bundle, collectorFactory, minRootFuture, maxRootFuture);
        classifier.close();
        verify(pool).shutdown();
    }
}
