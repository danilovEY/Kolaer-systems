package ru.kolaer.birthday.runnable;

import javafx.application.Application;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;

public class Launcher {
	public static void main(String[] args) {
		Application.launch(VMMainFrameImpl.class, args);
	}
}
