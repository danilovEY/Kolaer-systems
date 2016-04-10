package ru.kolaer.client.javafx;

import org.junit.Test;
import org.osgi.framework.BundleException;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.plugins.SearchPlugins;

import java.io.IOException;

/**
 * Created by Danilov on 10.04.2016.
 */
public class TestOSGI {

    @Test
    public void testPluginManager() throws ClassNotFoundException, BundleException, InstantiationException, IllegalAccessException, IOException {
        final PluginManager pm = new PluginManager();

        pm.initialization();

        final SearchPlugins searchPlugins  = new SearchPlugins();

        for(PluginBundle p :  searchPlugins.search()) {

            System.out.println(p.getNamePlugin());
            pm.installPlugin(p);
            pm.getInfoToBundle().get(p).start();
        }


    }
}
