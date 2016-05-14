package ru.kolaer.asmc.ui.javafx.controller;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * Контреллер для группы ярлыков.
 *
 * @author Danilov
 * @version 0.2
 */
public class CGroupLabels extends BorderPane implements ObservableGroupLabels{

	/**Модель.*/
	private MGroupLabels model;

	private ObserverGroupLabels observer;

	private final Button button;
	/**
	 * {@linkplain CGroupLabels}
	 */
	public CGroupLabels(){
		this(null);
	}

	/**
	 * 
	 * {@linkplain CGroupLabels}
	 * @param group Модель.
	 */
	public CGroupLabels(final MGroupLabels group) {
		this.model = group;
		Tools.runOnThreadFX(() -> {
			super.setUserData(Integer.valueOf(group.getPriority()));
		});

		this.button = new Button();

		this.setText(this.model.getNameGroup());

		this.initialize();
	}
	
	public void setText(final String text){
		Tools.runOnThreadFX(() -> {
			this.button.setText(text);
		});
	}

	public void initialize() {
		final ContextMenu contextGroupPanel = new ContextMenu();
		final MenuItem editGroupLabels = new MenuItem(Resources.MENU_ITEM_EDIT_GROUP);
		final MenuItem deleteGroupLabels = new MenuItem(Resources.MENU_ITEM_DELETE_GROUP);

		Tools.runOnThreadFX(() -> {
			contextGroupPanel.getItems().addAll(editGroupLabels, deleteGroupLabels);

			this.button.setContextMenu(contextGroupPanel);
		});


		
		this.button.setOnContextMenuRequested(event -> {
			Tools.runOnThreadFX(() -> {
				if(!SettingSingleton.getInstance().isRoot())
					contextGroupPanel.hide();
			});
		});
		
		deleteGroupLabels.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				final Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Вы действительно хотите удалить группу \"" + this.model.getNameGroup() + "\"?");
				if (alert.showAndWait().get() == ButtonType.OK) {
					this.notifyObserverDelete();
				}
			});
		});
		
		editGroupLabels.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				this.model = new CAddingGroupLabelsDialog(this.model).showAndWait().get();
				this.button.setText(this.model.getNameGroup());
				this.notifyObserverEdit();
			});
		});

		this.button.setOnAction((e)->this.notifyObserverClick());
	}

	@Override
	public void notifyObserverClick() {
		if(observer != null)
			observer.updateClick(this.model);
	}

	@Override
	public void registerObserver(ObserverGroupLabels observer) {
		this.observer = observer;
	}

	@Override
	public void removeObserver(ObserverGroupLabels observer) {
		this.observer = null;
	}

	@Override
	public void notifyObserverEdit() {
		if(observer != null)
			observer.updateEdit(this.model);
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
		if(observer != null)
			observer.updateDelete(this.model);
	}
}
