package ru.kolaer.client.javafx.mvp.view;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public interface VTab {
	Tab getContent();
	void setContent(Node node);
}
