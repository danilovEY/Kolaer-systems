package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * Контреллер для группы ярлыков.
 *
 * @author Danilov
 * @version 0.2
 */
public class CGroupLabels extends BaseController implements ObservableGroupLabels{
	
	private static final Logger LOG = LoggerFactory.getLogger(CGroupLabels.class);
	/**Модель.*/
	private MGroupLabels groupModel;
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
	public CGroupLabels(MGroupLabels group) {
		super(Resources.V_GROUP_LABELS);
		this.groupModel = group;
		
		if(this.groupModel == null) return;
		
		this.setText(this.groupModel.getNameGroup());

	}
	
	public void setText(String text){
		this.button.setText(text);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.button.setOnMouseClicked((e)->this.notifyObserverClick());
	}

	@Override
	public void notifyObserverClick() {
		observerList.forEach((o) -> o.updateClick(this.groupModel));
	}

	@Override
	public void registerOberver(ObserverGroupLabels observer) {
		observerList.add(observer);
	}

	@Override
	public void removeObserver(ObserverGroupLabels observer) {
		observerList.remove(observer);
	}
}
