package ru.kolaer.client.javafx.mvp.presenter.impl;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.ILabel;

public class PMainApplication implements IKolaerPlugin {

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public ILabel getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IApplication getApplication() {
		// TODO Auto-generated method stub
		return new IApplication() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void stop() {
				System.exit(-1);
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "Main";
			}
			
			@Override
			public String getIcon() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Pane getContent() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
