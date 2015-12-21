package ru.kolaer.client.javafx.runnable;

import javafx.application.Application;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;

public class Launcher {
	public static void main(final String[] args) {
		Application.launch(VMMainFrameImpl.class ,args);
	}
}