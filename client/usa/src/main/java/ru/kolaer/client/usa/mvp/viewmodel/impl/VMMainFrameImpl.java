package ru.kolaer.client.usa.mvp.viewmodel.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.impl.DefaultStaticUS;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.plugins.PluginManager;
import ru.kolaer.client.usa.plugins.info.InfoPaneBundle;
import ru.kolaer.client.usa.plugins.info.InfoPanePlugin;
import ru.kolaer.client.usa.services.HideShowMainStage;
import ru.kolaer.client.usa.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.usa.system.network.NetworkUSRestTemplate;
import ru.kolaer.client.usa.system.ui.MenuBarUSImpl;
import ru.kolaer.client.usa.system.ui.NotificationPopupControlFx;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;
import ru.kolaer.client.usa.tools.Resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Главное окно приложения.
 *
 * @author Danilov
 * @version 0.1
 */
@Slf4j
public class VMMainFrameImpl extends Application implements AuthenticationObserver {
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

    private void initialize() {
    	Thread.currentThread().setName("Главный поток");

        servicesManager = new ServiceManager();
        pluginManager = new PluginManager();

        //Инициализация вкладочного explorer'а.
        explorer = new VMTabExplorerOSGi();
        explorer.initView(initExplorer -> mainPane.setCenter(explorer.getContent()));

        initApplicationParams();

        ExecutorService mainApplicationThreadPool = Executors.newFixedThreadPool(5);

        Future<PluginManager> initializedPluginManager = mainApplicationThreadPool.submit(this::initPluginManager);

        CompletableFuture
                .supplyAsync(this::initUniformSystemEditorKit, mainApplicationThreadPool)
                .thenRunAsync(this::autoLogin, mainApplicationThreadPool)
                .thenRunAsync(this::initTray, mainApplicationThreadPool)
                .thenRunAsync(this::initShutdownApplication, mainApplicationThreadPool)
                .thenRunAsync(this::initSystemServices, mainApplicationThreadPool)
                .thenRunAsync(() -> installPlugins(initializedPluginManager), mainApplicationThreadPool)
                .exceptionally(throwable -> {
                    log.error("Ошибка", throwable);
                    return null;
                });
    }

    private void installPlugins(Future<PluginManager> pluginManager) {
        Thread.currentThread().setName("Чтение и установка плагинов");

        try {
            PluginManager initPluginManager = pluginManager.get(3, TimeUnit.MINUTES);

            this.initSystemPlugins();

//            Iterator<PluginBundle> iterPlugins = pluginBundles.iterator();
//
//            while (iterPlugins.hasNext()) {
//                PluginBundle pluginBundle = iterPlugins.next();
//                if(pluginBundle.getSymbolicNamePlugin().contains("ru.kolaer.asmc")) {
//                    installPlugin(explorer, initPluginManager, pluginBundle);
//                    iterPlugins.remove();
//                    break;
//                }
//            }

            ExecutorService executorService = Executors.newCachedThreadPool();

            initPluginManager
                    .getSearchPlugins()
                    .search(pluginBundle -> installPluginInThread(executorService, explorer, initPluginManager, pluginBundle));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Ошибка при инициализации и чтении плагинов!", e);
            System.exit(-9);
        }
    }

    private void installPluginInThread(ExecutorService executorService, VMTabExplorerOSGi explorer, PluginManager pluginManager, PluginBundle pluginBundle) {
        CompletableFuture
                .runAsync(() -> installPlugin(explorer, pluginManager, pluginBundle), executorService)
                .exceptionally(ex -> {
                    log.error("Ошибка при установки прагина: {}", pluginBundle.getNamePlugin(), ex);
                    return null;
                });
    }

    private void installPlugin(VMTabExplorerOSGi explorer, PluginManager pluginManager, PluginBundle pluginBundle) {
        Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
        log.info("{}: Установка плагина.", pluginBundle.getPathPlugin());

        if (pluginManager.install(pluginBundle)) {
            log.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
            String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
            explorer.addTabPlugin(tabName, pluginBundle);

            log.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
            Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
            if (pluginServices != null) {
                pluginServices.forEach(servicesManager::addService);
            }
        } else {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify("Не удалось установить плагин: " + pluginBundle.getNamePlugin(),
                            "Нужно удалить папку 'KolaerCahe'");
        }
    }

    private PluginManager initPluginManager() {
        Thread.currentThread().setName("Инициализация менеджера плагинов");
        try {
            pluginManager.initialization();
            return pluginManager;
        } catch (Exception e) {
            log.error("Ошибка при инициализации менеджера плагинов!", e);
            throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
        }
    }

    private void initSystemPlugins() {
        Thread.currentThread().setName("Добавление системных плагинов");

        installPluginInThread(Executors.newSingleThreadExecutor(), explorer, pluginManager,  new InfoPaneBundle(new InfoPanePlugin()));
    }

    private void initSystemServices() {
        Thread.currentThread().setName("Добавление системных служб");

//        servicesManager.addService(new AutoUpdatePlugins(pluginManager, explorer, servicesManager), true);
        servicesManager.addService(new HideShowMainStage(stage), true);
    }

    private void initTray() {
        Thread.currentThread().setName("Создание инонки для трея");

        new Tray().initTrayIcon(stage);
    }

