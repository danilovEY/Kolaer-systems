package ru.kolaer.asmc.runnable;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.plugins.ILabel;

public class ASMCLabel implements ILabel {

	@Override
	public String getName() {
		return "АСУП-v3";
	}

	@Override
	public String getIcon() {
		return "resources/aerIcon.png";
	}
}
