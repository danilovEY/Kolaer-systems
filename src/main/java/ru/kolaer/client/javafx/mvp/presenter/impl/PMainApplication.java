package ru.kolaer.client.javafx.mvp.presenter.impl;

import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemApplicationAdapter;
import ru.kolaer.client.javafx.plugins.UniformSystemPluginAdapter;

public class PMainApplication extends UniformSystemPluginAdapter {
	private final UniformSystemApplicationAdapter app = new UniformSystemApplicationAdapter() {
		@Override
		public void stop() {
			System.exit(-1);
		}
		
		@Override
		public String getName() {
			return "Main";
		}
	};
	@Override
	public String getName() {
		return "Main";
	}
	
	@Override
	public UniformSystemApplication getApplication() {
		return app;
	}
}
