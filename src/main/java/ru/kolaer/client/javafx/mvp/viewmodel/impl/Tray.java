package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Danilov on 11.05.2016.
 */
public class Tray {
    private final Logger LOG = LoggerFactory.getLogger(Tray.class);

    private Stage mainStage;
    private boolean firstTime = true;
    private TrayIcon trayIcon;

    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            this.mainStage = stage;

            final SystemTray tray = SystemTray.getSystemTray();

            this.mainStage.setOnCloseRequest(event -> {
                if (trayIcon != null) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            });

            final PopupMenu popup = new PopupMenu();

            final MenuItem showItem = new MenuItem("Открыть");
            final MenuItem closeItem = new MenuItem("Закрыть");

            final ActionListener showStage = e1 -> {
                Platform.runLater(() -> {
                    stage.show();
                });
            };

            showItem.addActionListener(showStage);

            closeItem.addActionListener(e -> {
                System.exit(0);
            });

            popup.add(showItem);
            popup.add(closeItem);

            try {
                this.trayIcon = new TrayIcon(ImageIO.read(this.getClass().getResource("/css/aerIcon.gif")), "Title", popup);
                this.trayIcon.addActionListener(showStage);

                tray.add(this.trayIcon);
            } catch (AWTException e) {
                LOG.error("Ошибка при добавлении иконки в трей!", e);
            } catch (IOException e) {
                LOG.error("Ошибка при чтении иконки для трея!", e);
            }
        } else {
            stage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        }
    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Внимание!.", "Приложение свернулось и находится в трее.", TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
}
