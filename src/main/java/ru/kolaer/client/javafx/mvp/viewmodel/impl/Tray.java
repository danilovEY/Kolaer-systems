package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.services.ServiceControlManager;

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

    public void createTrayIcon(final Stage stage, final ServiceControlManager servicesManager, final VMExplorer explorer) {
        stage.setOnCloseRequest(event -> {
            if (trayIcon != null) {
                Tools.runOnThreadFX(() -> {
                    stage.hide();
                });
                this.showProgramIsMinimizedMsg();
            } else {
                servicesManager.removeAllServices();
                explorer.removeAll();
                System.exit(0);
            }
        });

        if (SystemTray.isSupported()) {
            this.mainStage = stage;

            final SystemTray tray = SystemTray.getSystemTray();

            final PopupMenu popup = new PopupMenu();

            final MenuItem showItem = new MenuItem("Открыть");
            final MenuItem closeItem = new MenuItem("Закрыть");

            final ActionListener showStage = e1 -> {
                Tools.runOnThreadFX(() -> {
                    stage.show();
                });
            };

            showItem.addActionListener(showStage);

            closeItem.addActionListener(e -> {
                servicesManager.removeAllServices();
                explorer.removeAll();
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
        }
    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Внимание!.", "Приложение свернулось и находится в трее.", TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
}
