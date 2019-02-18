package ru.kolaer.client.core.system;

import ru.kolaer.client.core.system.network.NetworkUS;
import ru.kolaer.client.core.system.ui.UISystemUS;

public interface UniformSystemEditorKit {
	NetworkUS getUSNetwork();
	UISystemUS getUISystemUS();
	PluginsUS getPluginsUS();

	Authentication getAuthentication();
}
