package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PExplorer;
import ru.kolaer.client.javafx.mvp.view.VExplorer;
import ru.kolaer.client.javafx.mvp.view.impl.VExplorerImpl;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PExplorerImpl implements PExplorer {
	private static final Logger LOG = LoggerFactory.getLogger(PExplorerImpl.class);
	
	private VExplorer view = new VExplorerImpl();
	private final List<IKolaerPlugin> pluginsList = new ArrayList<>();
	
	@Override
	public VExplorer getView() {
		return this.view;
	}

	@Override
	public void setExplorer(VExplorer view) {
		this.view = view;
	}

	@Override
	public void addPlugin(IKolaerPlugin plugin) {
		if(this.view != null) {
			this.pluginsList.add(plugin);
			this.view.updataAddPlugin(plugin);
		} else {
			LOG.error("VExplorer is null!");
		}
	}

	@Override
	public void removePlugin(IKolaerPlugin plugin) {
		if(this.view != null) {
			this.pluginsList.remove(plugin);
			this.view.updataRemovePlugin(plugin);
		} else {
			LOG.error("VExplorer is null!");
		}
	}

}
