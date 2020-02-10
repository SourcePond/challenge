package org.sourcepond.challenge.app;

import java.io.Closeable;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.Validate.inclusiveBetween;

class CLI implements Closeable {
    public static final int DEFAULT_NUM_OF_RANGES = 1_000_000;
    public static final int MAX_VALUE = 1_000_000_000;
    public static final int MIN_VALUE = 1;
    public static final String READ_NUM_OF_RANGES_MESSAGE_KEY = "read.num.of.ranges";
    public static final String NUM_NOT_PARSABLE_MESSAGE_KEY = "num.not.parsable";
    public static final String NOT_IN_ALLOWED_RANGE_MESSAGE_KEY = "not.in.allowed.range";
    private final Scanner scanner;
    private final ResourceBundle bundle;
    private final PrintWriter console;

    public CLI(ResourceBundle bundle, InputStream in, PrintWriter console) {
        scanner = new Scanner(in);
        this.bundle = bundle;
        this.console = console;
    }

    public int readNumOfRanges() {
        console.printf(bundle.getString(READ_NUM_OF_RANGES_MESSAGE_KEY)).flush();
        var next = scanner.nextLine();
        int numOfRanges;
        try {
            numOfRanges = parseInt(next);
        } catch (NumberFormatException e) {
            console.printf(bundle.getString(NUM_NOT_PARSABLE_MESSAGE_KEY), next, DEFAULT_NUM_OF_RANGES).flush();
            numOfRanges = DEFAULT_NUM_OF_RANGES;
        }
        inclusiveBetween(MIN_VALUE, MAX_VALUE, numOfRanges, bundle.getString(NOT_IN_ALLOWED_RANGE_MESSAGE_KEY), numOfRanges, MIN_VALUE, MAX_VALUE);
        return numOfRanges;
    }

    public void awaitUserExit() {
        scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
