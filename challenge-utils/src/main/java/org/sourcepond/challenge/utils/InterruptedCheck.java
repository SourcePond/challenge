package org.sourcepond.challenge.utils;

import static java.lang.Thread.currentThread;

public class InterruptedCheck {

    public static boolean checkInterrupted() throws InterruptedException {
        if (currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        return true;
    }
}
