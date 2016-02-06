package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VCustomStage;

/**
 * Контроллер для JavaFX - окна.
 * @author danilovey
 *
 */
public interface PCustomStage extends PWindow {
	/**Получить view окна.*/
	VCustomStage getView();
	/**Задать view окна.*/
	void setView(final VCustomStage view);
}
