package ru.kolaer.client.javafx.plugins;

import ru.kolaer.client.javafx.plugins.ApplicationPlugin;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.ILabel;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

@ApplicationPlugin
public class HWApplication implements IKolaerPlugin{

	public HWApplication() {
		
	}
	
	@Override
	public ILabel getLabel() {
		return new HWLabel();
	}

	@Override
	public IApplication getApplication() {
		return new HWContent();
	}

}
