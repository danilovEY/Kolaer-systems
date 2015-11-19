package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 * Контроллер ярлыка.
 * @author Danilov
 * @version 0.1
 */
public class CLabel extends BaseController implements Initializable, ObservableLabel {
	
	/**Список слушателей.*/
	private final List<ObserverLabel> obsList = new ArrayList<>();
	/**Кнопка запускающие приложение.*/
	@FXML
	private Button button;
	/**Иконка.*/
	@FXML
	private ImageView image;
	/**Текст с описанием.*/
	@FXML
	private TextArea infoText;
	/**Имя ярлыка.*/
	@FXML
	private Label nameLabel;
	/**Модель.*/
	private MLabel model;
	
	public CLabel() {
		super(Resources.V_LABEL);
	}
	
	public CLabel(MLabel model){
		super(Resources.V_LABEL);
		this.model = model;
		
		if(this.model == null) return;
		
		this.button.setText(this.model.getName());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.button.setOnMouseClicked(e -> this.notifyObserverClick());
	}

	@Override
	public void notifyObserverClick() {
		this.obsList.forEach(o -> o.update(this.model));
	}

	@Override
	public void registerOberver(ObserverLabel observer) {
		this.obsList.add(observer);
	}

	@Override
	public void removeObserver(ObserverLabel observer) {
		this.obsList.remove(observer);
	}
}
