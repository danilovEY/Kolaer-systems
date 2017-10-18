package ru.kolaer.client.usa.mvp.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.usa.mvp.viewmodel.impl.Tray;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.plugins.PluginManager;
import ru.kolaer.client.usa.plugins.SearchPlugins;
import ru.kolaer.client.usa.services.AutoCheckingNotifyMessage;
import ru.kolaer.client.usa.services.AutoUpdatePlugins;
import ru.kolaer.client.usa.services.HideShowMainStage;
import ru.kolaer.client.usa.services.ServiceManager;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.tools.Resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by danilovey on 17.10.2017.
 */
public abstract class AbstractApplicationUiRunner<U extends UniformSystemPlugin<T>, T> implements ApplicationUiRunner<U, T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractApplicationUiRunner.class);
    /**
     * Мапа где ключ и значение соответствует ключам и значениям приложения.
     */
    protected Map<String, String> params;
    protected PluginManager pluginManager;
    protected ServiceManager serviceManager;

    @Override
    public void run(String[] args) {
        params = readParams(args);

        boolean randomDirCache = params.getOrDefault(Resources.RAND_DIR_CACHE_PARAM, "false").contains("true");

        serviceManager = new ServiceManager();
        pluginManager = new PluginManager(new SearchPlugins(), randomDirCache);

        initParams();

        ExecutorService mainApplicationThreadPool = Executors.newFixedThreadPool(5);
        Future<PluginManager> initializedPluginManager = mainApplicationThreadPool.submit(this::initPluginManager);
        Future<List<PluginBundle>> searchResult = mainApplicationThreadPool
                .submit(pluginManager.getSearchPlugins()::search);

        CompletableFuture
                .supplyAsync(this::initializeUi, mainApplicationThreadPool)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                }).thenApply(initUi -> {
                    if(!initUi) {
                        throw new RuntimeException("Интерфейс не инициализировался");
                    }

                    return initializeUniformSystemEditorKit();
                }).exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                }).thenApply(editorKit -> {
                    mainApplicationThreadPool.submit(this::initSystemServices);
                    mainApplicationThreadPool.submit(this::initExitAction);
                    mainApplicationThreadPool.submit(this::autoLogin);
                    mainApplicationThreadPool.submit(this::initTray);
                    mainApplicationThreadPool.submit(() -> this.installPlugins(initializedPluginManager, searchResult));
                    mainApplicationThreadPool.shutdown();
                    return null;
                }).exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });

    }

    private void initParams() {
        String pathServerRest = params.get(Resources.PRIVATE_SERVER_URL_PARAM);
        if (pathServerRest != null) {
            Resources.URL_TO_PRIVATE_SERVER.delete(0, Resources.URL_TO_PRIVATE_SERVER.length()).append(pathServerRest);
            log.info("Private server: {}", Resources.URL_TO_PRIVATE_SERVER.toString());
        }

        String pathServerWeb = params.get(Resources.PUBLIC_SERVER_URL_PARAM);
        if (pathServerWeb != null) {
            Resources.URL_TO_PUBLIC_SERVER.delete(0, Resources.URL_TO_PUBLIC_SERVER.length()).append(pathServerWeb);
            log.info("Public server: {}", Resources.URL_TO_PUBLIC_SERVER.toString());
        }

        if (params.getOrDefault(Resources.SERVICE_PARAM, "true").contains("true")) {
            serviceManager.setAutoRun(true);
            log.info("Service ON");
        }
    }

    private Map<String, String> readParams(String[] args) {
        Map<String, String> params = new HashMap<>();

        for (String arg : args) {
            if(arg.contains(Resources.PRIVATE_SERVER_URL_PARAM)) {
                params.put(Resources.PRIVATE_SERVER_URL_PARAM,
                        arg.substring(Resources.PRIVATE_SERVER_URL_PARAM.length()));
            } else if(arg.contains(Resources.PUBLIC_SERVER_URL_PARAM)) {
                params.put(Resources.PUBLIC_SERVER_URL_PARAM,
                        arg.substring(Resources.PUBLIC_SERVER_URL_PARAM.length()));
            } else if(arg.contains(Resources.SERVICE_PARAM)) {
                params.put(Resources.SERVICE_PARAM,
                        arg.substring(Resources.SERVICE_PARAM.length()));
            } else if(arg.contains(Resources.UI_PARAM)) {
                params.put(Resources.UI_PARAM,
                        arg.substring(Resources.UI_PARAM.length()));
            } else if(arg.contains(Resources.TRAY_PARAM)) {
                params.put(Resources.TRAY_PARAM,
                        arg.substring(Resources.TRAY_PARAM.length()));
            } else if(arg.contains(Resources.RAND_DIR_CACHE_PARAM)) {
                params.put(Resources.RAND_DIR_CACHE_PARAM,
                        arg.substring(Resources.RAND_DIR_CACHE_PARAM.length()));
            }
        }

        return params;
    }

    private void installPlugins(Future<PluginManager> pluginManagerFuture, Future<List<PluginBundle>> pluginsFuture) {
        Thread.currentThread().setName("Чтение и установка плагинов");

        try {
            PluginManager pluginManager = pluginManagerFuture.get(30, TimeUnit.SECONDS);
            List<PluginBundle> plugins = pluginsFuture.get(30, TimeUnit.SECONDS);

            plugins.removeIf(plugin -> {
                if(plugin.getSymbolicNamePlugin().equals("ru.kolaer.asmc")) {
                    installPlugin(plugin);
                    return true;
                }
                return false;
            });

            plugins.forEach(this::installPluginInThread);
            plugins.clear();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Ошибка при инициализации и чтении плагинов!", e);
            System.exit(-9);
        }
    }

    private void installPluginInThread(PluginBundle<U> pluginBundle) {
        final ExecutorService threadInstallPlugin = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            installPlugin(pluginBundle);

            threadInstallPlugin.shutdown();
        }, threadInstallPlugin).exceptionally(ex -> {
            log.error("Ошибка при установки прагина: {}", pluginBundle.getNamePlugin(), ex);
            return null;
        });
    }

    private void installPlugin(PluginBundle<U> pluginBundle) {
        Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
        log.info("{}: Установка плагина.", pluginBundle.getPathPlugin());

        if (pluginManager.install(pluginBundle, getTypeUi())) {
            log.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
            String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
            getExplorer().addTabPlugin(tabName, pluginBundle);

            log.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
            Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
            if (pluginServices != null) {
                pluginServices.forEach(serviceManager::addService);
            }
        }
    }

    private void initExitAction() {
        getFrame().setOnExit(e -> {
            ExecutorService serviceThread = Executors.newSingleThreadExecutor();
            CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("Завершение приложения");
                log.info("Завершение служб...");
                serviceManager.removeAllServices();
            }, serviceThread).exceptionally(t -> {
                log.error("Ошибка при завершении всех активных служб!", t);
                return null;
            }).thenAccept(aVoid -> {
                log.info("Завершение вкладок...");
                getExplorer().removeAll();
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
                log.info("Завершение приложения...");
                shutdown();
                System.exit(0);
            }).exceptionally(t -> {
                log.error("Ошибка при завершении всего приложения!", t);
                System.exit(-9);
                return null;
            });
            return null;
        });
    }

    private PluginManager initPluginManager() {
        Thread.currentThread().setName("Инициализация менеджера плагинов");
        try {
            pluginManager.initialization();
            return pluginManager;
        } catch (final Exception e) {
            log.error("Ошибка при инициализации менеджера плагинов!", e);
            throw new RuntimeException("Ошибка при инициализации менеджера плагинов!");
        }
    }

    private void initSystemServices() {
        Thread.currentThread().setName("Добавление системны служб");

        serviceManager.addService(new AutoUpdatePlugins<>(pluginManager, getExplorer(), serviceManager, getTypeUi()), true);
        serviceManager.addService(new AutoCheckingNotifyMessage(), true);
        if (params.getOrDefault(Resources.TRAY_PARAM, "true").contains("true")) {
            serviceManager.addService(new HideShowMainStage(getFrame()), true);
        }
    }

    private void initTray() {
        if (params.getOrDefault(Resources.TRAY_PARAM, "true").contains("true")) {
            Thread.currentThread().setName("Создание инонки для трея");

            new Tray().initTrayIcon(getFrame());
        }
    }

    private boolean autoLogin() {
        Thread.currentThread().setName("Добавление системны служб");

        return UniformSystemEditorKitSingleton.getInstance().getAuthentication().loginIsRemember();
    }

}
