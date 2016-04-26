package ru.kolaer.client.javafx;

import org.junit.Test;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.plugins.SearchPlugins;

/**
 * Created by Danilov on 10.04.2016.
 */
public class TestOSGI {

    @Test
    public void testPluginManager() throws Exception {
        final PluginManager pm = new PluginManager();

        pm.initialization();

        final SearchPlugins searchPlugins  = new SearchPlugins();

        for(PluginBundle p :  searchPlugins.search()) {

            System.out.println(p.getNamePlugin());
            pm.install(p);
            p.start();
            System.out.println(p.getUniformSystemPlugin());
            //pm.getInfoToBundle().get(p).start();
        }


    }
}
