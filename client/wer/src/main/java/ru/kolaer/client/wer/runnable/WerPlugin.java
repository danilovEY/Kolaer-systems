package ru.kolaer.client.wer.runnable;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.wer.mvp.model.MWindowsEventCmdSecurity;
import ru.kolaer.client.wer.mvp.presenter.PDetailedEventImpl;
import ru.kolaer.client.wer.mvp.presenter.PEventTableImpl;
import ru.kolaer.client.wer.mvp.presenter.PSplitTableDetailedEventImpl;

import java.net.URL;
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
        this.mWindowsEvent = new MWindowsEventCmdSecurity();
        this.pEventTable = new PEventTableImpl(this.mWindowsEvent);
        this.detailedEvent = new PDetailedEventImpl();

        this.splitPresenter = new PSplitTableDetailedEventImpl();
        this.splitPresenter.setDetailedEvent(this.detailedEvent);
        this.splitPresenter.setEventTable(this.pEventTable);
        this.splitPresenter.updateView();

        if(this.editorKit.getAuthentication().isAuthentication()) {
            this.login(this.editorKit.getAuthentication().getAuthorizedUser());
            this.pEventTable.updateView();
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
        Optional.ofNullable(this.mainPane).ifPresent(pane ->
            Tools.runOnWithOutThreadFX(() ->
                    pane.setCenter(this.splitPresenter.getView().getContent())
            )
        );
    }

    @Override
    public void logout(AccountEntity account) {
        Optional.ofNullable(this.mainPane).ifPresent(pane -> {
            pane.setCenter(null);
        });
    }
}
