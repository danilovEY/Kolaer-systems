package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.view.VLabelCss;

/**
 * Слушатель ярлыков.
 * @author Danilov
 * @version 0.1
 */
public interface ObservableLabel {
	/**Оповестить слушателей о нажатии на элемет {@linkplain VLabelCss}.*/
	void notifyObserverClick();
	/**Оповестить слушателей о изменении элемета {@linkplain VLabelCss}.*/
	void notifyObserverEdit();
	/**Оповестить слушателей о удалении элемета {@linkplain VLabelCss}.*/
	void notifyObserverDelete();
	
	void registerOberver(ObserverLabel observer);
	void removeObserver(ObserverLabel observer);
}
