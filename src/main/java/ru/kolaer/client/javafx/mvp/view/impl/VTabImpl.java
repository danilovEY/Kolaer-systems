package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.mvp.view.VTab;

import java.util.concurrent.TimeUnit;

/**
 * Реализация {@linkplain VTab}.
 *
 * @author Danilov
 * @version 0.1
 */
public class VTabImpl implements VTab {
	/**JavaFX вкладка.*/
	private final Tab tab;
	/**Окно плагина.*/
	private Stage stage;

	private Node content;

	public VTabImpl() {
		this.tab = new Tab();
		Tools.runOnThreadFXAndWain(() -> {
			this.init();
		}, 20, TimeUnit.SECONDS);
	}
	
	private void init() {	
		this.tab.setText("Плагин");
		this.tab.setStyle(".tab .tab:selected{-fx-background-color: #3c3c3c;} .tab.tab-label { -fx-text-fill: -fx-text-base-color; -fx-font-size: 18px;}");
		final MenuItem openInWindow = new MenuItem("Открыть в новом окне");
		openInWindow.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				if(this.stage == null) {
					this.stage = new Stage();				
					this.stage.setOnCloseRequest(event -> {
							stage.setScene(null);
							tab.setContent(VTabImpl.this.content);
					});
				}
				this.tab.setContent(null);
				this.stage.setScene(new Scene(new BorderPane(VTabImpl.this.content), 1024, 768));
				this.stage.centerOnScreen();
				this.stage.show();
			});
		});
		
		this.tab.setContextMenu(new ContextMenu(openInWindow));
	}

	@Override
	public Tab getContent() {
		return this.tab;
	}

	@Override
	public void setContent(final Node parent) {
		Tools.runOnThreadFXAndWain(() -> {
			this.content = parent;
			this.tab.setContent(parent);
		},20, TimeUnit.SECONDS);
	}

	@Override
	public void setTitle(final String title) {
		Tools.runOnThreadFX(() -> {
			this.tab.setText(title);
		});
	}

	@Override
	public String getTitle() {
		return this.tab.getText();
	}

	@Override
	public void closeTab() {
		Tools.runOnThreadFX(() -> {
			if(this.stage != null) {
				this.stage.close();
			}
			this.setContent(null);
			this.tab.getTabPane().getTabs().remove(this.tab);
		});
	}	
}
