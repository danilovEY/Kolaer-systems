package ru.kolaer.kolpass.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;

/**
 * Created by danilovey on 09.02.2017.
 */
@Slf4j
public class KolpassPlugin implements UniformSystemPlugin {
    private BorderPane mainPane;
    private UniformSystemEditorKit editorKit;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
    }

    @Override
    public URL getIcon() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return null;
    }

    @Override
    public void start() throws Exception {
        this.mainPane = new BorderPane();
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {

    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }
}
