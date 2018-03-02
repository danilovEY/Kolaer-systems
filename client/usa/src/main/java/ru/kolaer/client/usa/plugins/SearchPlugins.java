package ru.kolaer.client.usa.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Danilov on 10.04.2016.
 */
public class SearchPlugins {
    private static final String DEFAULT_PATH_DOR_PLUGINS = "plugins";
    private final List<String> pathsDirPlugins = new ArrayList<>();
    
    public SearchPlugins(final String pathDirPlugins) {
        this.pathsDirPlugins.add(pathDirPlugins);
    }

    public SearchPlugins() {
        if(this.initDefaultDir())
            this.addDir(DEFAULT_PATH_DOR_PLUGINS);
    }

    private boolean initDefaultDir() {
        final File defaultDir = new File(DEFAULT_PATH_DOR_PLUGINS);
        return defaultDir.exists() || defaultDir.mkdir();
    }

    public void addDir(final String dir) {
        if(dir != null)
            this.pathsDirPlugins.add(dir);
    }

    public void removeDir(final String dir) {
        if(dir != null)
            this.pathsDirPlugins.remove(dir);
    }
    
    public List<PluginBundle> search() {
        List<PluginBundle> plugins = new ArrayList<>();

        search(plugins::add);

        return plugins;
    }

    public void search(Consumer<PluginBundle> consumer) {
        pathsDirPlugins.parallelStream()
                .map(File::new)
                .filter(File::isDirectory)
                .map(pathDir -> pathDir.listFiles(filter -> filter.getName().endsWith(".jar")))
                .flatMap(Stream::of)
                .map(PluginBundleNotReadJar::getInstance)
                .filter(Objects::nonNull)
                .forEach(consumer);
    }
}
