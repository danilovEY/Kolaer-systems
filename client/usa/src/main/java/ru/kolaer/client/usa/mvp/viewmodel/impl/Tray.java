package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.common.tools.Tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.function.Function;

/**
 * Created by Danilov on 11.05.2016.
 */
public class Tray {
    private final Logger LOG = LoggerFactory.getLogger(Tray.class);

    private boolean firstTime = true;
    private TrayIcon trayIcon;

    public void initTrayIcon(Stage stage) {
        if(stage == null) {
            return;
        }

        Function<Boolean, Void> minFun = hide -> {
            if(trayIcon != null && hide) {
                stage.hide();
                showProgramIsMinimizedMsg();
            }
            return null;
        };

        stage.showingProperty().addListener((observable, oldValue, newValue) -> minFun.apply(newValue));

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Открыть");
            MenuItem closeItem = new MenuItem("Закрыть");

            ActionListener showStage = e1 -> Tools.runOnWithOutThreadFX(() -> {
                stage.show();
                stage.setMaximized(false); //JRE BUG
                stage.setMaximized(true);
            });

            showItem.addActionListener(showStage);
            closeItem.addActionListener(e ->
                    Tools.runOnWithOutThreadFX(() -> stage.getOnCloseRequest().handle(null)));

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

    private void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Внимание!", "Приложение свернулось и находится в трее.", TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
}
