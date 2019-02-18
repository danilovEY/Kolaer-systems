package ru.kolaer.client.usa.plugins.info;

import javafx.scene.Node;
import ru.kolaer.client.core.mvp.view.BaseView;
import ru.kolaer.client.core.plugins.UniformSystemPlugin;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;

import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
public class InfoPanePlugin implements UniformSystemPlugin {
    private final InfoPaneVc infoPaneVc = new InfoPaneVcImpl();

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        UISystemUSImpl uiSystemUS = (UISystemUSImpl) UniformSystemEditorKitSingleton.getInstance().getUISystemUS();
        uiSystemUS.setStaticUS(infoPaneVc);
        Thread.setDefaultUncaughtExceptionHandler(infoPaneVc);

        infoPaneVc.initView(BaseView::empty);

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return infoPaneVc.getContent();
    }
}
