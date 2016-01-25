package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.List;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.plugins.UniformSystemLabel;

public class PMainApplication implements UniformSystemPlugin {

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public UniformSystemLabel getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniformSystemApplication getApplication() {
		// TODO Auto-generated method stub
		return new UniformSystemApplication() {
			
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

	@Override
	public void initialization(UniformSystemEditorKit editorKid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Service> getServices() {
		// TODO Auto-generated method stub
		return null;
	}

}
