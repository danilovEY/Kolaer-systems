package ru.kolaer.client.psr.mvp.presenter.impl;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.model.impl.MPsrRegisterImpl;
import ru.kolaer.client.psr.mvp.presenter.PDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.presenter.PMainPane;
import ru.kolaer.client.psr.mvp.presenter.PPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.VMainPane;
import ru.kolaer.client.psr.mvp.view.impl.VMainPaneImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by danilovey on 01.08.2016.
 */
public class PMainPaneImpl implements PMainPane {
    private static final Logger LOG = LoggerFactory.getLogger(PMainPaneImpl.class);
    private final VMainPane view;
    private final UniformSystemEditorKit editorKit;
    private PPsrRegisterTable pPsrRegisterTable;
    private MPsrRegisterImpl model;

    public PMainPaneImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.view = new VMainPaneImpl();
        this.model = new MPsrRegisterImpl(editorKit);
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
            this.pPsrRegisterTable.setModel(this.model);
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

            detailsOrEditPsrRegister.showAndWait();
            PsrRegister psrRegister = detailsOrEditPsrRegister.getPsrRegister();
            psrRegister.setAuthor(this.editorKit.getAuthentication().getAuthorizedUser().getGeneralEmployeesEntity());
            this.model.addPsrProject(detailsOrEditPsrRegister.getPsrRegister());

            this.pPsrRegisterTable.updateTableData();
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
                    updateTitle("Подключение к серверу");
                    updateMessage("Проверка доступности сервера...");
                    if(editorKit.getUSNetwork().getKolaerWebServer().getServerStatus() == ServerStatus.AVAILABLE) {
                        updateMessage("Авторизация...");
                        try {
                            if(editorKit.getAuthentication().login(new UserAndPassJson(logPassArray[0], logPassArray[1]))) {
                                GeneralEmployeesEntity entity = editorKit.getAuthentication().getAuthorizedUser().getGeneralEmployeesEntity();
                                if(entity == null) {
                                    editorKit.getAuthentication().logout();
                                    Tools.runOnThreadFX(() ->{
                                        editorKit.getUISystemUS().getDialog().createErrorDialog("Ошибка!", "К аккаунту не привязан сотрудник!").show();
                                    });
                                }
                            }

                        } catch (ServerException ex) {
                            updateMessage("Не удалось авторизоваться!!");
                            this.setException(ex);
                            Tools.runOnThreadFX(() ->{
                                editorKit.getUISystemUS().getDialog().createErrorDialog("Ошибка!", "Неудалось авторизоватся!").show();
                            });
                        }
                    }
                    updateProgress(100,100);
                    return null;
                }
            };

            Tools.runOnThreadFX(() -> {
                ProgressDialog dlg = new ProgressDialog(worker);
                dlg.showAndWait();
            });

            ExecutorService authThread = Executors.newSingleThreadExecutor();
            CompletableFuture.runAsync(worker, authThread).exceptionally(t -> {
                LOG.error("Не удалось авторизоватся!", t);
                return null;
            });
        });

    }
}
