package ru.kolaer.asmc.ui.javafx.controller;

import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**Слушатель группы ярлыков.*/
public interface ObserverGroupLabels {
	/**
	 * Получить оповещение о нажатии на группу.
	 * @param group Модель нажатой группы.
	 */
	void updateClick(MGroupLabels group);
	
	/**
	 * Получить оповещение о изменении группы.
	 * @param group Модель измененной группы.
	 */
	void updateEdit(MGroupLabels group);
}
