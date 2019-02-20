package ru.kolaer.client.usa.system;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.usa.tools.Resources;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by Danilov on 24.04.2016.
 */
public class SettingsSingleton {
    private static final Logger log = LoggerFactory.getLogger(SettingsSingleton.class);
    private static SettingsSingleton instance;

    @Getter
    private final String urlServerMain;
    @Getter
    private final String urlServiceChat;
    @Getter
    private final String pathCache;
    @Getter
    private final String pathPlugins;
    @Getter
    private final boolean multipleInstant;
    @Getter
    private final boolean pathCacheRand;
    @Getter
    private final boolean serviceEnable;
    @Getter
    private final boolean trayEnable;

    private SettingsSingleton(String urlServerMain, String urlServiceChat, String pathCache, String pathPlugins, boolean multipleInstant,
            boolean pathCacheRand, boolean serviceEnable, boolean trayEnable) {
        this.urlServerMain = urlServerMain;
        this.urlServiceChat = urlServiceChat;
        this.pathCache = pathCache;
        this.pathPlugins = pathPlugins;
        this.multipleInstant = multipleInstant;
        this.pathCacheRand = pathCacheRand;
        this.serviceEnable = serviceEnable;
        this.trayEnable = trayEnable;
    }


    public static SettingsSingleton getInstance() {
        if(instance == null) {
            instance = initSettings();
        }

        return instance;
    }

    private static Properties readProperties() {
        Properties properties = new Properties();

        try(InputStream resourceAsStream = Resources.class.getResourceAsStream(Resources.CONFIG_PATH)) {
            properties.load(new InputStreamReader(resourceAsStream, Charset.forName("UTF-8")));
        } catch (Exception ex) {
            log.error("Невозможно прочитать конфигурацию", ex);
        }

        return properties;
    }

    private static SettingsSingleton initSettings() {
        Properties properties = readProperties();

        return new SettingsSingleton(
                properties.getProperty(Resources.PROPERTIES_NAME_URL_SERVER_MAIN, Resources.PROPERTIES_DEFAULT_VALUE_URL_SERVER_MAIN),
                properties.getProperty(Resources.PROPERTIES_NAME_URL_SERVICE_CHAT, Resources.PROPERTIES_DEFAULT_VALUE_URL_SERVICE_CHAT),
                properties.getProperty(Resources.PROPERTIES_NAME_PATH_CACHE, Resources.PROPERTIES_DEFAULT_VALUE_PATH_CACHE),
                properties.getProperty(Resources.PROPERTIES_NAME_PATH_PLUGINS, Resources.PROPERTIES_DEFAULT_VALUE_PATH_PLUGINS),
                Boolean.valueOf(properties.getProperty(Resources.PROPERTIES_NAME_MULTIPLE_INSTANT, Resources.PROPERTIES_DEFAULT_VALUE_MULTIPLE_INSTANT)),
                Boolean.valueOf(properties.getProperty(Resources.PROPERTIES_NAME_PATH_CACHE_RAND, Resources.PROPERTIES_DEFAULT_VALUE_PATH_CACHE_RAND)),
                Boolean.valueOf(properties.getProperty(Resources.PROPERTIES_NAME_SERVICE_ENABLE, Resources.PROPERTIES_DEFAULT_VALUE_SERVICE_ENABLE)),
                Boolean.valueOf(properties.getProperty(Resources.PROPERTIES_NAME_TRAY_ENABLE, Resources.PROPERTIES_DEFAULT_VALUE_TRAY_ENABLE))
        );
    }

}
