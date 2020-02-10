package org.sourcepond.challenge.app;

import org.sourcepond.challenge.api.Classifier;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.Future;

class ResultSinkFactory {

    public ResultSinkImpl createResultSink(Future<Classifier> classifier, Random random, PrintWriter console) {
        return new ResultSinkImpl(classifier, random, console);
    }
}
