package ru.kolaer.asmc.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.model.MGroupDataServiceImpl;
import ru.kolaer.asmc.mvp.presenter.*;
import ru.kolaer.asmc.mvp.view.ImageViewPane;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
@Slf4j
public class AsmcPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private PSplitListContent splitListContent;
    private BorderPane mainPane;

    private void initMenuBar() {
        final MenuItem authMenuItem = new MenuItem("Вход");

        final Menu asmcMenu = new Menu("АСУП");
        asmcMenu.getItems().add(authMenuItem);

        UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getMenuBar()
                .addMenu(asmcMenu);

        asmcMenu.addEventHandler(Menu.ON_SHOWING, e ->
            authMenuItem.setText( this.splitListContent.isAccess() ? "Выход" : "Вход")
        );

        authMenuItem.setOnAction(e -> {
            if(!this.splitListContent.isAccess()) {
                Dialog<?> loginDialog =  UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getDialog()
                        .createLoginDialog();
                loginDialog.showAndWait().ifPresent(result -> {
                    String[] logPass = result.toString().split("=");
                    if(logPass.length == 2) {
                        if(logPass[0].equals("root")
                                && logPass[1].equals("\u0032\u0073\u0065\u0072\u0064\u0063\u0065\u0033")) {
                            this.splitListContent.setAccess(true);

                            return;
                        }
                    }
                    UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                            .showErrorNotify("Ошибка!", "Не верный логин или пароль!");
                });
            } else {
                this.splitListContent.setAccess(false);
            }
        });
    }

    private void updateBanner() {
        Tools.runOnWithOutThreadFX(() -> {
            BorderPane imagePane = new BorderPane();
            imagePane.setStyle("-fx-background-color: #FFFFFF"); //,linear-gradient(#f8f8f8, #e7e7e7);
            imagePane.setMaxHeight(300);
            imagePane.setMaxWidth(Double.MAX_VALUE);

            ImageView left = new ImageView(new Image(this.getClass().getResource("/LR.png").toString(), true));
            left.setPreserveRatio(false);

            ImageView right = new ImageView(new Image(this.getClass().getResource("/LR.png").toString(), true));
            right.setPreserveRatio(false);

            ImageViewPane center = new ImageViewPane(new ImageView(new Image(this.getClass().getResource("/Centr.png").toString(), true)));

            imagePane.setRight(right);
            imagePane.setLeft(left);
            imagePane.setCenter(center);

            this.mainPane.setTop(imagePane);
        });
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void login(AccountDto account) {
        if(this.splitListContent != null && account.isAccessOit()) {
            this.splitListContent.setAccess(true);
        }
    }

    @Override
    public void logout(AccountDto account) {
      if(this.splitListContent != null)
          this.splitListContent.setAccess(false);
    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        this.mainPane = new BorderPane();

        PGroupTree pGroupTree = new PGroupTreeImpl();

        CompletableFuture.runAsync(() -> {
            MGroupDataService mGroupDataService = new MGroupDataServiceImpl();
            if(mGroupDataService.loadData()) {
                pGroupTree.setModel(mGroupDataService);
                Tools.runOnWithOutThreadFX(pGroupTree::updateView);
            } else {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify("Ошибка!", "Ошибка при чтении данных!");
            }
        }).exceptionally(t -> {
            log.error("Ошибка при чтении данных!", t);
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify("Ошибка!", "Ошибка при чтении данных!");
            return null;
        });

        final PContentLabel pContentLabel = new PContentLabelImpl();

        this.splitListContent = new PSplitListContentImpl();
        this.splitListContent.setPContentLabel(pContentLabel);
        this.splitListContent.setPGroupList(pGroupTree);
        this.splitListContent.updateView();
        if(UniformSystemEditorKitSingleton.getInstance().getAuthentication().isAuthentication()) {
            this.login( UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser());
        } else {
            this.splitListContent.setAccess(false);
        }

        UniformSystemEditorKitSingleton.getInstance().getAuthentication().registerObserver(this);

        this.mainPane.setCenter(this.splitListContent.getView().getContent());

        this.updateBanner();
        this.initMenuBar();

        viewVisit.accept(this);
    }
}
