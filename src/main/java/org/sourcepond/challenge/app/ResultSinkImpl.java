package org.sourcepond.challenge.app;

import org.sourcepond.challenge.api.Classifier;
import org.sourcepond.challenge.api.ResultSink;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.Thread.currentThread;
import static org.sourcepond.challenge.app.CLI.MAX_VALUE;

class ResultSinkImpl implements ResultSink, Runnable {
    static final String PRINT_FORMAT = "%d => Enclosed by %d range(s)%n";
    static final String ERROR_FORMAT = "Classification failed, message: %s";
    private final Future<Classifier> classifier;
    private final Random random;
    private final PrintWriter console;

    public ResultSinkImpl(Future<Classifier> classifier, Random random, PrintWriter console) {
        this.classifier = classifier;
        this.random = random;
        this.console = console;
    }

    @Override
    public void run() {
        try (var cl = classifier.get()) {
            while (!currentThread().isInterrupted()) {
                cl.classify(random.nextInt(MAX_VALUE), this);
            }
        } catch (InterruptedException | ExecutionException e) {
            console.printf(ERROR_FORMAT, e.getMessage());
        }
    }

    @Override
    public void found(int num, int matchingRanges) {
        console.printf(PRINT_FORMAT, num, matchingRanges).flush();
    }
}
