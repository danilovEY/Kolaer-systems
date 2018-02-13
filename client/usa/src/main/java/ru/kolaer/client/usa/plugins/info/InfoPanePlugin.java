package ru.kolaer.client.usa.plugins.info;

import javafx.scene.Node;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.usa.services.AutoCheckingNotifyMessage;
import ru.kolaer.client.usa.services.CounterService;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
public class InfoPanePlugin implements UniformSystemPlugin {
    private final InfoPaneVc infoPaneVc = new InfoPaneVcImpl();

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        infoPaneVc.initView(BaseView::empty);

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return infoPaneVc.getContent();
    }

    @Override
    public Collection<Service> getServices() {
        return Arrays.asList(new CounterService(), new AutoCheckingNotifyMessage());
    }
}
