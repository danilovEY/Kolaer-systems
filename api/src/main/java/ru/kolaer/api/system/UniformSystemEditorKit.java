package ru.kolaer.api.system;

import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.ui.UISystemUS;

public interface UniformSystemEditorKit {
	NetworkUS getUSNetwork();
	UISystemUS getUISystemUS();
	PluginsUS getPluginsUS();
}
