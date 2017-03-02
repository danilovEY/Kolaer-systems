package ru.kolaer.asmc.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.model.MGroupDataServiceImpl;
import ru.kolaer.asmc.mvp.presenter.*;
import ru.kolaer.asmc.mvp.view.ImageViewPane;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Created by danilovey on 20.02.2017.
 */
public class AsmcPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private final static Logger log = LoggerFactory.getLogger(AsmcPlugin.class);
    private UniformSystemEditorKit editorKit;
    private PSplitListContent splitListContent;
    private BorderPane mainPane;

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

        final PGroupTree pGroupTree = new PGroupTreeImpl(this.editorKit);

        CompletableFuture.runAsync(() -> {
            final MGroupDataService mGroupDataService = new MGroupDataServiceImpl(this.editorKit);
            if(mGroupDataService.loadData()) {
                pGroupTree.setModel(mGroupDataService);
                Tools.runOnWithOutThreadFX(pGroupTree::updateView);
            } else {
                this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Ошибка при чтении данных!");
            }
        }, Executors.newSingleThreadExecutor()).exceptionally(t -> {
            log.error("Ошибка при чтении данных!", t);
            this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Ошибка при чтении данных!");
            return null;
        });

        final PContentLabel pContentLabel = new PContentLabelImpl();

        this.splitListContent = new PSplitListContentImpl(this.editorKit);
        this.splitListContent.setPContentLabel(pContentLabel);
        this.splitListContent.setPGroupList(pGroupTree);
        this.splitListContent.updateView();
        if(this.editorKit.getAuthentication().isAuthentication()) {
            this.login(this.editorKit.getAuthentication().getAuthorizedUser());
        } else {
            this.splitListContent.setAccess(false);
        }

        this.editorKit.getAuthentication().registerObserver(this);

        this.mainPane.setCenter(this.splitListContent.getView().getContent());

        this.updateBanner();
        this.initMenuBar();
    }

    private void initMenuBar() {
        final MenuItem authMenuItem = new MenuItem("Вход");

        final Menu asmcMenu = new Menu("АСУП");
        asmcMenu.getItems().add(authMenuItem);

        this.editorKit.getUISystemUS().getMenuBar().addMenu(asmcMenu);

        asmcMenu.addEventHandler(Menu.ON_SHOWING, e ->
            authMenuItem.setText( this.splitListContent.isAccess() ? "Выход" : "Вход")
        );

        authMenuItem.setOnAction(e -> {
            if(!this.splitListContent.isAccess()) {
                final Dialog<?> loginDialog = this.editorKit.getUISystemUS().getDialog().createLoginDialog();
                loginDialog.showAndWait().ifPresent(result -> {
                    final String[] logPass = result.toString().split("=");
                    if(logPass.length == 2) {
                        if(logPass[0].equals("root")
                                && logPass[1].equals("\u0032\u0073\u0065\u0072\u0064\u0063\u0065\u0033")) {
                            this.splitListContent.setAccess(true);

                            return;
                        }
                    }
                    this.editorKit.getUISystemUS().getNotification()
                            .showErrorNotifi("Ошибка!", "Не верный логин или пароль!");
                });
            } else {
                this.splitListContent.setAccess(false);
            }
        });
    }

    private void updateBanner() {
        Tools.runOnThreadFX(() -> {
            final BorderPane imagePane = new BorderPane();
            imagePane.setStyle("-fx-background-color: #FFFFFF"); //,linear-gradient(#f8f8f8, #e7e7e7);
            imagePane.setMaxHeight(300);
            imagePane.setMaxWidth(Double.MAX_VALUE);

            final ImageView left = new ImageView(new Image(this.getClass().getResource("/LR.png").toString(), true));
            left.setPreserveRatio(false);

            final ImageView right = new ImageView(new Image(this.getClass().getResource("/LR.png").toString(), true));
            right.setPreserveRatio(false);

            final ImageViewPane center = new ImageViewPane(new ImageView(new Image(this.getClass().getResource("/Centr.png").toString(), true)));

            imagePane.setRight(right);
            imagePane.setLeft(left);
            imagePane.setCenter(center);

            this.mainPane.setTop(imagePane);
        });
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void login(AccountEntity account) {
        if(this.splitListContent != null) {
            account.getRoles().stream()
                    .filter(roleEntity -> roleEntity.getType().equals("OIT"))
                    .findAny()
                    .ifPresent(role -> this.splitListContent.setAccess(true));
        }
    }

    @Override
    public void logout(AccountEntity account) {
      if(this.splitListContent != null)
          this.splitListContent.setAccess(false);
    }
}
