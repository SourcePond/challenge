package org.sourcepond.challenge.app;

import org.sourcepond.challenge.api.Classifier;
import org.sourcepond.challenge.api.ClassifierBuilderFactory;

import java.io.PrintWriter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.sourcepond.challenge.app.CLI.MAX_VALUE;
import static org.sourcepond.challenge.utils.InterruptedCheck.checkInterrupted;

class ClassifierFactory {
    public static final String CREATE_RANGES_MESSAGE = "create.ranges.message";
    private final ClassifierBuilderFactory factory;
    private final ExecutorService executor;
    private final Random random;
    private final ResourceBundle bundle;
    private final PrintWriter console;

    public ClassifierFactory(ClassifierBuilderFactory factory,
                             ExecutorService executor,
                             Random random,
                             ResourceBundle bundle,
                             PrintWriter console) {
        this.factory = factory;
        this.executor = executor;
        this.random = random;
        this.bundle = bundle;
        this.console = console;
    }

    public Future<Classifier> create(int numOfRanges) {
        return executor.submit(() -> {
            console.printf(bundle.getString(CREATE_RANGES_MESSAGE), numOfRanges).flush();
            var builder = factory.create();
            for (int i = 0; i < numOfRanges && checkInterrupted() ; i++){
                int n1 = random.nextInt(MAX_VALUE);
                int n2 = random.nextInt(MAX_VALUE);
                if (n1 > n2) {
                    builder.addRange(n2, n1);
                } else {
                    builder.addRange(n1, n2);
                }
            }
            return builder.build();
        });
    }
}
