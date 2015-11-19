package ru.kolaer.asmc.ui.javafx.controller;

/**
 * Слушатель ярлыков.
 * @author Danilov
 * @version 0.1
 */
public interface ObservableLabel {
	/**Оповестить слушателей о нажатии на элемет {@linkplain CLabel}.*/
	void notifyObserverClick();
	
	void registerOberver(ObserverLabel observer);
	void removeObserver(ObserverLabel observer);
}
