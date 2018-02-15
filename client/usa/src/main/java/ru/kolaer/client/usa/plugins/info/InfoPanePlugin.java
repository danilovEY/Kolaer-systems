package ru.kolaer.client.usa.plugins.info;

import javafx.scene.Node;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.mvp.viewmodel.impl.ServiceManager;
import ru.kolaer.client.usa.services.CounterService;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;

import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
public class InfoPanePlugin implements UniformSystemPlugin {
    private final InfoPaneVc infoPaneVc = new InfoPaneVcImpl();
    private final ServiceManager servicesManager;

    public InfoPanePlugin(ServiceManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        infoPaneVc.initView(BaseView::empty);

        UISystemUSImpl uiSystemUS = (UISystemUSImpl) UniformSystemEditorKitSingleton.getInstance().getUISystemUS();
        uiSystemUS.setStaticUS(infoPaneVc);
        Thread.setDefaultUncaughtExceptionHandler(infoPaneVc);
        servicesManager.addService(new CounterService());

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return infoPaneVc.getContent();
    }
}
