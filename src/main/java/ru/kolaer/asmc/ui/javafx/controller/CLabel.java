package ru.kolaer.asmc.ui.javafx.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.ToolTipManager;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 * Контроллер ярлыка.
 * 
 * @author Danilov
 * @version 0.1
 */
public class CLabel extends BaseController implements Initializable, ObservableLabel {

	/** Список слушателей. */
	private final List<ObserverLabel> obsList = new ArrayList<>();
	/** Кнопка запускающие приложение. */
	@FXML
	private Button button;
	/** Иконка. */
	@FXML
	private ImageView image;
	/** Имя ярлыка. */
	@FXML
	private Label nameLabel;
	@FXML
	private ScrollPane scrollBackPanel;
	@FXML
	private Tooltip toolTip;
	/** Модель. */
	private MLabel model;
	

	public CLabel() {
		super(Resources.V_LABEL);
	}

	public CLabel(final MLabel model) {
		super(Resources.V_LABEL);
		this.model = model;

		if(this.model == null) return;

		this.updateView(this.model);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		final ContextMenu labelContextMenu = new ContextMenu();

		final MenuItem editLabel = new MenuItem(Resources.MENU_ITEM_EDIT_LABEL);
		final MenuItem deleteLabel = new MenuItem(Resources.MENU_ITEM_DELETE_LABEL);
		labelContextMenu.getItems().addAll(editLabel, deleteLabel);
		
		//=====Event====
		final EventHandler<ContextMenuEvent> contextMenu = e -> {
			if(!SettingSingleton.getInstance().isRoot()) 
				labelContextMenu.hide();
		};
		
		this.scrollBackPanel.setContextMenu(labelContextMenu);
		this.scrollBackPanel.setOnContextMenuRequested(contextMenu);

		editLabel.setOnAction(e -> {
			this.model = new CAddingLabelDialog(this.model).showAndWait().get();
			this.updateView(this.model);
			this.notifyObserverEdit();
		});
		
		deleteLabel.setOnAction(e -> {
			final Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Вы действительно хотите удалить ярлык \""+ this.model.getName() + "\"?");
			if(alert.showAndWait().get() == ButtonType.OK) {
				this.notifyObserverDelete();
			}
		});
		
		this.button.setOnAction(e -> this.notifyObserverClick());
	}

	private void updateView(final MLabel label) {
		this.nameLabel.setText(label.getName());
		this.button.setText(label.getInfo());
		
		ToolTipManager.sharedInstance().setDismissDelay(0);
		toolTip.setText(label.getName());
		
		final Optional<File> file = Optional.of(new File(label.getPathImage()));

		if(file.isPresent() && (file.get().exists() && file.get().isFile())){
			try{
				this.image.setImage(new Image(file.get().toURI().toURL().toString()));
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
		}
	}
	
	@Override
	public void notifyObserverClick() {
		this.obsList.forEach(o -> o.updateClick(this.model));
	}

	@Override
	public void registerOberver(ObserverLabel observer) {
		this.obsList.add(observer);
	}

	@Override
	public void removeObserver(ObserverLabel observer) {
		this.obsList.remove(observer);
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
		this.obsList.forEach(o -> o.updateEdit(this.model));
	}

	@Override
	public void notifyObserverDelete() {
		this.obsList.forEach((o) -> {
			o.updateDelete(this.model);
		});
		this.obsList.clear();
	}
}
