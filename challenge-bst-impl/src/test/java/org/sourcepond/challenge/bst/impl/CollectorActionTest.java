package org.sourcepond.challenge.bst.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CollectorActionTest {
    private final Node node = mock(Node.class);
    private final Node left = mock(Node.class);
    private final Node right = mock(Node.class);
    private final Collector collector = mock(Collector.class);
    private CollectorAction action = new CollectorFactory().createAction(node, collector);

    @BeforeEach
    public void setup() {
        when(collector.accept(node)).thenReturn(true);
    }

    @Test
    public void nodeNotAccepted() {
        when(collector.accept(node)).thenReturn(false);
        action.compute();
        verify(collector).accept(node);
        verifyNoMoreInteractions(collector);
    }

    private void oneBranchOnly(Node branch) {
        action.compute();
        InOrder order = inOrder(collector);
        order.verify(collector).accept(node);
        order.verify(collector).accept(branch);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void leftBranchAvailableOnly() {
        when(node.getLeft()).thenReturn(left);
        oneBranchOnly(left);
    }

    @Test
    public void rightBranchAvailableOnly() {
        when(node.getRight()).thenReturn(right);
        oneBranchOnly(right);
    }

    @Test
    public void bothBranchesAvailable() {
        when(node.getLeft()).thenReturn(left);
        when(node.getRight()).thenReturn(right);
        action.compute();
        verify(collector).accept(node);
        verify(collector).accept(left);
        verify(collector).accept(right);
    }
}
