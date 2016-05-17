package ru.kolaer.client.javafx.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMTabExplorerOSGi;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;

public class AutoUpdatePlugins implements Service {
	private final Logger LOG = LoggerFactory.getLogger(AutoUpdatePlugins.class);
	
	private boolean isRun = false;
	private final PluginManager pluginManager;
	private final VMTabExplorerOSGi explorer;
	private final ServiceControlManager serviceControlManager;
	
	public AutoUpdatePlugins(final PluginManager pluginManager, final VMTabExplorerOSGi explorer, final ServiceControlManager serviceControlManager) {
		this.pluginManager = pluginManager;
		this.explorer = explorer;
		this.serviceControlManager = serviceControlManager;
	}
	
	@Override
	public void run() {
		this.isRun = true;
		
		while(this.isRun) {
			try{
				TimeUnit.MINUTES.sleep(5);
			}catch(InterruptedException e){
				LOG.error("Прервано ожидание!");
				this.isRun = false;
			}
			
			final List<PluginBundle> installPlugins = new ArrayList<>(this.pluginManager.getInstallPlugins());
			final List<PluginBundle> pluginsInFoldes = this.pluginManager.getSearchPlugins().search();
			
			pluginsInFoldes.parallelStream().forEach(plugin -> {
				installPlugins.parallelStream().forEach(pluginInstalls -> {	
					if(plugin.getSymbolicNamePlugin().equals(pluginInstalls.getSymbolicNamePlugin())) {
						if(plugin.getVersion().equals(pluginInstalls.getVersion())) {
							if(plugin.getFirstModified() != pluginInstalls.getFirstModified()) {
								this.unInstallPlugin(pluginInstalls);
								this.installPlugin(plugin);
							} 
						}
					}
				});
			});
			
			
			for(Iterator<PluginBundle> installPluginsIter = installPlugins.iterator(); installPluginsIter.hasNext(); ) {
				final PluginBundle pluginInstall = installPluginsIter.next();
				
				for(Iterator<PluginBundle> pluginsInFolderIter = pluginsInFoldes.iterator(); pluginsInFolderIter.hasNext(); ) {
					final PluginBundle pluginInFolder = pluginsInFolderIter.next();
					
					if(pluginInFolder.getSymbolicNamePlugin().equals(pluginInstall.getSymbolicNamePlugin())) {
						installPluginsIter.remove();
						pluginsInFolderIter.remove();
						break;
					}
				}
			}	
			
			installPlugins.parallelStream().forEach(this::unInstallPlugin);
			pluginsInFoldes.parallelStream().forEach(this::installPlugin);
		}
	}
	
	private void installPlugin(final PluginBundle pluginBundle) {
		//Установка нового плагина
		this.pluginManager.install(pluginBundle);
		
		LOG.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
        final String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
        this.explorer.addTabPlugin(tabName, pluginBundle);
        
        LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
        final Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
        if (pluginServices != null) {
            pluginServices.parallelStream().forEach(this.serviceControlManager::addService);
        }
	}
	
	private void unInstallPlugin(final PluginBundle pluginBundle) {
		//Удаление старого плагина
		if(pluginBundle.getUniformSystemPlugin().getServices() != null)
			pluginBundle.getUniformSystemPlugin().getServices().parallelStream().forEach(serviceControlManager::removeService);
		
		this.explorer.removePlugin(pluginBundle);
		this.pluginManager.unInstall(pluginBundle);
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
