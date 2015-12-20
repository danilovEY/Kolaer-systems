package ru.kolaer.asmc.runnable;

import ru.kolaer.client.javafx.plugins.ApplicationPlugin;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.ILabel;

@ApplicationPlugin
public class ASMCPlugin implements IKolaerPlugin{

	@Override
	public String getName() {
		return "АСУП";
	}

	@Override
	public ILabel getLabel() {
		return new ASMCLabel();
	}

	@Override
	public IApplication getApplication() {
		return new ASMCApplication();
	}

}
