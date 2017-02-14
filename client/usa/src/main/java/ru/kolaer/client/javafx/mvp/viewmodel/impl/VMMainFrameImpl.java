package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.application.Application;
import javafx.application.Platform;
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
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.ui.MenuBarUS;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.plugins.SearchPlugins;
import ru.kolaer.client.javafx.services.AutoCeckingNotifyMessage;
import ru.kolaer.client.javafx.services.AutoUpdatePlugins;
import ru.kolaer.client.javafx.services.HideShowMainStage;
import ru.kolaer.client.javafx.services.ServiceControlManager;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.javafx.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.javafx.system.network.NetworkUSImpl;
import ru.kolaer.client.javafx.system.ui.MenuBarUSImpl;
import ru.kolaer.client.javafx.system.ui.NotificationPane;
import ru.kolaer.client.javafx.system.ui.UISystemUSImpl;
import ru.kolaer.client.javafx.tools.Resources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @FXML
    public void initialize() {
    	Thread.currentThread().setName("Главный поток");
    	
        this.servicesManager = new ServiceControlManager();
        this.initApplicationParams();       

        //Инициализация вкладочного explorer'а.
        this.explorer = new VMTabExplorerOSGi();
        final SplitPane splitPane = new SplitPane();
        splitPane.getItems().add(explorer.getContent());
        splitPane.setDividerPositions(1);
        this.mainPane.setCenter(splitPane);

        final MenuBar menuBar = new MenuBar();
        this.mainPane.setTop(menuBar);

        final MenuBarUSImpl menuBarUS = new MenuBarUSImpl(menuBar);
        final NotificationPane notify = new NotificationPane();
        final NetworkUSImpl network = new NetworkUSImpl();
        final UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        uiSystemUS.setNotification(notify);
        splitPane.getItems().add(notify.getContent());


        final AuthenticationOnNetwork authentication = new AuthenticationOnNetwork();
        authentication.registerObserver(menuBarUS);

        final UniformSystemEditorKitSingleton editorKit = UniformSystemEditorKitSingleton.getInstance();
        editorKit.setUSNetwork(network);
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(explorer);
        editorKit.setAuthentication(authentication);

        authentication.loginIsRemember();

        final SearchPlugins searchPlugins = new SearchPlugins();
        this.pluginManager = new PluginManager(searchPlugins);

        final ExecutorService threadOnCreateTray = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            new Tray().createTrayIcon(stage, pluginManager, this.servicesManager, explorer);
            threadOnCreateTray.shutdown();
        }, threadOnCreateTray);

        final ExecutorService threadStartService = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Добавление системны служб");
            this.servicesManager.addService(new AutoUpdatePlugins(pluginManager, explorer, this.servicesManager), true);
            this.servicesManager.addService(new AutoCeckingNotifyMessage(), true);
            threadStartService.shutdown();
        }, threadStartService);

        final ExecutorService threadScan = Executors.newSingleThreadExecutor();
        CompletableFuture<List<PluginBundle>> resultSearch = CompletableFuture.supplyAsync(searchPlugins::search, threadScan);

        final ExecutorService threadInstall = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Инициализация менеджера плагинов");

            try {
                pluginManager.initialization();
            } catch (final Exception e) {
                LOG.error("Ошибка при инициализации менеджера плагинов!", e);
                throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
            }

            List<PluginBundle> plugins = Collections.emptyList();
            try {
                plugins = resultSearch.get();
            } catch (Exception e) {
                LOG.error("Ошибка при получении плагинов!", e);
            }

            final Iterator<PluginBundle> iterPlugins = plugins.iterator();

            while (iterPlugins.hasNext()) {
                PluginBundle pluginBundle = iterPlugins.next();
                if(pluginBundle.getSymbolicNamePlugin().equals("ru.kolaer.asmc")) {
                    this.installPlugin(explorer,pluginManager,pluginBundle);
                    iterPlugins.remove();
                    break;
                }
            }

            plugins.forEach(pluginBundle -> this.installPluginInThread(explorer,pluginManager,pluginBundle));
            plugins.clear();

            threadInstall.shutdown();
        }, threadInstall).exceptionally(t -> {
            LOG.error("Ошибка при инициализации менеджера плагинов!", t);
            threadInstall.shutdownNow();
            return null;
        });
    }

    public void installPluginInThread(final VMTabExplorerOSGi explorer, final PluginManager pluginManager, final PluginBundle pluginBundle) {
        final ExecutorService threadInstallPlugin = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            installPlugin(explorer, pluginManager, pluginBundle);

            threadInstallPlugin.shutdown();
        }, threadInstallPlugin);
    }

    public void installPlugin(final VMTabExplorerOSGi explorer, final PluginManager pluginManager, final PluginBundle pluginBundle) {
        Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
        LOG.info("{}: Установка плагина.", pluginBundle.getPathPlugin());

        if (pluginManager.install(pluginBundle)) {
            LOG.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
            final String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
            this.explorer.addTabPlugin(tabName, pluginBundle);

            LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
            final Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
            if (pluginServices != null) {
                pluginServices.parallelStream().forEach(this.servicesManager::addService);
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
            this.servicesManager.runAllServices();
            LOG.info("Service ON");
        }
        PARAM.clear();
    }

    @Override
    public void start(final Stage stage) throws InterruptedException {
    	VMMainFrameImpl.stage = stage;

        stage.setMinHeight(650);
        stage.setMinWidth(850);

        PARAM.putAll(this.getParameters().getNamed());

        Tools.runOnThreadFX(() -> {
            try {
                stage.setScene(new Scene(FXMLLoader.load(Resources.V_MAIN_FRAME)));
                stage.setMaximized(true);
            } catch (IOException e) {
                LOG.error("Не удалось загрузить: " + Resources.V_MAIN_FRAME, e);
                System.exit(-9);
            }
        });

        Tools.runOnThreadFX(() -> {
            stage.getIcons().add(new Image("/css/aerIcon.png"));
        });

        stage.setFullScreen(false);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
            if (e.getCode() == KeyCode.F11)
                stage.setFullScreen(true);
        });

        stage.setTitle("Единая система КолАЭР");

        stage.centerOnScreen();
        stage.show();
    }


}