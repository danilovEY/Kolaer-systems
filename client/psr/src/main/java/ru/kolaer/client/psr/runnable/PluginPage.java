package ru.kolaer.client.psr.runnable;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.presenter.PMainPane;
import ru.kolaer.client.psr.mvp.presenter.impl.PMainPaneImpl;

import java.net.URL;
import java.util.Collection;

/**
 * Created by Danilov on 28.07.2016.
 */
public class PluginPage implements UniformSystemPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(PluginPage.class);
    private UniformSystemEditorKit editorKit;
    private PMainPane mainPane;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
        this.mainPane = new PMainPaneImpl(editorKit);
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
        if(!this.editorKit.getAuthentication().isAuthentication())
            this.editorKit.getAuthentication().login(new UserAndPassJson("anonymous", "anonymous"));

        Tools.runOnThreadFX(this.mainPane::updatePluginPage);
        this.editorKit.getAuthentication().registerObserver(this.mainPane);
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.getView().setContent(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane.getView().getContent();
    }
}
