package ru.kolaer.asmc.ui.javafx.controller;

import java.util.Optional;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface Dialog {
	/**Показать окно и ждать результата.*/
	public Optional<?> showAndWait();
}