    private void initShutdownApplication() {
        stage.setOnCloseRequest(event -> {
            ExecutorService serviceThread = Executors.newSingleThreadExecutor();
            CompletableFuture<Void> shutdownThread = CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("Завершение приложения");
                log.info("Завершение служб...");
                servicesManager.removeAllServices();
            }, serviceThread).exceptionally(t -> {
                log.error("Ошибка при завершении всех активных служб!", t);
                return null;
            }).thenAccept(aVoid -> {
                log.info("Завершение вкладок...");
                explorer.removeAll();
            }).exceptionally(t -> {
                log.error("Ошибка при завершении всех активных плагинов!", t);
                return null;
            }).thenAccept(aVoid -> {
                try {
                    pluginManager.shutdown();
                } catch (InterruptedException e1) {
                    log.error("Ошибка при закрытии OSGi!", e1);
                }
            }).thenAccept(aVoid -> {
                Tools.runOnWithOutThreadFX(() -> {
                    log.info("Завершение JavaFX...");
                    stage.close();
                    Platform.exit();
                    System.exit(0);
                });
                log.info("Завершение приложения...");
            }).exceptionally(t -> {
                log.error("Ошибка при завершении всего приложения!", t);
                System.exit(-9);
                return null;
            });

            serviceThread.shutdown();

            try {
                shutdownThread.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Ошибка при ожидании завершения!", e);
            }

            System.exit(0);
        });
    }

    private boolean autoLogin() {
        Thread.currentThread().setName("Добавление системны служб");

        return UniformSystemEditorKitSingleton.getInstance().getAuthentication().loginIsRemember();
    }

    private UniformSystemEditorKit initUniformSystemEditorKit() {
        Thread.currentThread().setName("Инициализация UniformSystemEditorKit");

        ObjectMapper objectMapper = new ObjectMapper();
        MenuBarUSImpl menuBarUS = new MenuBarUSImpl();

        NetworkUSRestTemplate network = new NetworkUSRestTemplate(objectMapper);
        UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        AuthenticationOnNetwork authentication = new AuthenticationOnNetwork(network.getGlobalRestTemplate(), objectMapper);

        uiSystemUS.setMenuBarUS(menuBarUS);
        uiSystemUS.setStaticUS(new DefaultStaticUS());
        uiSystemUS.setNotification(new NotificationPopupControlFx());
        uiSystemUS.setMainStage(stage);

        authentication.registerObserver(menuBarUS);
        authentication.registerObserver(this);

        Tools.runOnWithOutThreadFX(() ->  {
            menuBarUS.initView(initMenuBar -> mainPane.setTop(initMenuBar.getContent()));
        });

        UniformSystemEditorKitSingleton editorKit = new UniformSystemEditorKitSingleton();
        editorKit.setUSNetwork(network);
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(explorer);
        editorKit.setAuthentication(authentication);

        UniformSystemEditorKitSingleton.setInstance(editorKit);

        return editorKit;
    }
    private void initApplicationParams() {
        String pathServerRest = PARAM.get(Resources.PRIVATE_SERVER_URL_PARAM);
        if (pathServerRest != null) {
            Resources.URL_TO_PRIVATE_SERVER.delete(0, Resources.URL_TO_PRIVATE_SERVER.length()).append(pathServerRest);
            log.info("Private server: {}", Resources.URL_TO_PRIVATE_SERVER.toString());
        }

        String pathServerWeb = PARAM.get(Resources.PUBLIC_SERVER_URL_PARAM);
        if (pathServerWeb != null) {
            Resources.URL_TO_PUBLIC_SERVER.delete(0, Resources.URL_TO_PUBLIC_SERVER.length()).append(pathServerWeb);
            log.info("Public server: {}", Resources.URL_TO_PUBLIC_SERVER.toString());
        }

        String service = PARAM.get(Resources.SERVICE_PARAM);
        if (service == null || !service.equals("false")) {
            servicesManager.setAutoRun(true);
            log.info("Service ON");
        }

        pluginManager.setUniqueCacheDir(PARAM.getOrDefault(Resources.RAND_DIR_CACHE_PARAM, "false").equals("true"));
    }

    @Override
    public void start(Stage stage) throws InterruptedException {
        mainPane = new BorderPane();

    	VMMainFrameImpl.stage = stage;
        stage.setMinHeight(650);
        stage.setMinWidth(850);
        stage.setTitle("Единая система КолАЭР " + Resources.VERSION );
        stage.setOnCloseRequest(event -> System.exit(0));

        PARAM.putAll(getParameters().getNamed());

        stage.setScene(new Scene(mainPane));
        stage.getIcons().add(new Image(getClass().getResource("/css/aerIcon.png").toString(), false));
        stage.setMaximized(true);
        stage.getScene().getStylesheets().add(getClass().getResource("/css/ToastStyle.css").toString());
        stage.setFullScreen(false);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
            if (e.getCode() == KeyCode.F11)
                stage.setFullScreen(true);
        });

        stage.centerOnScreen();
        stage.show();

        initialize();
    }

    @Override
    public void login(AccountDto account) {

    }

    @Override
    public void logout(AccountDto account) {
//        Tools.runOnWithOutThreadFX(() -> stage.getOnCloseRequest().handle(null));
    }
}