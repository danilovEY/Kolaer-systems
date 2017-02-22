package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.model.MLabel;

/**
 * Слушатель ярлыков.
 * @author Danilov
 * @version 0.1
 */
public interface ObserverLabel {
	/**
	 * Получить оповещение о нажатии на ярлык.
	 * @param model Модель нажатого ярлыка.
	 */
	void updateClick(MLabel model);

	/**
	 * Получить оповещение о изменении ярлыка.
	 * @param model Модель нажатого ярлыка.
	 */
	void updateEdit(MLabel model);

	/**
	 * Получить оповещение о удалении ярлыка.
	 * @param model Модель нажатого ярлыка.
	 */
	void updateDelete(MLabel model);
}
