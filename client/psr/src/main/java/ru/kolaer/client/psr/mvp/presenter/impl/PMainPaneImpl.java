package ru.kolaer.client.psr.mvp.presenter.impl;

import javafx.scene.control.Dialog;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.psr.mvp.presenter.PMainPane;
import ru.kolaer.client.psr.mvp.presenter.PPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.VMainPane;
import ru.kolaer.client.psr.mvp.view.impl.VMainPaneImpl;

/**
 * Created by danilovey on 01.08.2016.
 */
public class PMainPaneImpl implements PMainPane {
    private final VMainPane view;
    private final UniformSystemEditorKit editorKit;
    private final PPsrRegisterTable pPsrRegisterTable;

    public PMainPaneImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.pPsrRegisterTable = new PPsrRegisterTableImpl(editorKit);
        this.view = new VMainPaneImpl();

    }

    @Override
    public VMainPane getView() {
        return this.view;
    }

    @Override
    public void updatePluginPage() {
        this.view.initializationView();
        pPsrRegisterTable.updateTableData();
        this.view.setContent(pPsrRegisterTable.getView().getContent());

        if(this.editorKit.getAuthentication().isAuthentication()) {
            this.login(this.editorKit.getAuthentication().getAuthorizedUser());
        }

    }

    @Override
    public void login(GeneralAccountsEntity account) {
        this.view.setUserName(account.getUsername());
        this.view.logoutAction(e -> {
            this.editorKit.getAuthentication().logout();
        });
    }

    @Override
    public void logout(GeneralAccountsEntity account) {
        this.view.setUserName("Вход");
        this.view.loginAction(e -> {
            final Dialog loginDialog = this.editorKit.getUISystemUS().getDialog().createLoginDialog();
            loginDialog.showAndWait();
            String[] logPassArray = loginDialog.getResult().toString().split("=");
            this.editorKit.getAuthentication().login(new UserAndPassJson(logPassArray[0], logPassArray[1]));
        });
    }
}
