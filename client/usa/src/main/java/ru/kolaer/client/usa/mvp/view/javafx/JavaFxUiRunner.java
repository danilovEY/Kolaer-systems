package ru.kolaer.client.usa.mvp.view.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import ru.kolaer.client.usa.mvp.view.ApplicationUiRunner;
import ru.kolaer.client.usa.mvp.viewmodel.impl.VMMainFrameImpl;

/**
 * Created by danilovey on 13.10.2017.
 */
public class JavaFxUiRunner implements ApplicationUiRunner {
    @Override
    public void run(String[] args) {
        Platform.setImplicitExit(false);
        Application.launch(VMMainFrameImpl.class ,args);
    }
}
