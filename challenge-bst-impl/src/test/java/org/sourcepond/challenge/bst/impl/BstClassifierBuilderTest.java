package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcepond.challenge.api.Classifier;
import org.sourcepond.challenge.api.ClassifierBuilder;
import org.sourcepond.challenge.api.ResultSink;
import org.sourcepond.challenge.testing.TestResourceBundle;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BstClassifierBuilderTest {
    private static final int EXPECTED_NUM = 1;
    private static final int EXPECTED_MIN = 0;
    private static final int EXPECTED_MAX = 2;
    private static final int EXPECTED_RESULT = 10;
    private final TestResourceBundle bundle = mock(TestResourceBundle.class);
    private final RangeFactory rangeFactory = mock(RangeFactory.class);
    private final Range range = mock(Range.class);
    private final Future<Node> minTreeFuture = mock(Future.class);
    private final Future<Node> maxTreeFuture = mock(Future.class);
    private final Node minTree = mock(Node.class);
    private final Node maxTree = mock(Node.class);
    private final TreeFactory treeFactory = mock(TreeFactory.class);
    private final ForkJoinPool pool = mock(ForkJoinPool.class);
    private final CollectorFactory collectorFactory = mock(CollectorFactory.class);
    private final Collector minCollector = mock(Collector.class);
    private final Collector maxCollector = mock(Collector.class);
    private final CollectorAction minTask = mock(CollectorAction.class);
    private final CollectorAction maxTask = mock(CollectorAction.class);
    private final ResultSink sink = mock(ResultSink.class);
    private final Set<Range> result = mock(Set.class);
    private final BstClassifierBuilderFactory classifierBuilderFactory =
            new BstClassifierBuilderFactory(bundle, pool, collectorFactory, treeFactory, rangeFactory);
    private ClassifierBuilder builder;

    private void setDone(ForkJoinTask task) throws Exception {
        var setDone = ForkJoinTask.class.getDeclaredMethod("setDone");
        setDone.setAccessible(true);
        setDone.invoke(task);
    }

    @BeforeEach
    public void setup() throws Exception {
        when(rangeFactory.create(EXPECTED_MIN, EXPECTED_MAX)).thenReturn(range);
        when(minTreeFuture.get()).thenReturn(minTree);
        when(maxTreeFuture.get()).thenReturn(maxTree);
        when(treeFactory.buildMin(argThat(i -> i.length == 1 && i[0] == range))).thenReturn(minTreeFuture);
        when(treeFactory.buildMax(argThat(i -> i.length == 1 && i[0] == range))).thenReturn(maxTreeFuture);
        when(collectorFactory.createMinCollector(EXPECTED_NUM)).thenReturn(minCollector);
        when(collectorFactory.createMaxCollector(EXPECTED_NUM)).thenReturn(maxCollector);
        when(collectorFactory.createAction(minTree, minCollector)).thenReturn(minTask);
        when(collectorFactory.createAction(maxTree, maxCollector)).thenReturn(maxTask);
        when(pool.submit(minTask)).thenReturn(minTask);
        when(pool.submit(maxTask)).thenReturn(maxTask);
        setDone(minTask);
        setDone(maxTask);
        when(minCollector.merge(maxCollector)).thenReturn(result);
        when(result.size()).thenReturn(EXPECTED_RESULT);
        builder = classifierBuilderFactory.create();
        builder.addRange(EXPECTED_MIN, EXPECTED_MAX);
    }

    @Test
    public void build() throws Exception {
        Classifier classifier = builder.build();
        classifier.classify(EXPECTED_NUM, sink);
        verify(sink).found(EXPECTED_NUM, EXPECTED_RESULT);
        verify(treeFactory).close();
    }
}
