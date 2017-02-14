package ru.kolaer.kolpass.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.kolpass.mvp.presenter.PRepositoryPaneImpl;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by danilovey on 09.02.2017.
 */
@Slf4j
public class KolpassPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private PRepositoryPaneImpl pRepositoryPane;
    private Button loginButton;
    private BorderPane mainPane;
    private UniformSystemEditorKit editorKit;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
        this.editorKit.getAuthentication().registerObserver(this);
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
        this.loginButton = new Button("Авторизоваться");
        this.loginButton.setOnAction(e -> this.editorKit.getUISystemUS().getDialog().createAndShowLoginDialog());

        this.pRepositoryPane = new PRepositoryPaneImpl(this.editorKit);

        if(editorKit.getAuthentication().isAuthentication())
            this.login(null);
        else
            this.logout(null);
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

    @Override
    public void login(AccountEntity account) {
        Optional.ofNullable(this.pRepositoryPane).ifPresent(pane ->
            Tools.runOnWithOutThreadFX(() -> {
                this.mainPane.setCenter(this.pRepositoryPane.getView().getContent());
                this.pRepositoryPane.setModel(this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getKolpassTable());
            })
        );
    }

    @Override
    public void logout(AccountEntity account) {
        this.mainPane.setCenter(this.loginButton);
    }
}
