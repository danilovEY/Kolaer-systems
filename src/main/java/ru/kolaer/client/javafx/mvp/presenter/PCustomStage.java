package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VCustomStage;

/**
 * Контроллер для JavaFX - окна.
 * @author danilovey
 *
 */
public interface PCustomStage extends PWindow{
	VCustomStage getView();
	void setView(final VCustomStage view);
}
