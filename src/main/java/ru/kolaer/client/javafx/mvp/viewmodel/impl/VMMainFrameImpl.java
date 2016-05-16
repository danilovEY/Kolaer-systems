package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.plugins.SearchPlugins;
import ru.kolaer.client.javafx.services.*;
import ru.kolaer.client.javafx.system.NetworkUSImpl;
import ru.kolaer.client.javafx.system.UISystemUSImpl;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.javafx.tools.Resources;

import java.io.IOException;
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
        final VMTabExplorerOSGi explorer = new VMTabExplorerOSGi();

        final ExecutorService threadOnCreateTray = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            new Tray().createTrayIcon(stage, this.servicesManager, explorer);
        }, threadOnCreateTray);

        final UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        final NetworkUSImpl network = new NetworkUSImpl();
        final UniformSystemEditorKitSingleton editorKit = UniformSystemEditorKitSingleton.getInstance();
        editorKit.setUSNetwork(network);
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(explorer);

        this.mainPane.setCenter(explorer.getContent());

        final ExecutorService threadStartService = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Добавление системны служб");
            this.servicesManager.addService(new HideShowMainStage(stage), true);
            this.servicesManager.addService(new UserPingService(network.getService().path("system")), true);
            this.servicesManager.addService(new ServiceRemoteActivOrDeactivPlugin(explorer, network.getService().path("system")), true);
            this.servicesManager.addService(new ServiceUserIpAndHostName(network.getService().path("system")), true);
            threadStartService.shutdown();
        }, threadStartService);

        final SearchPlugins searchPlugins = new SearchPlugins();
        final PluginManager pluginManager = new PluginManager(searchPlugins);

        final ExecutorService threadScan = Executors.newSingleThreadExecutor();
        CompletableFuture<List<PluginBundle>> resultSearch = CompletableFuture.supplyAsync(() -> {
            return searchPlugins.getPlugins();
        }, threadScan);

        final ExecutorService threadInstall = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Инициализация менеджера плагинов");

            try {
                pluginManager.initialization();
            } catch (final Exception e) {
                LOG.error("Ошибка при инициализации менеджера плагинов!", e);
                throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
            }

            List<PluginBundle> plugins = null;
            try {
                plugins = resultSearch.get();
            } catch (Exception e) {
                LOG.error("Ошибка при получении плагинов!", e);
                plugins = Collections.emptyList();
            }

            plugins.parallelStream().forEach(pluginBundle -> {
                try {
                    Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
                    LOG.info("{}: Установка плагина.", pluginBundle.getPathPlugin());
                    pluginManager.install(pluginBundle);
                } catch (final BundleException e) {
                    LOG.error("Ошибка при установке/запуска плагина: {}", pluginBundle.getSymbolicNamePlugin(), e);
                    try {
                        pluginManager.uninstall(pluginBundle);
                    } catch (final BundleException e1) {
                        LOG.error("Ошибка при удалении плагина: {}", pluginBundle.getSymbolicNamePlugin(), e1);
                    }
                    return;
                }

                LOG.info("{}: Получение USP...", pluginBundle.getSymbolicNamePlugin());
                final UniformSystemPlugin uniformSystemPlugin = pluginBundle.getUniformSystemPlugin();

                if(uniformSystemPlugin == null) {
                    LOG.info("{}: USP is null!", pluginBundle.getSymbolicNamePlugin());
                    try {
                        pluginManager.uninstall(pluginBundle);
                    } catch (final BundleException e1) {
                        LOG.error("Ошибка при удалении плагина: {}", pluginBundle.getSymbolicNamePlugin(), e1);
                    }
                    return;
                }

                try {
                    LOG.info("{}: Инициализация USP...", pluginBundle.getSymbolicNamePlugin());
                    uniformSystemPlugin.initialization(UniformSystemEditorKitSingleton.getInstance());
                } catch (final Exception e) {
                    LOG.error("Ошибка при инициализации плагина: {}", pluginBundle.getSymbolicNamePlugin(), e);
                    try {
                        pluginManager.uninstall(pluginBundle);
                    } catch (final BundleException e1) {
                        LOG.error("Ошибка при удалении плагина: {}", pluginBundle.getSymbolicNamePlugin(), e1);
                    }
                    return;
                }

                LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
                final Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
                if (pluginServices != null) {
                    pluginServices.parallelStream().forEach(this.servicesManager::addService);
                }

                final String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
                explorer.addTabPlugin(tabName, pluginBundle);
        	});

            plugins.clear();
            threadInstall.shutdown();
        }, threadInstall).exceptionally(t -> {
            LOG.error("Ошибка при инициализации менеджера плагинов!", t);
            threadInstall.shutdownNow();
            return null;
        });
    }

    private void initApplicationParams() {
        final String pathServer = PARAM.get("server");
        if (pathServer != null) {
            Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append(pathServer);
        }

        final String service = PARAM.get("service");
        if (service == null || !service.equals("false")) {
            this.servicesManager.setAutoRun(true);
            this.servicesManager.runAllServices();
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