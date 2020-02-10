package org.sourcepond.challenge.testing;

import java.util.ResourceBundle;

public abstract class TestResourceBundle extends ResourceBundle {

    @Override
    public abstract Object handleGetObject(String key);
}
