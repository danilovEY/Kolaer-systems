package ru.kolaer.client.javafx.plugins.main.launcher;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMTabExplorerOSGi;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.plugins.UniformSystemPluginAdapter;
import ru.kolaer.client.javafx.services.ServiceControlManager;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by danilovey on 17.08.2016.
 */
public class LauncherPagePlugin extends UniformSystemPluginAdapter {
    private final Logger LOG = LoggerFactory.getLogger(LauncherPagePlugin.class);
    private final PluginManager pluginManager;
    private final VMTabExplorerOSGi explorer;
    private final ServiceControlManager servicesManager;
    private BorderPane mainPane;

    public LauncherPagePlugin(VMTabExplorerOSGi explorer, final PluginManager pluginManager, ServiceControlManager servicesManager) {
        this.pluginManager = pluginManager;
        this.explorer = explorer;
        this.servicesManager = servicesManager;
    }

    @Override
    public void start() throws Exception {
        final VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        this.mainPane = new BorderPane(vBox);
        BackgroundImage myBI= new BackgroundImage(new Image(this.getClass().getResource("/1.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        //then you set to your node
        this.mainPane.setBackground(new Background(myBI));
        pluginManager.getSearchPlugins().search().parallelStream().forEach(pluginBundle -> {
            Tools.runOnThreadFX(() -> {
                final Button launchButton = new Button(pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")");
                launchButton.setStyle("    -fx-background-color: linear-gradient(#ff5400, #be1d00);\n" +
                        "    -fx-background-radius: 30;\n" +
                        "    -fx-background-insets: 0;\n" +
                        "    -fx-text-fill: white;" +
                        "    -fx-font-weight: bold;\n" +
                        "    -fx-font-size: 2em;");
                launchButton.setMinWidth(400);
                launchButton.setCursor(Cursor.HAND);
                launchButton.setOnAction(e -> {
                    for(PluginBundle plugin : explorer.getAllPlugins()) {
                        if(plugin.getNamePlugin().equals(pluginBundle.getNamePlugin()) && plugin.getVersion().equals(pluginBundle.getVersion())) {
                            explorer.showPlugin(plugin.getUniformSystemPlugin());
                            return;
                        }
                    }
                    this.installPluginInThread(explorer, pluginManager, pluginBundle);
                });
                vBox.getChildren().add(launchButton);
            });
        });
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    public void installPluginInThread(final VMTabExplorerOSGi explorer, final PluginManager pluginManager, final PluginBundle pluginBundle) {
        final ExecutorService threadInstallPlugin = Executors.newSingleThreadExecutor();
        CompletableFuture.supplyAsync(() -> {
            installPlugin(explorer, pluginManager, pluginBundle);
            return pluginBundle;
        }, threadInstallPlugin).thenAcceptAsync((plugin) -> {
            explorer.showPlugin(plugin.getUniformSystemPlugin());
            threadInstallPlugin.shutdown();
        }).exceptionally(t -> {
            LOG.error("Ошибка при установке!", t);
            return null;
        });
    }

    public void installPlugin(final VMTabExplorerOSGi explorer, final PluginManager pluginManager, final PluginBundle pluginBundle) {
        Thread.currentThread().setName("Установка плагина: " + pluginBundle.getNamePlugin());
        LOG.info("{}: Установка плагина.", pluginBundle.getPathPlugin());

        if (pluginManager.install(pluginBundle)) {
            LOG.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
            final String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
            explorer.addTabPlugin(tabName, pluginBundle);

            LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
            final Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
            if (pluginServices != null) {
                pluginServices.parallelStream().forEach(this.servicesManager::addService);
            }
        }
    }
}
