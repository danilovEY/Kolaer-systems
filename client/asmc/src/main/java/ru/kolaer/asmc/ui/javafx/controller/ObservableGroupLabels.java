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
	void notifyObserverUnClick();
	/**Оповестить слушателей о изменении элемета {@linkplain CGroupLabels}.*/
	void notifyObserverEdit();
	/**Оповестить слушателей о удалении элемета {@linkplain CGroupLabels}.*/
	void notifyObserverDelete();
	
	void registerObserver(ObserverGroupLabels observer);
	void removeObserver(ObserverGroupLabels observer);
}
