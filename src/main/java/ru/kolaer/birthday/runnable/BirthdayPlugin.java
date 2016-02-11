package ru.kolaer.birthday.runnable;

import java.util.ArrayList;
import java.util.List;

import ru.kolaer.birthday.service.BirthdayService;
import ru.kolaer.client.javafx.plugins.UniformSystem;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

@UniformSystem
/**
 * Реализация модуля.
 *
 * @author danilovey
 * @version 0.1
 */
public class BirthdayPlugin implements UniformSystemPlugin {
	private UniformSystemApplication application;
	/**Список служб.*/
	private List<Service> servicesList;
	
	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		this.application = new BirthdayApplication(editorKid);
		this.servicesList = new ArrayList<>();
		this.servicesList.add(new BirthdayService(editorKid));
	}

	@Override
	public String getName() {
		return "Birthday";
	}

	@Override
	public UniformSystemApplication getApplication() {
		return this.application;
	}

	@Override
	public List<Service> getServices() {
		return this.servicesList;
	}
}
