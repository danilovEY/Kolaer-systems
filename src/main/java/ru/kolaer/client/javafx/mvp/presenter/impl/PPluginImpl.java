package ru.kolaer.client.javafx.mvp.presenter.impl;

import ru.kolaer.client.javafx.mvp.presenter.PPlugin;
import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMLabelImpl;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class PPluginImpl implements PPlugin{

	private final IKolaerPlugin plugin;
	private final VMLabel label;
	private PWindow window;
	
	public PPluginImpl(final IKolaerPlugin plugin) {
		this.plugin = plugin;
		this.label = new VMLabelImpl(plugin.getLabel());
		this.init();
	}
	
	private void init() {
		this.label.setOnAction(e -> {
			if(this.window == null)
				this.window = new PCustomStageImpl(this.plugin.getApplication());
			this.window.show();
		});
	}
	
	@Override
	public VMLabel getVMLabel() {
		return this.label;
	}

	@Override
	public PWindow getWindow() {
		return this.window;
	}

	@Override
	public IKolaerPlugin getPlugin() {
		return this.plugin;
	}

}
