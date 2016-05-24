package com.teamdev.jxbrowser.chromium.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class NavigationPanel extends BorderPane {
	private final Logger LOG = LoggerFactory.getLogger(NavigationPanel.class);
	private final Browser browser;

	public NavigationPanel(final Browser browser) {
		this.browser = browser;
		this.init();
	}

	private void init() {
		final TextField search = new TextField(browser.getURL());
		search.setMaxWidth(Double.MAX_VALUE);
		search.setOnKeyReleased(e -> {
			if(e.getCode() == KeyCode.ENTER)
				this.browser.loadURL(search.getText());
		});

		this.browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent arg0) {
				Platform.runLater(() -> {
					search.setText(browser.getURL());
				});
			}

			@Override
			public void onStartLoadingFrame(StartLoadingEvent arg0) {
				Platform.runLater(() -> {
					search.setText(browser.getURL());
				});
			}
		});

		final Button prevPage = new Button("", new ImageView(new Image(this.getClass().getResource("/back.png").toString())));
		prevPage.setTooltip(new Tooltip("Назад"));
		prevPage.setCursor(Cursor.HAND);
		prevPage.setStyle("-fx-background-color: transparent;");
		prevPage.setOnMousePressed(e -> {
			prevPage.setGraphic(new ImageView(new Image(this.getClass().getResource("/back-selected.png").toString())));
		});
		prevPage.setOnMouseReleased(e -> {
			prevPage.setGraphic(new ImageView(new Image(this.getClass().getResource("/back.png").toString())));
		});
		prevPage.setOnAction(e -> {
			this.browser.goBack();
		});

		final Button nextPage = new Button("", new ImageView(new Image(this.getClass().getResource("/forward.png").toString())));
		nextPage.setTooltip(new Tooltip("Вперед"));
		nextPage.setCursor(Cursor.HAND);
		nextPage.setStyle("-fx-background-color: transparent;");
		nextPage.setOnMousePressed(e -> {
			nextPage.setGraphic(new ImageView(new Image(this.getClass().getResource("/forward-selected.png").toString())));
		});
		nextPage.setOnMouseReleased(e -> {
			nextPage.setGraphic(new ImageView(new Image(this.getClass().getResource("/forward.png").toString())));
		});
		nextPage.setOnAction(e -> {
			this.browser.goForward();
		});

		final Button refreshPage = new Button("", new ImageView(new Image(this.getClass().getResource("/refresh.png").toString())));
		refreshPage.setTooltip(new Tooltip("Обновить"));
		refreshPage.setCursor(Cursor.HAND);
		refreshPage.setStyle("-fx-background-color: transparent;");
		refreshPage.setOnMousePressed(e -> {
			refreshPage.setGraphic(new ImageView(new Image(this.getClass().getResource("/refresh-selected.png").toString())));
		});
		refreshPage.setOnMouseReleased(e -> {
			refreshPage.setGraphic(new ImageView(new Image(this.getClass().getResource("/refresh.png").toString())));
		});
		refreshPage.setOnAction(e -> {
			this.browser.reload();
		});

		final Button goPage = new Button("", new ImageView(new Image(this.getClass().getResource("/go.png").toString())));
		goPage.setTooltip(new Tooltip("Перейти"));
		goPage.setCursor(Cursor.HAND);
		goPage.setStyle("-fx-background-color: transparent;");
		goPage.setOnAction(e -> {
			this.browser.loadURL(search.getText());
		});

		final Button openInDefaultBrowser = new Button("", new ImageView(new Image(this.getClass().getResource("/browser-default.png").toString(), 20, 20, false, false)));
		openInDefaultBrowser.setTooltip(new Tooltip("Открыть страницу в вашем браузере"));
		openInDefaultBrowser.setCursor(Cursor.HAND);
		openInDefaultBrowser.setStyle("-fx-background-color: transparent;");
		openInDefaultBrowser.setOnAction(e -> {
			try{
				Runtime.getRuntime().exec("cmd /C explorer \"" + this.browser.getURL().toString() + "\"");
			}catch(Exception e1){
				LOG.error("Ошибка при запуске браузера по-умолчанию!", e);
			}
		});

		final HBox buttonPane = new HBox(prevPage, nextPage, refreshPage, openInDefaultBrowser);
		buttonPane.setPrefWidth(FlowPane.USE_PREF_SIZE);

		this.setRight(goPage);
		this.setCenter(search);
		this.setLeft(buttonPane);
	}
}
