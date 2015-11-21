package ru.kolaer.asmc.ui.javafx.controller;

/**
 * Оповещяет слушателей о event-ах {@linkplain CGroupLabels}.
 *
 * @author Danilov
 * @version 0.1
 */
public interface ObservableGroupLabels {
	/**Оповестить слушателей о нажатии на элемет {@linkplain CGroupLabels}.*/
	void notifyObserverClick();
	/**Оповестить слушателей о изменении элемета {@linkplain CGroupLabels}.*/
	void notifyObserverEdit();
	
	void registerOberver(ObserverGroupLabels observer);
	void removeObserver(ObserverGroupLabels observer);
}
