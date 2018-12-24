package ru.kolaer.common.system.impl;

import ru.kolaer.common.system.Authentication;
import ru.kolaer.common.system.PluginsUS;
import ru.kolaer.common.system.UniformSystemEditorKit;
import ru.kolaer.common.system.network.NetworkUS;
import ru.kolaer.common.system.ui.UISystemUS;

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
