package ru.kolaer.client.javafx.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danilov on 10.04.2016.
 */
public class SearchPlugins {
    private static final String DEFAULT_PATH_DOR_PLUGINS = "plugins";
    private final List<String> pathsDirPlugins = new ArrayList<>();
    private List<PluginBundle> plugins;
    
    public SearchPlugins(final String pathDirPlugins) {
        this.pathsDirPlugins.add(pathDirPlugins);
    }

    public SearchPlugins() {
        this(DEFAULT_PATH_DOR_PLUGINS);
    }

    public void addDir(final String dir) {
        if(dir != null)
            this.pathsDirPlugins.add(dir);
    }

    public void removeDir(final String dir) {
        if(dir != null)
            this.pathsDirPlugins.remove(dir);
    }

    public List<PluginBundle> getPlugins() {
    	if(this.plugins == null) {
    		this.plugins = this.search();
    	}
    	
    	return this.plugins;
    }
    
    public List<PluginBundle> search() {
        final List<PluginBundle> plugins = new ArrayList<>();

        this.pathsDirPlugins.parallelStream().forEach(path -> {
            final File pathDir = new File(path);
            if(pathDir.isDirectory()) {
                final File[] filesToDit = pathDir.listFiles(filter -> {
                    return filter.getName().endsWith(".jar");
                });

                for(final File fileOnDir : filesToDit) {
                    final PluginBundle bundle = PluginBundleJar.getInstance(fileOnDir);
                    
                    if(bundle != null)
                        plugins.add(bundle);
                }
            }
        });

        return plugins;
    }


}
