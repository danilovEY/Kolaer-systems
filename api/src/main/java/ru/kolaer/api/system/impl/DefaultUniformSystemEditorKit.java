package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.PluginsUS;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.ui.UISystemUS;

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
