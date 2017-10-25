package ru.kolaer.client.usa.mvp.viewmodel.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
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
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.plugins.PluginManager;
import ru.kolaer.client.usa.services.AutoCheckingNotifyMessage;
import ru.kolaer.client.usa.services.AutoUpdatePlugins;
import ru.kolaer.client.usa.services.HideShowMainStage;
import ru.kolaer.client.usa.services.ServiceManager;
import ru.kolaer.client.usa.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.usa.system.network.NetworkUSRestTemplate;
import ru.kolaer.client.usa.system.ui.MenuBarUSImpl;
import ru.kolaer.client.usa.system.ui.NotificationPanelExceptionHandler;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;
import ru.kolaer.client.usa.tools.Resources;

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
    private BorderPane mainPane;

    /**
     * Менеджер служб.
     */
    private ServiceManager servicesManager;
    private VMTabExplorerOSGi explorer;
    private PluginManager pluginManager;
    /**
     * Главное окно приложения.
     */
    private static Stage stage;
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

        servicesManager = new ServiceManager();
        pluginManager = new PluginManager();

        initApplicationParams();

        ExecutorService mainApplicationThreadPool = Executors.newFixedThreadPool(5);

        Future<List<PluginBundle>> searchResult = mainApplicationThreadPool
                .submit(pluginManager.getSearchPlugins()::search);

        Future<PluginManager> initializedPluginManager = mainApplicationThreadPool.submit(this::initPluginManager);

        CompletableFuture
                .supplyAsync(this::initUniformSystemEditorKit, mainApplicationThreadPool)
        .thenRunAsync(() -> {
            mainApplicationThreadPool.submit(this::autoLogin);
            mainApplicationThreadPool.submit(this::initTray);
            mainApplicationThreadPool.submit(this::initSystemServices);
            mainApplicationThreadPool.submit(this::initShutdownApplication);
            mainApplicationThreadPool.submit(() -> installPlugins(initializedPluginManager, searchResult));
            mainApplicationThreadPool.shutdown();
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private void installPlugins(Future<PluginManager> pluginManager, Future<List<PluginBundle>> plugins) {
        Thread.currentThread().setName("Чтение и установка плагинов");

        try {
            PluginManager initPluginManager = pluginManager.get(30, TimeUnit.SECONDS);
            List<PluginBundle> pluginBundles = plugins.get(30, TimeUnit.SECONDS);

            Iterator<PluginBundle> iterPlugins = pluginBundles.iterator();

            while (iterPlugins.hasNext()) {
                PluginBundle pluginBundle = iterPlugins.next();
                if(pluginBundle.getSymbolicNamePlugin().equals("ru.kolaer.asmc")) {
                    installPlugin(explorer, initPluginManager, pluginBundle);
                    iterPlugins.remove();
                    break;
                }
            }

            pluginBundles.forEach(pluginBundle -> installPluginInThread(explorer, initPluginManager, pluginBundle));
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
        } catch (Exception e) {
            LOG.error("Ошибка при инициализации менеджера плагинов!", e);
            throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
        }
    }

    private void initSystemServices() {
        Thread.currentThread().setName("Добавление системны служб");

        servicesManager.addService(new AutoUpdatePlugins(pluginManager, explorer, servicesManager), true);
        servicesManager.addService(new AutoCheckingNotifyMessage(), true);
        servicesManager.addService(new HideShowMainStage(stage), true);
    }

    private void initTray() {
        Thread.currentThread().setName("Создание инонки для трея");

        new Tray().initTrayIcon(stage);
    }

    private void initShutdownApplication() {
        stage.setOnCloseRequest(event -> {
            ExecutorService serviceThread = Executors.newSingleThreadExecutor();
            CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("Завершение приложения");
                LOG.info("Завершение служб...");
                servicesManager.removeAllServices();
            }, serviceThread).exceptionally(t -> {
                LOG.error("Ошибка при завершении всех активных служб!", t);
                return null;
            }).thenAccept(aVoid -> {
                LOG.info("Завершение вкладок...");
                explorer.removeAll();
            }).exceptionally(t -> {
                LOG.error("Ошибка при завершении всех активных плагинов!", t);
                return null;
            }).thenAccept(aVoid -> {
                try {
                    pluginManager.shutdown();
                } catch (InterruptedException e1) {
                    LOG.error("Ошибка при закрытии OSGi!", e1);
                }
            }).thenAccept(aVoid -> {
                Tools.runOnWithOutThreadFX(() -> {
                    LOG.info("Завершение JavaFX...");
                    stage.close();
                    Platform.exit();
                    System.exit(0);
                });
                LOG.info("Завершение приложения...");
            }).exceptionally(t -> {
                LOG.error("Ошибка при завершении всего приложения!", t);
                System.exit(-9);
                return null;
            });
        });
    }

    private boolean autoLogin() {
        Thread.currentThread().setName("Добавление системны служб");

        return UniformSystemEditorKitSingleton.getInstance().getAuthentication().loginIsRemember();
    }

    private UniformSystemEditorKit initUniformSystemEditorKit() {
        Thread.currentThread().setName("Инициализация UniformSystemEditorKit");

        ObjectMapper objectMapper = new ObjectMapper();

        MenuBarUSImpl menuBarUS = new MenuBarUSImpl(menuBar);
        NotificationPanelExceptionHandler notify = new NotificationPanelExceptionHandler();
        NetworkUSRestTemplate network = new NetworkUSRestTemplate(objectMapper);
        UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        uiSystemUS.setNotification(notify);
        uiSystemUS.setMenuBarUS(menuBarUS);

        Tools.runOnWithOutThreadFX(() ->
            splitPane.getItems().add(notify.getContent())
        );

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

    private void installPluginInThread(VMTabExplorerOSGi explorer, PluginManager pluginManager, PluginBundle pluginBundle) {
        ExecutorService threadInstallPlugin = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            installPlugin(explorer, pluginManager, pluginBundle);

            threadInstallPlugin.shutdown();
        }, threadInstallPlugin).exceptionally(ex -> {
            LOG.error("Ошибка при установки прагина: {}", pluginBundle.getNamePlugin(), ex);
            return null;
        });
    }

    private void installPlugin(VMTabExplorerOSGi explorer, PluginManager pluginManager, PluginBundle pluginBundle) {
        Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
        LOG.info("{}: Установка плагина.", pluginBundle.getPathPlugin());

        if (pluginManager.install(pluginBundle)) {
            LOG.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
            String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
            explorer.addTabPlugin(tabName, pluginBundle);

            LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
            Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
            if (pluginServices != null) {
                pluginServices.forEach(servicesManager::addService);
            }
        }
    }
    private void initApplicationParams() {
        String pathServerRest = PARAM.get(Resources.PRIVATE_SERVER_URL_PARAM);
        if (pathServerRest != null) {
            Resources.URL_TO_PRIVATE_SERVER.delete(0, Resources.URL_TO_PRIVATE_SERVER.length()).append(pathServerRest);
            LOG.info("RESTful server: {}", Resources.URL_TO_PRIVATE_SERVER.toString());
        }

        String pathServerWeb = PARAM.get(Resources.PUBLIC_SERVER_URL_PARAM);
        if (pathServerWeb != null) {
            Resources.URL_TO_PRIVATE_SERVER.delete(0, Resources.URL_TO_PRIVATE_SERVER.length()).append(pathServerWeb);
            LOG.info("Web server: {}", Resources.URL_TO_PRIVATE_SERVER.toString());
        }

        String service = PARAM.get(Resources.SERVICE_PARAM);
        if (service == null || !service.equals("false")) {
            servicesManager.setAutoRun(true);
            LOG.info("Service ON");
        }

        pluginManager.setUniqueCacheDir(PARAM.getOrDefault(Resources.RAND_DIR_CACHE_PARAM, "false").equals("true"));

    }

    @Override
    public void start(Stage stage) throws InterruptedException {
        mainPane = new BorderPane();

    	VMMainFrameImpl.stage = stage;
        stage.setMinHeight(650);
        stage.setMinWidth(850);
        stage.setTitle("Единая система КолАЭР");
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        PARAM.putAll(getParameters().getNamed());

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