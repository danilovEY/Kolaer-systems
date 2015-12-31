package ru.kolaer.client.javafx.runnable;

import javafx.application.Application;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.client.javafx.tools.Resources;

public class Launcher {
	public static void main(final String[] args) {
		if(args.length == 1) {
			Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append(args[0]);
		}
		
		Application.launch(VMMainFrameImpl.class ,args);
	}
}