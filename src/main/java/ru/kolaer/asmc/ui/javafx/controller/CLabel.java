package ru.kolaer.asmc.ui.javafx.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

/**
 * Контроллер ярлыка.
 * 
 * @author Danilov
 * @version 0.1
 */
public class CLabel extends BorderPane implements ObservableLabel {

	/** Список слушателей. */
	private ObserverLabel obsList;
	/** Кнопка запускающие приложение. */
	private Button button;
	/** Иконка. */
	private ImageView image;
	/** Имя ярлыка. */
	private Label nameLabel;
	private ScrollPane scrollBackPanel;
	private Tooltip toolTip;
	/** Модель. */
	private MLabel model;


	public CLabel(final MLabel model) {
		this.model = model;

		this.initialize();

		this.updateView(this.model);
	}

	public void initialize() {
		final ContextMenu labelContextMenu = new ContextMenu();

		this.button = new Button();
		this.nameLabel = new Label();
		this.image = new ImageView();
		this.toolTip = new Tooltip();
		this.scrollBackPanel = new ScrollPane();

		final MenuItem editLabel = new MenuItem(Resources.MENU_ITEM_EDIT_LABEL);
		final MenuItem deleteLabel = new MenuItem(Resources.MENU_ITEM_DELETE_LABEL);

		Tools.runOnThreadFX(() -> {
			labelContextMenu.getItems().addAll(editLabel, deleteLabel);

			final BorderPane borderPane = new BorderPane(this.button);
			borderPane.setTop(this.nameLabel);
			//borderPane.setLeft(this.image);
			borderPane.getStyleClass().add("lableSecontPanel");

			this.button.setMaxWidth(Double.MAX_VALUE);
			this.button.setPrefHeight(60);
			this.button.getStyleClass().add("record-sales");

			this.nameLabel.setTextAlignment(TextAlignment.CENTER);
			this.nameLabel.setAlignment(Pos.CENTER);
			this.nameLabel.setMaxWidth(Double.MAX_VALUE);
			//this.nameLabel.setStyle("-fx-background-color:  rgba(255, 255, 255, 0.3);");

			this.image.setSmooth(true);
			this.image.setFitHeight(100);
			this.image.setFitWidth(100);

			this.scrollBackPanel.setFitToWidth(true);
			this.scrollBackPanel.setFitToHeight(true);
			this.scrollBackPanel.setContextMenu(labelContextMenu);
			this.scrollBackPanel.setContent(borderPane);
			this.scrollBackPanel.getStyleClass().add("lableTherdPanel");
			this.scrollBackPanel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

			final BorderPane icon  = new BorderPane(this.image);
			//icon.setStyle("-fx-background-color:  rgba(255, 255, 255, 0.3);");

			this.setPrefWidth(500);
			this.getStyleClass().add("lableMainPane");
			this.setCenter(this.scrollBackPanel);
			this.setLeft(icon);
			BorderPane.setMargin(this.image, new Insets(5,5,5,5));
		});

		//=====Event====
		this.scrollBackPanel.setOnContextMenuRequested(e -> {
			Tools.runOnThreadFX(() -> {
				if(!SettingSingleton.getInstance().isRoot())
					labelContextMenu.hide();
			});
		});

		editLabel.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				this.model = new CAddingLabelDialog(this.model).showAndWait().get();
				this.updateView(this.model);
				this.notifyObserverEdit();
			});
		});
		
		deleteLabel.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				final Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Вы действительно хотите удалить ярлык \"" + this.model.getName() + "\"?");
				if (alert.showAndWait().get() == ButtonType.OK) {
					this.notifyObserverDelete();
				}
			});
		});
		
		this.button.setOnAction(e -> this.notifyObserverClick());
	}

	private void updateView(final MLabel label) {
		Tools.runOnThreadFX(() -> {
			this.nameLabel.setFont(Font.font(18 - label.getName().length() * 0.15));
			this.nameLabel.setText(label.getName());

			this.button.setFont(Font.font(18 - label.getName().length() * 0.15));
			this.button.setText(label.getInfo());
			ToolTipManager.sharedInstance().setDismissDelay(0);
			toolTip.setText(label.getName());
			this.button.setTooltip(this.toolTip);


			if(label.getPathImage().equals(Resources.AER_ICON.toString())) {
				this.image.setImage(new Image(Resources.AER_ICON.toString(), true));
			} else {
				final Optional<File> file = Optional.of(new File(label.getPathImage()));

				if(file.isPresent() && (file.get().exists() && file.get().isFile())){
					try{
						this.image.setImage(new Image(file.get().toURI().toURL().toString(), true));
					}
					catch(final Exception e1){
						final Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Ошибка!");
						alert.setHeaderText("Невозможно переконвертировать в URL файл:" +file.get().getAbsolutePath());
						alert.setContentText(e1.toString());
						alert.showAndWait();
					}
				}
				else {
					this.image.setImage(null);
					this.image.setVisible(false);
				}
			}
		});
	}
	
	@Override
	public void notifyObserverClick() {
		if(this.obsList != null)
			this.obsList.updateClick(this.model);
	}

	@Override
	public void registerOberver(ObserverLabel observer) {
		this.obsList = observer;
	}

	@Override
	public void removeObserver(ObserverLabel observer) {
		this.obsList = null;
	}

	/**
	 * @return the {@linkplain #model}
	 */
	public MLabel getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the {@linkplain #model} to set
	 */
	public void setModel(final MLabel model) {
		this.model = model;
	}

	@Override
	public void notifyObserverEdit() {
		if(this.obsList != null)
			this.obsList.updateEdit(this.model);
	}

	@Override
	public void notifyObserverDelete() {
		if(this.obsList != null)
			this.obsList.updateDelete(this.model);
	}
}
