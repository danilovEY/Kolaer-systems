package ru.kolaer.client.psr.mvp.presenter.impl;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.presenter.PDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.presenter.PMainPane;
import ru.kolaer.client.psr.mvp.presenter.PPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.VMainPane;
import ru.kolaer.client.psr.mvp.view.impl.VMainPaneImpl;

import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 01.08.2016.
 */
public class PMainPaneImpl implements PMainPane {
    private static final Logger LOG = LoggerFactory.getLogger(PMainPaneImpl.class);
    private final VMainPane view;
    private final UniformSystemEditorKit editorKit;
    private PPsrRegisterTable pPsrRegisterTable;

    public PMainPaneImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.view = new VMainPaneImpl();

    }

    @Override
    public VMainPane getView() {
        return this.view;
    }

    @Override
    public void updatePluginPage() {
        if(!this.view.isInitializationView()) {
            this.view.initializationView();

            this.pPsrRegisterTable = new PPsrRegisterTableImpl(editorKit);
            this.pPsrRegisterTable.updateTableData();

            this.view.setContent(pPsrRegisterTable.getView().getContent());

            if(this.editorKit.getAuthentication().isAuthentication()) {
                this.login(this.editorKit.getAuthentication().getAuthorizedUser());
            }
        }




    }

    @Override
    public void login(GeneralAccountsEntity account) {
        this.view.setUserName("Вошли как: (" + account.getUsername() + ") | Выход");
        this.view.logoutAction(e -> this.editorKit.getAuthentication().logout());

        this.view.createPsrAction(e -> {
            final PDetailsOrEditPsrRegister detailsOrEditPsrRegister = new PDetailsOrEditPsrRegisterImpl();
            detailsOrEditPsrRegister.getView().initializationView();

            detailsOrEditPsrRegister.getView().showAndWait();
        });

        account.getRoles().forEach(role -> {
            if(role.getType() == EnumRole.ANONYMOUS) {
                this.view.createPsrAction(e -> {
                    Alert warningAlert = new Alert(Alert.AlertType.ERROR);
                    warningAlert.setTitle("Ошибка");
                    warningAlert.setHeaderText("Авторизируйтесь!");
                    warningAlert.show();
                });
                return;
            }
        });

    }

    @Override
    public void logout(GeneralAccountsEntity account) {
        this.view.setUserName("Вход");
        this.view.loginAction(e -> {
            final Dialog loginDialog = this.editorKit.getUISystemUS().getDialog().createLoginDialog();
            loginDialog.showAndWait();
            String[] logPassArray = loginDialog.getResult().toString().split("=");
            Task<Object> worker = new Task<Object>() {
                @Override
                protected Object call() throws Exception {
                    updateMessage("Connect...");

                    editorKit.getAuthentication().login(new UserAndPassJson(logPassArray[0], logPassArray[1]));
                    updateProgress(100,100);
                    return null;
                }
            };

            Tools.runOnThreadFX(() -> {
                ProgressDialog dlg = new ProgressDialog(worker);
                dlg.showAndWait();
            });

            Thread th = new Thread(worker);
            th.setDaemon(true);
            th.start();

        });

    }
}
