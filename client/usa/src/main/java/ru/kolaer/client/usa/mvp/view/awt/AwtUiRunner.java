package ru.kolaer.client.usa.mvp.view.awt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.api.plugins.UniformSystemPluginAwt;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.impl.DefaultMenuBarUS;
import ru.kolaer.api.system.impl.DefaultNotificationUS;
import ru.kolaer.api.system.impl.DefaultPluginsUS;
import ru.kolaer.client.usa.mvp.view.AbstractApplicationUiRunner;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.usa.system.network.NetworkUSRestTemplate;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;

import java.awt.*;

/**
 * Created by danilovey on 13.10.2017.
 */
public class AwtUiRunner extends AbstractApplicationUiRunner<UniformSystemPluginAwt, Panel> {
    private VTabExplorerAwt vTabExplorerAwt;
    private VMainFrameAwt vMainFrameAwt;

    @Override
    public boolean initializeUi() {
        vMainFrameAwt = new VMainFrameAwt();
        vTabExplorerAwt = new VTabExplorerAwt();

        vMainFrameAwt.setContent(vTabExplorerAwt.getContent());

        vMainFrameAwt.show();
        return true;
    }

    @Override
    public UniformSystemEditorKit initializeUniformSystemEditorKit() {
        Thread.currentThread().setName("Инициализация UniformSystemEditorKit - AWT");

        ObjectMapper objectMapper = new ObjectMapper();

        //MenuBarUS menuBarUS = new MenuBarUSAwt(menuBar);
        //NotificationUS notify = new NotificationAwtExceptionHandler();
        NetworkUSRestTemplate network = new NetworkUSRestTemplate(objectMapper);
        UISystemUSImpl uiSystemUS = new UISystemUSImpl();
        uiSystemUS.setNotification(new DefaultNotificationUS());
        uiSystemUS.setMenuBarUS(new DefaultMenuBarUS());

        //Thread.setDefaultUncaughtExceptionHandler(notify);

        AuthenticationOnNetwork authentication = new AuthenticationOnNetwork(objectMapper);
        //authentication.registerObserver(menuBarUS);

        UniformSystemEditorKitSingleton editorKit = UniformSystemEditorKitSingleton.getInstance();
        editorKit.setUSNetwork(network);
        editorKit.setUISystemUS(uiSystemUS);
        editorKit.setPluginsUS(new DefaultPluginsUS());
        editorKit.setAuthentication(authentication);

        return editorKit;
    }

    @Override
    public VMainFrame<Panel> getFrame() {
        return vMainFrameAwt;
    }

    @Override
    public VTabExplorer<UniformSystemPluginAwt, Panel> getExplorer() {
        return vTabExplorerAwt;
    }


    @Override
    public TypeUi getTypeUi() {
        return TypeUi.LOW;
    }

    @Override
    public void shutdown() {

    }
}
