package ru.kolaer.client.message;

import javafx.scene.Node;
import ru.kolaer.client.message.service.AutoCheckingNotifyMessage;
import ru.kolaer.common.plugins.UniformSystemPlugin;
import ru.kolaer.common.plugins.services.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * Created by danilovey on 15.02.2018.
 */
public class MessagePlugin implements UniformSystemPlugin {
    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
    }

    @Override
    public Node getContent() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return Collections.singletonList(new AutoCheckingNotifyMessage());
    }

    @Override
    public boolean isInitPluginView() {
        return false;
    }
}
