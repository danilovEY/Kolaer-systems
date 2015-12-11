package ru.kolaer.client.javafx.plugins;

@ApplicationPlugin
public interface IKolaerPlugin {
	String getName();
	ILabel getLabel();
	IApplication getApplication();
}
