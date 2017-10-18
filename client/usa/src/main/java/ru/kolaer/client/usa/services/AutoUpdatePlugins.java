package ru.kolaer.client.usa.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.plugins.PluginManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoUpdatePlugins<U extends UniformSystemPlugin, T> implements Service {
	private final Logger LOG = LoggerFactory.getLogger(AutoUpdatePlugins.class);
	
	private boolean isRun = false;
	private final PluginManager pluginManager;
	private final VTabExplorer<U, T> explorer;
	private final ServiceManager serviceManager;
	private final TypeUi typeUi;

	public AutoUpdatePlugins(PluginManager pluginManager,
							 VTabExplorer<U, T> explorer,
							 ServiceManager serviceManager,
							 TypeUi typeUi) {
		this.pluginManager = pluginManager;
		this.explorer = explorer;
		this.serviceManager = serviceManager;
		this.typeUi = typeUi;
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
			
			List<PluginBundle> installPlugins = new ArrayList<>(this.pluginManager.getInstallPlugins());
			List<PluginBundle> pluginsInFolders = this.pluginManager.getSearchPlugins().search();
			
			for(Iterator<PluginBundle> installPluginsIter = installPlugins.iterator(); installPluginsIter.hasNext(); ) {
				PluginBundle pluginInstall = installPluginsIter.next();
				
				for(Iterator<PluginBundle> pluginsInFolderIter = pluginsInFolders.iterator(); pluginsInFolderIter.hasNext(); ) {
					PluginBundle pluginInFolder = pluginsInFolderIter.next();
					
					if(pluginInFolder.getSymbolicNamePlugin().equals(pluginInstall.getSymbolicNamePlugin())
                            && pluginInFolder.getVersion().equals(pluginInstall.getVersion())) {
						installPluginsIter.remove();
						pluginsInFolderIter.remove();
						break;
					}
				}
			}	
			
			installPlugins.forEach(this::unInstallPlugin);
			pluginsInFolders.forEach(this::installPlugin);
		}
	}
	
	private void installPlugin(PluginBundle<U> pluginBundle) {
		//Установка нового плагина
		pluginManager.install(pluginBundle, typeUi);
		
		LOG.info("{}: Создание вкладки...", pluginBundle.getSymbolicNamePlugin());
        String tabName = pluginBundle.getNamePlugin() + " (" + pluginBundle.getVersion() + ")";
        explorer.addTabPlugin(tabName, pluginBundle);
        
        LOG.info("{}: Получение служб...", pluginBundle.getSymbolicNamePlugin());
        Collection<Service> pluginServices = pluginBundle.getUniformSystemPlugin().getServices();
        if (pluginServices != null) {
            pluginServices.parallelStream().forEach(serviceManager::addService);
        }
	}
	
	private void unInstallPlugin(PluginBundle<U> pluginBundle) {
		//Удаление старого плагина
		Collection<Service> services = pluginBundle.getUniformSystemPlugin().getServices();
		if(services != null) {
			services.parallelStream().forEach(serviceManager::removeService);
		}
		
		explorer.removePlugin(pluginBundle);
		pluginManager.unInstall(pluginBundle);
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
