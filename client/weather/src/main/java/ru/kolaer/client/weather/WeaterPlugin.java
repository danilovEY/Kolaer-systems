package ru.kolaer.client.weather;

import javafx.scene.Node;
import ru.kolaer.client.core.plugins.UniformSystemPlugin;
import ru.kolaer.client.core.plugins.services.Service;
import ru.kolaer.client.weather.service.WeatherService;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * Created by danilovey on 15.02.2018.
 */
public class WeaterPlugin implements UniformSystemPlugin {
    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {

    }

    @Override
    public Node getContent() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return Collections.singletonList(new WeatherService());
    }

    @Override
    public boolean isInitPluginView() {
        return false;
    }
}
