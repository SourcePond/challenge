package org.sourcepond.challenge.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcepond.challenge.testing.TestResourceBundle;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;

import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.sourcepond.challenge.app.CLI.DEFAULT_NUM_OF_RANGES;
import static org.sourcepond.challenge.app.CLI.MAX_VALUE;
import static org.sourcepond.challenge.app.CLI.NOT_IN_ALLOWED_RANGE_MESSAGE_KEY;
import static org.sourcepond.challenge.app.CLI.NUM_NOT_PARSABLE_MESSAGE_KEY;
import static org.sourcepond.challenge.app.CLI.READ_NUM_OF_RANGES_MESSAGE_KEY;


public class CLITest {
    private static final String EXPECTED_NUM_NOT_PARSABLE_MESSAGE_FORMAT = "aaa %s %s";
    private static final String EXPECTED_NOT_IN_ALLOWED_RANGE_MESSAGE_FORMAT = "bbbb %d";
    private static final String EXPECTED_READ_NUM_OF_RANGES_FORMAT = "ZZZ";
    private static final String UNPARSABLE_INPUT = "ABC";
    private final TestResourceBundle bundle = mock(TestResourceBundle.class);
    private final PrintWriter console = mock(PrintWriter.class);

    @BeforeEach
    public void setup() {
        when(bundle.handleGetObject(NOT_IN_ALLOWED_RANGE_MESSAGE_KEY)).thenReturn(EXPECTED_NOT_IN_ALLOWED_RANGE_MESSAGE_FORMAT);
        when(bundle.handleGetObject(NUM_NOT_PARSABLE_MESSAGE_KEY)).thenReturn(EXPECTED_NUM_NOT_PARSABLE_MESSAGE_FORMAT);
        when(bundle.handleGetObject(READ_NUM_OF_RANGES_MESSAGE_KEY)).thenReturn(EXPECTED_READ_NUM_OF_RANGES_FORMAT);
        when(console.printf(EXPECTED_READ_NUM_OF_RANGES_FORMAT)).thenReturn(console);
        when(console.printf(EXPECTED_NUM_NOT_PARSABLE_MESSAGE_FORMAT, UNPARSABLE_INPUT, DEFAULT_NUM_OF_RANGES)).thenReturn(console);
    }

    @Test
    public void readNumOfRanges() {
        var in = new ByteArrayInputStream("10".getBytes(UTF_8));
        var cli = new CLI(bundle, in, console);
        assertEquals(10, cli.readNumOfRanges());
        verify(console).printf(EXPECTED_READ_NUM_OF_RANGES_FORMAT);
        verify(console).flush();
        verifyNoMoreInteractions(console);
    }

    @Test
    public void numNotParsable() {
        var in = new ByteArrayInputStream(UNPARSABLE_INPUT.getBytes(UTF_8));
        var cli = new CLI(bundle, in, console);
        assertEquals(DEFAULT_NUM_OF_RANGES, cli.readNumOfRanges());
        verify(console).printf(EXPECTED_READ_NUM_OF_RANGES_FORMAT);
        verify(console).printf(EXPECTED_NUM_NOT_PARSABLE_MESSAGE_FORMAT, UNPARSABLE_INPUT, DEFAULT_NUM_OF_RANGES);
        verify(console, times(2)).flush();
        verifyNoMoreInteractions(console);
    }

    @Test
    public void numIsLessThanAllowedMin() {
        var in = new ByteArrayInputStream("0".getBytes(UTF_8));
        var cli = new CLI(bundle, in, console);
        assertThrows(IllegalArgumentException.class, () -> cli.readNumOfRanges());
        verify(console).printf(EXPECTED_READ_NUM_OF_RANGES_FORMAT);
        verify(console).flush();
        verifyNoMoreInteractions(console);
    }

    @Test
    public void numIsHigherThanAllowedMin() {
        var in = new ByteArrayInputStream(valueOf(MAX_VALUE + 1).getBytes(UTF_8));
        var cli = new CLI(bundle, in, console);
        assertThrows(IllegalArgumentException.class, () -> cli.readNumOfRanges());
        verify(console).printf(EXPECTED_READ_NUM_OF_RANGES_FORMAT);
        verify(console).flush();
        verifyNoMoreInteractions(console);
    }
}
