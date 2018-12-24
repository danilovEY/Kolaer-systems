package ru.kolaer.kolpass.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.observers.AuthenticationObserver;
import ru.kolaer.common.plugins.UniformSystemPlugin;
import ru.kolaer.common.system.UniformSystemEditorKit;
import ru.kolaer.common.tools.Tools;
import ru.kolaer.kolpass.mvp.presenter.PEmployeeRepositoryListImpl;
import ru.kolaer.kolpass.mvp.presenter.PRepositoryContentImpl;
import ru.kolaer.kolpass.mvp.presenter.PSplitContentAndListRep;
import ru.kolaer.kolpass.mvp.presenter.PSplitContentAndListRepImpl;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 09.02.2017.
 */
public class KolpassPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private static final Logger log = LoggerFactory.getLogger(KolpassPlugin.class);
    private PSplitContentAndListRep pSplitContentAndListRep;
    private Button loginButton;
    private BorderPane mainPane;
    private UniformSystemEditorKit editorKit;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
        this.editorKit.getAuthentication().registerObserver(this);
    }

    @Override
    public void stop() throws Exception {
        this.pSplitContentAndListRep.clear();
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void login(AccountDto account) {
        Optional.ofNullable(this.pSplitContentAndListRep).ifPresent(pane ->
            Tools.runOnWithOutThreadFX(() -> {
                this.mainPane.setCenter(this.pSplitContentAndListRep.getView().getContent());
                this.pSplitContentAndListRep.setModel(this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getKolpassTable());
                this.pSplitContentAndListRep.updateView();
            })
        );
    }

    @Override
    public void logout(AccountDto account) {
        Optional.ofNullable(this.mainPane).ifPresent(pane -> {
            this.pSplitContentAndListRep.clear();
            pane.setCenter(this.loginButton);
        });
    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        this.mainPane = new BorderPane();


        this.loginButton = new Button("Авторизоваться");
        this.loginButton.setOnAction(e -> this.editorKit.getUISystemUS().getDialog().createAndShowLoginToSystemDialog());

        this.pSplitContentAndListRep = new PSplitContentAndListRepImpl(this.editorKit);
        this.pSplitContentAndListRep.setContent(new PRepositoryContentImpl(this.editorKit));
        this.pSplitContentAndListRep.setEmployeeList(new PEmployeeRepositoryListImpl(this.editorKit));

        if(editorKit.getAuthentication().isAuthentication()) {
            this.login(null);
        }
        else {
            this.logout(null);
        }

        viewVisit.accept(this);
    }
}
