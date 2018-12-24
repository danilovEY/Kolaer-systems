package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import ru.kolaer.client.usa.mvp.view.VTab;
import ru.kolaer.common.tools.Tools;

import java.util.function.Consumer;

/**
 * Реализация {@linkplain VTab}.
 *
 * @author Danilov
 * @version 0.1
 */
public class VTabImpl implements VTab {
	/**JavaFX вкладка.*/
	private Tab tab;
	/**Окно плагина.*/
	private Stage stage;

	private Node content;


	@Override
	public Tab getContent() {
		return this.tab;
	}

	@Override
	public void setContent(Node parent) {
		Tools.runOnWithOutThreadFX(() -> {
			this.content = parent;
			if(parent == null) {
				this.tab.setContent(new Region());
			} else {
				this.tab.setContent(parent);
			}
		});
	}

	@Override
	public void setTitle(final String title) {
		Tools.runOnWithOutThreadFX(() -> {
			this.tab.setText(title);
		});
	}

	@Override
	public String getTitle() {
		return this.tab.getText();
	}

	@Override
	public void closeTab() {
		Tools.runOnWithOutThreadFX(() -> {
			if(this.stage != null) {
				this.stage.close();
			}
			this.tab.setContent(null);
			if(this.tab != null && this.tab.getTabPane() != null)
				this.tab.getTabPane().getTabs().remove(this.tab);
		});
	}

	@Override
	public void initView(Consumer<VTab> viewVisit) {
		tab = new Tab();
		tab.setText("Плагин");
		tab.setStyle(".tab .tab:selected{-fx-background-color: #3c3c3c;} .tab.tab-label { -fx-text-fill: -fx-text-base-color; -fx-font-size: 18px;}");
		MenuItem openInWindow = new MenuItem("Открыть в новом окне");
		openInWindow.setOnAction(e -> {
			Tools.runOnWithOutThreadFX(() -> {
				if(stage == null) {
					stage = new Stage();
					stage.setOnCloseRequest(event -> {
						stage.setScene(null);
						tab.setContent(content);
					});
				}
				tab.setContent(new Region());
				stage.setScene(new Scene(new BorderPane(content), 1024, 768));
				stage.centerOnScreen();
				stage.show();
			});
		});

		tab.setContextMenu(new ContextMenu(openInWindow));

		viewVisit.accept(this);
	}
}
