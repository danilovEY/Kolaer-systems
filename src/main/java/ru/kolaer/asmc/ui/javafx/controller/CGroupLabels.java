package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * Контреллер для группы ярлыков.
 *
 * @author Danilov
 * @version 0.2
 */
public class CGroupLabels extends BaseController implements ObservableGroupLabels{

	/**Модель.*/
	private MGroupLabels model;
	/**Список слушателей.*/
	private final List<ObserverGroupLabels> observerList = new ArrayList<>();
	
	/**Кнопка для отображения ярлыков.*/
	@FXML
	private Button button;
	
	/**
	 * {@linkplain CGroupLabels}
	 */
	public CGroupLabels(){
		super(Resources.V_GROUP_LABELS);
	}
	
	/**
	 * 
	 * {@linkplain CGroupLabels}
	 * @param group Модель.
	 */
	public CGroupLabels(final MGroupLabels group) {
		super(Resources.V_GROUP_LABELS);
		this.model = group;
		super.setUserData(Integer.valueOf(group.getPriority()));
		this.setText(this.model.getNameGroup());

	}
	
	public void setText(final String text){
		this.button.setText(text);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
		final ContextMenu contextGroupPanel = new ContextMenu();
		final MenuItem editGroupLabels = new MenuItem(Resources.MENU_ITEM_EDIT_GROUP);
		final MenuItem deleteGroupLabels = new MenuItem(Resources.MENU_ITEM_DELETE_GROUP);
		
		contextGroupPanel.getItems().addAll(editGroupLabels, deleteGroupLabels);
		
		this.button.setContextMenu(contextGroupPanel);
		
		this.button.setOnContextMenuRequested(event -> {
			if(!SettingSingleton.getInstance().isRoot()) 
				contextGroupPanel.hide();
		});
		
		deleteGroupLabels.setOnAction(e -> {
			final Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Вы действительно хотите удалить группу \""+ this.model.getNameGroup() + "\"?");
			if(alert.showAndWait().get() == ButtonType.OK) {
				this.notifyObserverDelete();
			}
		});
		
		editGroupLabels.setOnAction(e -> {
			this.model = new CAddingGroupLabelsDialog(this.model).showAndWait().get();
			this.button.setText(this.model.getNameGroup());
			this.notifyObserverEdit();
		});

		this.button.setOnAction((e)->this.notifyObserverClick());
	}

	@Override
	public void notifyObserverClick() {
		observerList.forEach((o) -> o.updateClick(this.model));
	}

	@Override
	public void registerOberver(ObserverGroupLabels observer) {
		observerList.add(observer);
	}

	@Override
	public void removeObserver(ObserverGroupLabels observer) {
		observerList.remove(observer);
	}

	@Override
	public void notifyObserverEdit() {
		observerList.forEach((o) -> o.updateEdit(this.model));
	}

	/**
	 * @return the {@linkplain #model}
	 */
	public MGroupLabels getModel() {
		return model;
	}

	/**
	 * @param model the {@linkplain #model} to set
	 */
	public void setModel(final MGroupLabels model) {
		this.model = model;
	}

	@Override
	public void notifyObserverDelete() {
		observerList.forEach((o) -> {
			o.updateDelete(this.model);
		});
		observerList.clear();
	}
}
