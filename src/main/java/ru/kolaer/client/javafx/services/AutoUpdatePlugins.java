package ru.kolaer.client.javafx.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;

public class AutoUpdatePlugins implements Service {
	private final Logger LOG = LoggerFactory.getLogger(AutoUpdatePlugins.class);
	
	private boolean isRun = false;
	private final PluginManager pluginManager;
	private final VMExplorer explorer;
	private final ServiceControlManager serviceControlManager;
	
	public AutoUpdatePlugins(final PluginManager pluginManager, final VMExplorer explorer, final ServiceControlManager serviceControlManager) {
		this.pluginManager = pluginManager;
		this.explorer = explorer;
		this.serviceControlManager = serviceControlManager;
	}
	
	@Override
	public void run() {
		this.isRun = true;
		
		while(this.isRun) {
			try{
				TimeUnit.MINUTES.sleep(1);
			}catch(InterruptedException e){
				LOG.error("Прервано ожидание!");
				this.isRun = false;
			}
			
			final Collection<PluginBundle> installPlugins = this.pluginManager.getAllPlugins();
			final List<PluginBundle> pluginsInFoldes = this.pluginManager.getSearchPlugins().search();
			
			pluginsInFoldes.parallelStream().forEach(plugin -> {
				installPlugins.parallelStream().forEach(pluginInstalls -> {	
					if(plugin.getNamePlugin().equals(pluginInstalls.getNamePlugin())) {
						if(plugin.getVersion().equals(pluginInstalls.getVersion())) {
							if(plugin.getFirstModified() != pluginInstalls.getFirstModified()) {
								plugin.getUniformSystemPlugin().getServices().parallelStream().forEach(serviceControlManager::removeService);
								explorer.removePlugin(plugin);
								try{
									pluginManager.unInstall(plugin);
								}catch(Exception e){
									LOG.error("Ошибка при удалении плагина: {}",plugin.getUriPlugin(), e);
								}
							} else {
								return;
							}
						}
					}
				});
			});
			
			
		}
	}

	@Override
	public boolean isRunning() {
		return this.isRun;
	}

	@Override
	public String getName() {
		return "Автообновление плагинов";
	}

	@Override
	public void stop() {
		this.isRun = false;
	}
}
