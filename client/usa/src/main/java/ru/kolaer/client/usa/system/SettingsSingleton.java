package ru.kolaer.client.usa.system;

/**
 * Created by Danilov on 24.04.2016.
 */
public class SettingsSingleton {
    private static SettingsSingleton instance;

    public static SettingsSingleton getInstance() {
        if(instance == null) {
            instance = new SettingsSingleton();
        }

        return instance;
    }

}
