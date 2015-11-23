package ru.kolaer.asmc.ui.javafx.controller;

/**
 * Слушатель ярлыков.
 * @author Danilov
 * @version 0.1
 */
public interface ObservableLabel {
	/**Оповестить слушателей о нажатии на элемет {@linkplain CLabel}.*/
	void notifyObserverClick();
	/**Оповестить слушателей о изменении элемета {@linkplain CLabel}.*/
	void notifyObserverEdit();
	/**Оповестить слушателей о удалении элемета {@linkplain CLabel}.*/
	void notifyObserverDelete();
	
	void registerOberver(ObserverLabel observer);
	void removeObserver(ObserverLabel observer);
}
