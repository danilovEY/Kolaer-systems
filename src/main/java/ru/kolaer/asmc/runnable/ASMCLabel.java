package ru.kolaer.asmc.runnable;

import ru.kolaer.client.javafx.plugins.ILabel;

public class ASMCLabel implements ILabel {

	@Override
	public String getName() {
		return "АСУП-v33";
	}

	@Override
	public String getIcon() {
		return "resources/aerIcon.png";
	}
}
