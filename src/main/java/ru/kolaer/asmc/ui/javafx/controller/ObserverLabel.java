package ru.kolaer.asmc.ui.javafx.controller;

import ru.kolaer.asmc.ui.javafx.model.MLabel;

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
	void update(MLabel model);
}
