package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.application.Platform;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
                	this.mainStage.show();
                });
            };

            showItem.addActionListener(showStage);

            closeItem.addActionListener(e -> {
            	final ExecutorService serviceThread = Executors.newSingleThreadExecutor();
            	final Future<?> serviceRes = serviceThread.submit(() -> {
            		servicesManager.removeAllServices();
            	});
            	
            	final ExecutorService explorerThread = Executors.newSingleThreadExecutor();
    			final Future<?> explorerRes = explorerThread.submit(() -> {
            		 explorer.removeAll();
            	});

            	Executors.newSingleThreadExecutor().submit(() -> {
            		try{
						serviceRes.get(5, TimeUnit.SECONDS);
						explorerRes.get(1, TimeUnit.MINUTES);
					}catch(Exception e2){
						LOG.error("Ошибка ожидания");
						serviceThread.shutdownNow();
						explorerThread.shutdownNow();
					}
                    Tools.runOnThreadFX(() -> {
                        stage.close();
                        Platform.exit();
                        System.exit(0);
                    });

            	});
            });

            popup.add(showItem);
            popup.add(closeItem);

            try {
                this.trayIcon = new TrayIcon(ImageIO.read(this.getClass().getResource("/css/aerIcon.gif")), "Единая система приложений", popup);
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
            trayIcon.displayMessage("Внимание!", "Приложение свернулось и находится в трее.", TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
}
