package ru.kolaer.common.system;

import ru.kolaer.common.system.network.NetworkUS;
import ru.kolaer.common.system.ui.UISystemUS;

public interface UniformSystemEditorKit {
	NetworkUS getUSNetwork();
	UISystemUS getUISystemUS();
	PluginsUS getPluginsUS();

	Authentication getAuthentication();
}
