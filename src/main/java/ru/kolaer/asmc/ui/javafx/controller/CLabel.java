package ru.kolaer.asmc.ui.javafx.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
	private static final Logger LOG = LoggerFactory.getLogger(CLabel.class);

	/** Список слушателей. */
	private final List<ObserverLabel> obsList = new ArrayList<>();
	/** Кнопка запускающие приложение. */
	@FXML
	private Button button;
	/** Иконка. */
	@FXML
	private ImageView image;
	/** Текст с описанием. */
	@FXML
	private TextArea infoText;
	/** Имя ярлыка. */
	@FXML
	private Label nameLabel;
	@FXML
	private ScrollPane scrollBackPanel;
	/** Модель. */
	private MLabel model;
	

	public CLabel() {
		super(Resources.V_LABEL);
	}

	public CLabel(MLabel model) {
		super(Resources.V_LABEL);
		this.model = model;

		if(this.model == null) return;

		this.nameLabel.setText(this.model.getName());
		this.infoText.setText(this.model.getInfo());

		final Optional<File> file = Optional.of(new File(this.model.getPathImage()));

		if(file.isPresent() && (file.get().exists() && file.get().isFile())){
			try{
				this.image.setImage(new Image(file.get().toURI().toURL().toString()));
			}
			catch(Exception e){
				LOG.error("Невозможно переконвертировать в URL файл:" + file.get().getAbsolutePath(), e);
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final ContextMenu labelContextMenu = new ContextMenu();

		final MenuItem editLabel = new MenuItem(Resources.MENU_ITEM_EDIT_LABEL);
		final MenuItem deleteLabel = new MenuItem(Resources.MENU_ITEM_DELETE_LABEL);
		labelContextMenu.getItems().addAll(editLabel, deleteLabel);
		
		//=====Event====
		EventHandler<ContextMenuEvent> contextMenu = e -> {
			if(!SettingSingleton.getInstance().isRoot()) 
				labelContextMenu.hide();
		};
		
		this.scrollBackPanel.setContextMenu(labelContextMenu);
		this.scrollBackPanel.setOnContextMenuRequested(contextMenu);

		editLabel.setOnAction(e -> {
			this.model = new CAddingLabelDialog(this.model).showAndWait().get();

			this.notifyObserverEdit();
		});
		
		deleteLabel.setOnAction(e -> {
			this.notifyObserverDelete();
		});
		
		this.button.setOnAction(e -> this.notifyObserverClick());
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
	public void setModel(MLabel model) {
		this.model = model;
	}

	@Override
	public void notifyObserverEdit() {
		this.obsList.forEach(o -> o.updateEdit(this.model));
	}

	@Override
	public void notifyObserverDelete() {
		this.obsList.forEach(o -> o.updateDelete(this.model));
	}
}
