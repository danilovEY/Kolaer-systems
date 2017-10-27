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
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.model.DataService;
import ru.kolaer.asmc.mvp.model.DataServiceImpl;
import ru.kolaer.asmc.mvp.view.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
@Slf4j
public class AsmcPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private SplitListContentVc splitListContent;
    private BorderPane mainPane;
    private DataService dataService;
    private GroupTreeVc groupTreeVc;
    private ContentLabelVc contentLabelVc;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        editorKit.getAuthentication().registerObserver(this);

        dataService = new DataServiceImpl();

        groupTreeVc = new GroupTreeVcImpl(dataService);
        contentLabelVc = new ContentLabelVcImpl(dataService);

        dataService.registerObserver(groupTreeVc);
        dataService.registerObserver(contentLabelVc);

        splitListContent = new SplitListContentVcImpl(groupTreeVc, contentLabelVc);
    }

    @Override
    public void start() throws Exception {
        CompletableFuture.runAsync(() -> dataService.loadData());
    }

    private void initMenuBar() {
        MenuItem authMenuItem = new MenuItem("Вход");
        MenuItem updateMenuItem = new MenuItem("Обновить данные");

        Menu asmcMenu = new Menu("АСУП");
        asmcMenu.getItems().add(authMenuItem);

        UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getMenuBar()
                .addMenu(asmcMenu);

        asmcMenu.addEventHandler(Menu.ON_SHOWING, e ->
            authMenuItem.setText(splitListContent.isAccess() ? "Выход" : "Вход")
        );

        authMenuItem.setOnAction(e -> {
            if(!splitListContent.isAccess()) {
                Dialog<?> loginDialog =  UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getDialog()
                        .createLoginDialog();
                loginDialog.showAndWait().ifPresent(result -> {
                    String[] logPass = result.toString().split("=");
                    if(logPass.length == 2) {
                        if(logPass[0].equals("root")
                                && logPass[1].equals("\u0032\u0073\u0065\u0072\u0064\u0063\u0065\u0033")) {
                            splitListContent.setAccess(true);

                            return;
                        }
                    }
                    UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                            .showErrorNotify("Ошибка!", "Не верный логин или пароль!");
                });
            } else {
                splitListContent.setAccess(false);
            }
        });

        updateMenuItem.setOnAction(e -> {
            CompletableFuture
                    .runAsync(() -> dataService.loadData())
                    .exceptionally(t -> {
                        log.error("");
                        UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getNotification()
                                .showErrorNotify("Ошибка", "Не удалось прочитать данные");
                        return null;
                    });
        });
    }

    private void updateBanner() {
        Tools.runOnWithOutThreadFX(() -> {
            BorderPane imagePane = new BorderPane();
            imagePane.setStyle("-fx-background-color: #FFFFFF"); //,linear-gradient(#f8f8f8, #e7e7e7);
            imagePane.setMaxHeight(300);
            imagePane.setMaxWidth(Double.MAX_VALUE);

            ImageView left = new ImageView(new Image(getClass().getResource("/LR.png").toString(), true));
            left.setPreserveRatio(false);

            ImageView right = new ImageView(new Image(getClass().getResource("/LR.png").toString(), true));
            right.setPreserveRatio(false);

            ImageViewPane center = new ImageViewPane(new ImageView(new Image(getClass().getResource("/Centr.png").toString(), true)));

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

        splitListContent.initView(initSplit -> {
            groupTreeVc.initView(BaseView::empty);
            contentLabelVc.initView(BaseView::empty);
            initSplit.setView(groupTreeVc, contentLabelVc);
        });

        if(UniformSystemEditorKitSingleton.getInstance().getAuthentication().isAuthentication()) {
            login(UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser());
        } else {
            splitListContent.setAccess(false);
        }

        mainPane.setCenter(splitListContent.getContent());

        updateBanner();
        initMenuBar();

        viewVisit.accept(this);
    }
}
