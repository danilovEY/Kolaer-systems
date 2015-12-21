package ru.kolaer.client.javafx.mvp.presenter.impl;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PPlugin;
import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMLabelImpl;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class PPluginImpl implements PPlugin{

	private final IKolaerPlugin plugin;
	private final VMLabel label;
	private final Pane taskPane;
	private PWindow window;
	
	public PPluginImpl(final IKolaerPlugin plugin) {
		this(plugin, null);
	}
	
	public PPluginImpl(final IKolaerPlugin plugin, final Pane taskPane) {
		this.plugin = plugin;
		this.taskPane = taskPane;
		this.label = new VMLabelImpl(plugin.getLabel());
		this.init();
	}
	
	private void init() {
		this.label.setOnAction(e -> {
			if(this.window == null)
				this.window = new PCustomStageImpl(this.plugin.getApplication());
			this.window.show();
			
			if(this.taskPane!=null && this.window.getTaskPane() != null)
				this.taskPane.getChildren().add(this.window.getTaskPane().getContent());
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
