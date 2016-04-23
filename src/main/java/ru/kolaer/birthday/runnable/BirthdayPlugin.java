package ru.kolaer.birthday.runnable;

import java.util.ArrayList;
import java.util.List;

import ru.kolaer.api.plugin.AbstractUniformSystemPlugin;
import ru.kolaer.birthday.service.BirthdayOnHoliday;
import ru.kolaer.birthday.service.BirthdayService;
import ru.kolaer.api.plugin.UniformSystem;
import ru.kolaer.api.plugin.UniformSystemApplication;
import ru.kolaer.api.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

@UniformSystem
/**
 * Реализация модуля.
 *
 * @author danilovey
 * @version 0.1
 */
public class BirthdayPlugin extends AbstractUniformSystemPlugin {
	private UniformSystemApplication application;
	/**Список служб.*/
	private List<Service> servicesList;
	
	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		this.application = new BirthdayApplication(editorKid);
		this.servicesList = new ArrayList<>();
		this.servicesList.add(new BirthdayService(editorKid));
		this.servicesList.add(new BirthdayOnHoliday(editorKid));
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

	@Override
	public void updatePluginObjects(String key, Object object) {
	}
}
