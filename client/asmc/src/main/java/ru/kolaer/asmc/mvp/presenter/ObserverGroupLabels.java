package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.model.MGroup;

/**Слушатель группы ярлыков.*/
public interface ObserverGroupLabels {
	/**
	 * Получить оповещение о нажатии на группу.
	 * @param group Модель нажатой группы.
	 */
	void updateClick(MGroup group);
	
	/**
	 * Получить оповещение о изменении группы.
	 * @param group Модель измененной группы.
	 */
	void updateEdit(MGroup group);

	/**
	 * Получить оповещение о удалении группы.
	 * @param group Модель измененной группы.
	 */
	void updateDelete(MGroup model);
}
