package ru.kolaer.client.core.system.impl;

import ru.kolaer.client.core.system.Authentication;
import ru.kolaer.client.core.system.PluginsUS;
import ru.kolaer.client.core.system.UniformSystemEditorKit;
import ru.kolaer.client.core.system.network.NetworkUS;
import ru.kolaer.client.core.system.ui.UISystemUS;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultUniformSystemEditorKit implements UniformSystemEditorKit {
    private final NetworkUS networkUS = new DefaultNetworkUS();
    private final UISystemUS uiSystemUS = new DefaultUISystemUS();
    private final PluginsUS pluginsUS = new DefaultPluginsUS();
    private final Authentication authentication = new DefaultAuthentication();

    @Override
    public NetworkUS getUSNetwork() {
        return this.networkUS;
    }

    @Override
    public UISystemUS getUISystemUS() {
        return this.uiSystemUS;
    }

    @Override
    public PluginsUS getPluginsUS() {
        return this.pluginsUS;
    }

    @Override
    public Authentication getAuthentication() {
        return this.authentication;
    }
}
