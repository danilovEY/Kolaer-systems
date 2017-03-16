package ru.kolaer.client.wer.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.wer.mvp.model.CmdArguments;
import ru.kolaer.client.wer.mvp.model.MWindowsEventCmdSecurity;
import ru.kolaer.client.wer.mvp.presenter.PDetailedEventImpl;
import ru.kolaer.client.wer.mvp.presenter.PEventTableImpl;
import ru.kolaer.client.wer.mvp.presenter.PSplitTableDetailedEventImpl;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by danilovey on 14.03.2017.
 */
public class WerPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private UniformSystemEditorKit editorKit;
    private BorderPane mainPane;
    private PEventTableImpl pEventTable;
    private MWindowsEventCmdSecurity mWindowsEvent;
    private PDetailedEventImpl detailedEvent;
    private PSplitTableDetailedEventImpl splitPresenter;

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;

        this.mWindowsEvent = new MWindowsEventCmdSecurity(this.editorKit);
        this.mWindowsEvent.getModel().setMaxCountLoad(20);

        this.editorKit.getAuthentication().registerObserver(this);
    }

    @Override
    public URL getIcon() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return Arrays.asList(this.mWindowsEvent);
    }

    @Override
    public void start() throws Exception {
        this.mainPane = new BorderPane();

        this.pEventTable = new PEventTableImpl(this.editorKit, this.mWindowsEvent);
        this.detailedEvent = new PDetailedEventImpl();

        this.splitPresenter = new PSplitTableDetailedEventImpl();
        this.splitPresenter.setDetailedEvent(this.detailedEvent);
        this.splitPresenter.setEventTable(this.pEventTable);
        this.splitPresenter.updateView();

        if(this.editorKit.getAuthentication().isAuthentication()) {
            this.login(this.editorKit.getAuthentication().getAuthorizedUser());
        }
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void login(AccountEntity account) {
        if(account.getRoles().stream().anyMatch(role -> role.getType().equals("OIT"))
                && this.mainPane != null) {
            Tools.runOnWithOutThreadFX(() -> {
                final TextField hostNameText = new TextField();
                final TextField userNameText = new TextField();
                final PasswordField passwordText = new PasswordField();
                final Button connectButton = new Button("Подключится");
                connectButton.setOnAction(e -> {
                    final CmdArguments cmdArguments = new CmdArguments();
                    cmdArguments.setHost(hostNameText.getText());
                    cmdArguments.setUsername(userNameText.getText());
                    cmdArguments.setPassword(passwordText.getText());
                    cmdArguments.setMaxCountLoad(20);

                    this.mWindowsEvent.setModel(cmdArguments);

                    if(this.mWindowsEvent.loadLastWindowsEvent().isPresent()) {
                        this.pEventTable.updateView();
                        this.mainPane.setCenter(this.splitPresenter.getView().getContent());
                    }
                });

                final VBox content = new VBox(new FlowPane(new Label("Имя сервера"), hostNameText),
                        new FlowPane(new Label("Имя пользователя"), userNameText),
                        new FlowPane(new Label("Пароль пользователя"), passwordText),
                        connectButton);

                this.mainPane.setCenter(content);
            });


        }
    }

    @Override
    public void logout(AccountEntity account) {
        Optional.ofNullable(this.mainPane).ifPresent(pane -> {
            pane.setCenter(null);
        });
    }
}
