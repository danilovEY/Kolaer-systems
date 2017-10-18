package ru.kolaer.client.usa.mvp.view.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.usa.mvp.view.AbstractApplicationUiRunner;
import ru.kolaer.client.usa.mvp.view.ApplicationUiRunner;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;

import java.util.concurrent.Executors;

/**
 * Created by danilovey on 13.10.2017.
 */
public class JavaFxUiRunner extends AbstractApplicationUiRunner {
    private static final Logger log = LoggerFactory.getLogger(JavaFxUiRunner.class);
    private ApplicationUiRunner javaFxUiRunner;

    @Override
    public boolean initializeUi() {
        Platform.setImplicitExit(false);
        Executors.newSingleThreadExecutor().submit(() -> Application.launch(VMMainFrameImpl.class));
        synchronized (VMMainFrameImpl.lock) {
            try {
                VMMainFrameImpl.lock.wait();
            } catch (InterruptedException e) {
                return false;
            }
            javaFxUiRunner = VMMainFrameImpl.getInstance();
        }

        return true;
    }

    @Override
    public UniformSystemEditorKit initializeUniformSystemEditorKit() {
        return javaFxUiRunner.initializeUniformSystemEditorKit();
    }

    @Override
    public VMainFrame getFrame() {
        return javaFxUiRunner.getFrame();
    }

    @Override
    public VTabExplorer getExplorer() {
        return javaFxUiRunner.getExplorer();
    }

    @Override
    public TypeUi getTypeUi() {
        return javaFxUiRunner.getTypeUi();
    }

    @Override
    public void shutdown() {
        Platform.exit();
    }
}
