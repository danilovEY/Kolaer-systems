package ru.kolaer.client.javafx.plugins;

import java.net.URI;

/**
 * Created by Danilov on 10.04.2016.
 */
public abstract class PluginBundle {
    private String version;
    private String pathPlugin;
    private String symbolicNamePlugin;
    private String namePlugin;
    private URI uriPlugin;

    public String getSymbolicNamePlugin() {
        return symbolicNamePlugin;
    }

    public void setSymbolicNamePlugin(String symbolicNamePlugin) {
        this.symbolicNamePlugin = symbolicNamePlugin;
    }

    public URI getUriPlugin() {
        return uriPlugin;
    }

    public void setUriPlugin(URI uriPlugin) {
        this.uriPlugin = uriPlugin;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPathPlugin() {
        return pathPlugin;
    }

    public void setPathPlugin(String pathPlugin) {
        this.pathPlugin = pathPlugin;
    }

    public String getNamePlugin() {
        return namePlugin;
    }

    public void setNamePlugin(String namePlugin) {
        this.namePlugin = namePlugin;
    }
}
