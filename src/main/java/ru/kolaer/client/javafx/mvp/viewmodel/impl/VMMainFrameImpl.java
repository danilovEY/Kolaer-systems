package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.services.*;
import ru.kolaer.client.javafx.system.UISystemUSImpl;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.javafx.tools.Resources;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Stage stage;

    @FXML
    public void initialize() {
    	Thread.currentThread().setName("Главный поток");
    	
        this.servicesManager = new ServiceControlManager();
        this.initApplicationParams();
        
        //Статус бар приложения.
        final HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(0, 30, 0, 30));
        statusBar.setSpacing(30);
        statusBar.setAlignment(Pos.CENTER_RIGHT);
        statusBar.setStyle("-fx-background-color: #66CCFF");

        //Инициализация вкладочного explorer'а.
        final VMTabExplorerOSGi explorer = new VMTabExplorerOSGi();

        final UISystemUSImpl uiSystemUS = new UISystemUSImpl();

        final UniformSystemEditorKitSingleton editorKit = UniformSystemEditorKitSingleton.getInstance();
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(explorer);

        this.mainPane.setBottom(statusBar);
        this.mainPane.setCenter(explorer.getContent());

        final ExecutorService threadStartService = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Добавление системны служб");
            this.servicesManager.addService(new UserPingService(), true);
            this.servicesManager.addService(new ServiceRemoteActivOrDeactivPlugin(explorer, editorKit), true);
            threadStartService.shutdown();
        }, threadStartService);

        final ExecutorService threadScan = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Инициализация менеджера плагинов");
            final PluginManager pluginManager = new PluginManager();

            try {
                pluginManager.initialization();
            } catch (final Exception e) {
                LOG.error("Ошибка при инициализации менеджера плагинов!", e);
                throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
            }
            
            final List<PluginBundle> plugins = pluginManager.getSearchPlugins().search();
            
            for (final PluginBundle pluginBundle : plugins) {
                final ExecutorService initPluginThread = Executors.newSingleThreadExecutor();
                CompletableFuture.supplyAsync(() -> {
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
                        initPluginThread.shutdownNow();
                        return null;
                    }
                    return pluginBundle;
                }, initPluginThread).thenApplyAsync(plugin -> {
                	if(plugin != null) {
                    	LOG.info("{}: Получение USP...", plugin.getSymbolicNamePlugin());
                        final UniformSystemPlugin uniformSystemPlugin = pluginBundle.getUniformSystemPlugin();
                        if(uniformSystemPlugin == null) {
                        	LOG.info("{}: USP is null!", plugin.getSymbolicNamePlugin());
                        	initPluginThread.shutdownNow();
                        }
                        try {
                        	LOG.info("{}: Инициализация USP...", plugin.getSymbolicNamePlugin());
                            uniformSystemPlugin.initialization(UniformSystemEditorKitSingleton.getInstance());
                        } catch (final Exception e) {
                            LOG.error("Ошибка при инициализации плагина: {}", pluginBundle.getSymbolicNamePlugin(), e);
                            try {
                                pluginManager.uninstall(pluginBundle);
                            } catch (final BundleException e1) {
                                LOG.error("Ошибка при удалении плагина: {}", pluginBundle.getSymbolicNamePlugin(), e1);
                            }
                            initPluginThread.shutdownNow();
                        }
                	}
                    return plugin;
                }, initPluginThread).thenAcceptAsync(plugin -> {
                	if(plugin != null) {
                    	LOG.info("{}: Получение служб...", plugin.getSymbolicNamePlugin());
                        final Collection<Service> pluginServices = plugin.getUniformSystemPlugin().getServices();
                        if (pluginServices != null) {
                            pluginServices.parallelStream().forEach(this.servicesManager::addService);
                            pluginServices.clear();
                        }
                        
                        final String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
                        explorer.addTabPlugin(tabName, plugin);
                	}
                    initPluginThread.shutdown();
                }, initPluginThread).exceptionally(t -> {
                	LOG.error("Ошибка!", t);
                	initPluginThread.shutdownNow();
                    return null;
                });
            }
            
            plugins.clear();
            threadScan.shutdown();
        }, threadScan).exceptionally(t -> {
            LOG.error("Ошибка при инициализации менеджера плагинов!", t);
            threadScan.shutdownNow();
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
            this.servicesManager.addService(new SeviceUserIpAndHostName());
            this.servicesManager.addService(new ServiceScreen());
            this.servicesManager.addService(new UserWindowsKeyListenerService());
            this.servicesManager.runAllServices();
        }
        PARAM.clear();
    }

    @Override
    public void start(final Stage stage) {
        this.stage = stage;
        this.stage.setMinHeight(650);
        this.stage.setMinWidth(850);

        PARAM.putAll(this.getParameters().getNamed());

        Tools.runOnThreadFX(() -> {
            try {
                this.stage.setScene(new Scene(FXMLLoader.load(Resources.V_MAIN_FRAME)));
                this.stage.setMaximized(true);
            } catch (IOException e) {
                LOG.error("Не удалось загрузить: " + Resources.V_MAIN_FRAME, e);
                System.exit(-9);
            }
        });

        this.stage.getIcons().add(new Image("/css/aerIcon.png"));
        this.stage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        this.stage.setFullScreen(false);
        this.stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
            if (e.getCode() == KeyCode.F11)
                this.stage.setFullScreen(true);
        });
        this.stage.setTitle("Единая система КолАЭР");

        this.stage.centerOnScreen();
        this.stage.show();
    }
}