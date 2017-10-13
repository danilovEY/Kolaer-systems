package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.plugins.PluginManager;
import ru.kolaer.client.usa.services.AutoCheckingNotifyMessage;
import ru.kolaer.client.usa.services.AutoUpdatePlugins;
import ru.kolaer.client.usa.services.ServiceControlManager;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.usa.system.network.NetworkUSRestTemplate;
import ru.kolaer.client.usa.system.network.ResponseErrorHandlerNotifications;
import ru.kolaer.client.usa.system.ui.MenuBarUSImpl;
import ru.kolaer.client.usa.system.ui.NotificationPaneExceptionHandler;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;
import ru.kolaer.client.usa.tools.Resources;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Главное окно приложения.
 *
 * @author Danilov
 * @version 0.1
 */
public class VMMainFrameImpl extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
    /**
     * Мапа где ключ и значение соответствует ключам и значениям приложения.
     */
    private static final Map<String, String> PARAM = new HashMap<>();

    /**
     * Панель с контентом главного окна.
     */
    @FXML private BorderPane mainPane;
    /**
     * Менеджер служб.
     */
    private ServiceControlManager servicesManager;
    private VMTabExplorerOSGi explorer;
    private PluginManager pluginManager;
    /**
     * Главное окно приложения.
     */
    private static Stage stage;
    private MenuBar menuBar;
    private SplitPane splitPane;

    @FXML
    public void initialize() {
    	Thread.currentThread().setName("Главный поток");

        //Инициализация вкладочного explorer'а.
        this.explorer = new VMTabExplorerOSGi();
        this.menuBar = new MenuBar();

        this.splitPane = new SplitPane();
        splitPane.getItems().add(explorer.getContent());
        splitPane.setDividerPositions(1);

        this.mainPane.setTop(menuBar);
        this.mainPane.setCenter(splitPane);

        this.servicesManager = new ServiceControlManager();
        this.pluginManager = new PluginManager();

        this.initApplicationParams();

        final ExecutorService mainApplicationThreadPool = Executors.newFixedThreadPool(5);

        final Future<List<PluginBundle>> searchResult = mainApplicationThreadPool
                .submit(this.pluginManager.getSearchPlugins()::search);

        final Future<PluginManager> initializedPluginManager = mainApplicationThreadPool.submit(this::initPluginManager);

        CompletableFuture
                .supplyAsync(this::initUniformSystemEditorKit, mainApplicationThreadPool)
        .thenRunAsync(() -> {
            mainApplicationThreadPool.submit(this::autoLogin);
            mainApplicationThreadPool.submit(this::initTray);
            mainApplicationThreadPool.submit(this::initSystemServices);
            mainApplicationThreadPool.submit(() -> this.installPlugins(initializedPluginManager, searchResult));
            mainApplicationThreadPool.shutdown();
        });
    }

    private void installPlugins(Future<PluginManager> pluginManager, Future<List<PluginBundle>> plugins) {
        Thread.currentThread().setName("Чтение и установка плагинов");

        try {
            final PluginManager initPluginManager = pluginManager.get(30, TimeUnit.SECONDS);

            final List<PluginBundle> pluginBundles = plugins.get(30, TimeUnit.SECONDS);

            final Iterator<PluginBundle> iterPlugins = pluginBundles.iterator();

            while (iterPlugins.hasNext()) {
                PluginBundle pluginBundle = iterPlugins.next();
                if(pluginBundle.getSymbolicNamePlugin().equals("ru.kolaer.asmc")) {
                    this.installPlugin(this.explorer, initPluginManager, pluginBundle);
                    iterPlugins.remove();
                    break;
                }
            }

            pluginBundles.forEach(pluginBundle -> this.installPluginInThread(this.explorer, initPluginManager, pluginBundle));
            pluginBundles.clear();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.error("Ошибка при инициализации и чтении плагинов!", e);
            System.exit(-9);
        }
    }

    private PluginManager initPluginManager() {
        Thread.currentThread().setName("Инициализация менеджера плагинов");
        try {
            pluginManager.initialization();
            return pluginManager;
        } catch (final Exception e) {
            LOG.error("Ошибка при инициализации менеджера плагинов!", e);
            throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
        }
    }

    private void initSystemServices() {
        Thread.currentThread().setName("Добавление системны служб");

        this.servicesManager.addService(new AutoUpdatePlugins(pluginManager, explorer, this.servicesManager), true);
        this.servicesManager.addService(new AutoCheckingNotifyMessage(), true);
    }

    private void initTray() {
        Thread.currentThread().setName("Создание инонки для трея");

        new Tray().createTrayIcon(stage, this.pluginManager, this.servicesManager, this.explorer);
    }

    private boolean autoLogin() {
        Thread.currentThread().setName("Добавление системны служб");

        return UniformSystemEditorKitSingleton.getInstance().getAuthentication().loginIsRemember();
    }

    private UniformSystemEditorKit initUniformSystemEditorKit() {
        Thread.currentThread().setName("Инициализация UniformSystemEditorKit");

        final MenuBarUSImpl menuBarUS = new MenuBarUSImpl(menuBar);
        final NotificationPaneExceptionHandler notify = new NotificationPaneExceptionHandler();
        final NetworkUSRestTemplate network = new NetworkUSRestTemplate(new ResponseErrorHandlerNotifications());
        final UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        uiSystemUS.setNotification(notify);
        uiSystemUS.setMenuBarUS(menuBarUS);

        Tools.runOnWithOutThreadFX(() ->
            this.splitPane.getItems().add(notify.getContent())
        );

        Thread.setDefaultUncaughtExceptionHandler(notify);

        final AuthenticationOnNetwork authentication = new AuthenticationOnNetwork();
        authentication.registerObserver(menuBarUS);

        final UniformSystemEditorKitSingleton editorKit = UniformSystemEditorKitSingleton.getInstance();
        editorKit.setUSNetwork(network);
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(explorer);
        editorKit.setAuthentication(authentication);

        return editorKit;
    }

    private void installPluginInThread(final VMTabExplorerOSGi explorer, final PluginManager pluginManager, final PluginBundle pluginBundle) {
        final ExecutorService threadInstallPlugin = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            installPlugin(explorer, pluginManager, pluginBundle);

            threadInstallPlugin.shutdown();
        }, threadInstallPlugin).exceptionally(ex -> {
            LOG.error("Ошибка при установки прагина: {}", pluginBundle.getNamePlugin(), ex);
            return null;
        });
    }

    private void installPlugin(final VMTabExplorerOSGi explorer, final PluginManager pluginManager, final PluginBundle pluginBundle) {
        Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
        LOG.info("{}: Установка плагина.", pluginBundle.getPathPlugin());

        if (pluginManager.install(pluginBundle)) {
            LOG.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
            final String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
            explorer.addTabPlugin(tabName, pluginBundle);

            LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
            final Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
            if (pluginServices != null) {
                pluginServices.forEach(this.servicesManager::addService);
            }
        }
    }
    private void initApplicationParams() {
        final String pathServerRest = PARAM.get("server-rest");
        if (pathServerRest != null) {
            Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append(pathServerRest);
            LOG.info("RESTful server: {}", Resources.URL_TO_KOLAER_RESTFUL.toString());
        }

        final String pathServerWeb = PARAM.get("server-web");
        if (pathServerWeb != null) {
            Resources.URL_TO_KOLAER_WEB.delete(0, Resources.URL_TO_KOLAER_WEB.length()).append(pathServerWeb);
            LOG.info("Web server: {}", Resources.URL_TO_KOLAER_WEB.toString());
        }

        final String service = PARAM.get("service");
        if (service == null || !service.equals("false")) {
            this.servicesManager.setAutoRun(true);
            LOG.info("Service ON");
        }
        PARAM.clear();
    }

    @Override
    public void start(final Stage stage) throws InterruptedException {
    	VMMainFrameImpl.stage = stage;
        stage.setMinHeight(650);
        stage.setMinWidth(850);
        stage.setTitle("Единая система КолАЭР");
        stage.setOnCloseRequest(event -> System.exit(0));

        PARAM.putAll(this.getParameters().getNamed());

        Tools.runOnThreadFX(() -> {
            try {
                stage.setScene(new Scene(FXMLLoader.load(Resources.V_MAIN_FRAME)));
                stage.getIcons().add(new Image("/css/aerIcon.png"));
                stage.setMaximized(true);
            } catch (IOException e) {
                LOG.error("Не удалось загрузить: " + Resources.V_MAIN_FRAME, e);
                System.exit(-9);
            }
        });


        stage.setFullScreen(false);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
            if (e.getCode() == KeyCode.F11)
                stage.setFullScreen(true);
        });



        stage.centerOnScreen();
        stage.show();
    }


}