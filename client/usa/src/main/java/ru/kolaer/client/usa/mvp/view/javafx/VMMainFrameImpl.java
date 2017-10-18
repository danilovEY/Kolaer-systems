package ru.kolaer.client.usa.mvp.view.javafx;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.mvp.view.ApplicationUiRunner;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.usa.system.network.NetworkUSRestTemplate;
import ru.kolaer.client.usa.system.ui.MenuBarUSJavaFx;
import ru.kolaer.client.usa.system.ui.NotificationJavaFxExceptionHandler;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;

import java.util.function.Function;

/**
 * Главное окно приложения.
 *
 * @author Danilov
 * @version 0.1
 */
public class VMMainFrameImpl extends Application implements VMainFrame<Parent>, ApplicationUiRunner {
    private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
    public static final Object lock = new Object();
    private static ApplicationUiRunner uiRunner;

    /**
     * Панель с контентом главного окна.
     */
    private BorderPane mainPane;

    /**
     * Менеджер служб.
     */
    private VMTabExplorerOSGi explorer;
    /**
     * Главное окно приложения.
     */
    private Stage stage;
    private MenuBar menuBar;
    private SplitPane splitPane;

    private void initialize() {
        Thread.currentThread().setName("Главный поток");

        //Инициализация вкладочного explorer'а.
        explorer = new VMTabExplorerOSGi();
        menuBar = new MenuBar();

        splitPane = new SplitPane();
        splitPane.getItems().add(explorer.getContent());
        splitPane.setDividerPositions(1);

        mainPane.setTop(menuBar);
        mainPane.setCenter(splitPane);

        uiRunner = this;
        VMMainFrameImpl.lock.notify();
    }

    @Override
    public void start(final Stage stage) throws InterruptedException {
        synchronized (lock) {
            mainPane = new BorderPane();

            this.stage = stage;
            stage.setMinHeight(650);
            stage.setMinWidth(850);
            stage.setTitle("Единая система КолАЭР");
            stage.setOnCloseRequest(e -> System.exit(0));
            stage.setScene(new Scene(mainPane));
            stage.getIcons().add(new Image("/css/aerIcon.png", true));
            stage.setMaximized(true);

            stage.setFullScreen(false);
            stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
                if (e.getCode() == KeyCode.F11)
                    stage.setFullScreen(true);
            });

            stage.centerOnScreen();
            stage.show();

            initialize();
        }
    }

    public static ApplicationUiRunner getInstance() {
        return uiRunner;
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
    public void show() {
        Tools.runOnThreadFX(() -> {
            stage.show();
            stage.setMaximized(false); //JRE BUG
            stage.setMaximized(true);
        });
    }

    @Override
    public void hide() {
        Tools.runOnThreadFX(stage::hide);
    }

    @Override
    public void setOnMinimize(Function<Boolean, Void> function) {
        stage.iconifiedProperty()
                .addListener((observable, oldValue, newValue) -> function.apply(newValue));
    }

    @Override
    public void setOnExit(Function<Object, Void> function) {
        stage.setOnCloseRequest(function::apply);
    }

    @Override
    public void exit() {
        stage.getOnCloseRequest().handle(null);
    }

    @Override
    public UniformSystemEditorKit initializeUniformSystemEditorKit() {
        Thread.currentThread().setName("Инициализация UniformSystemEditorKit");

        ObjectMapper objectMapper = new ObjectMapper();

        MenuBarUSJavaFx menuBarUS = new MenuBarUSJavaFx(menuBar);
        NotificationJavaFxExceptionHandler notify = new NotificationJavaFxExceptionHandler();
        NetworkUSRestTemplate network = new NetworkUSRestTemplate(objectMapper);
        UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        uiSystemUS.setNotification(notify);
        uiSystemUS.setMenuBarUS(menuBarUS);

        Tools.runOnWithOutThreadFX(() -> splitPane.getItems().add(notify.getContent()));

        Thread.setDefaultUncaughtExceptionHandler(notify);

        AuthenticationOnNetwork authentication = new AuthenticationOnNetwork(objectMapper);
        authentication.registerObserver(menuBarUS);

        UniformSystemEditorKitSingleton editorKit = UniformSystemEditorKitSingleton.getInstance();
        editorKit.setUSNetwork(network);
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(explorer);
        editorKit.setAuthentication(authentication);

        return editorKit;
    }

    @Override
    public VMainFrame getFrame() {
        return this;
    }

    @Override
    public VTabExplorer getExplorer() {
        return explorer;
    }

    @Override
    public TypeUi getTypeUi() {
        return TypeUi.HIGH;
    }

    @Override
    public void run(String[] args) {

    }

    @Override
    public boolean initializeUi() {
        return false;
    }

    @Override
    public void shutdown() {
    }
}