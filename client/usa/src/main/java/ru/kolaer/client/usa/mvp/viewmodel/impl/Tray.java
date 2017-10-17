package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.usa.plugins.PluginManager;
import ru.kolaer.client.usa.services.ServiceControlManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Danilov on 11.05.2016.
 */
public class Tray {
    private final Logger LOG = LoggerFactory.getLogger(Tray.class);

    private boolean firstTime = true;
    private TrayIcon trayIcon;

    public void createTrayIcon(Stage stage, PluginManager pluginManager, ServiceControlManager servicesManager, VMExplorer explorer) {
        stage.setOnCloseRequest(event -> {
            final ExecutorService serviceThread = Executors.newSingleThreadExecutor();
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
                Platform.runLater(() -> {
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

        stage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if (trayIcon != null && newValue) {
                Tools.runOnThreadFX(stage::hide);
                showProgramIsMinimizedMsg();
            }
        });

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Открыть");
            MenuItem closeItem = new MenuItem("Закрыть");

            ActionListener showStage = e1 -> {
                Tools.runOnThreadFX(() -> {
                    LOG.info("AAAAAA");
                    stage.show();
                    stage.setMaximized(false); //JRE BUG
                    stage.setMaximized(true);
                });
            };

            showItem.addActionListener(showStage);
            closeItem.addActionListener(e -> stage.getOnCloseRequest().handle(null));

            popup.add(showItem);
            popup.add(closeItem);

            try {
                trayIcon = new TrayIcon(ImageIO.read(getClass().getResource("/css/aerIcon.gif")), "Единая система приложений", popup);
                trayIcon.addActionListener(showStage);

                tray.add(trayIcon);
            } catch (AWTException e) {
                LOG.error("Ошибка при добавлении иконки в трей!", e);
            } catch (IOException e) {
                LOG.error("Ошибка при чтении иконки для трея!", e);
            }
        }
    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Внимание!", "Приложение свернулось и находится в трее.", TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
}
