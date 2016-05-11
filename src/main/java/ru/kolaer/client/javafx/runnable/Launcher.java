package ru.kolaer.client.javafx.runnable;

import javafx.application.Application;
import javafx.application.Platform;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;

public class Launcher {
	public static void main(final String[] args) {
		Platform.setImplicitExit(false);
		Application.launch(VMMainFrameImpl.class ,args);
	}
}