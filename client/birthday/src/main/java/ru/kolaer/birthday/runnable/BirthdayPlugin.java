package ru.kolaer.birthday.runnable;

import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.birthday.mvp.view.MainContentVc;
import ru.kolaer.birthday.mvp.view.MainContentVcImpl;
import ru.kolaer.birthday.service.BirthdayOnHoliday;
import ru.kolaer.birthday.service.BirthdayService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Реализация модуля.
 *
 * @author danilovey
 * @version 0.1
 */
public class BirthdayPlugin implements UniformSystemPlugin {
	private final static Logger log = LoggerFactory.getLogger(BirthdayPlugin.class);

	/**Список служб.*/
	private List<Service> servicesList;
	private MainContentVc mainContentVc;

	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		mainContentVc = new MainContentVcImpl();

		this.servicesList = new ArrayList<>();
		this.servicesList.add(new BirthdayService());
		this.servicesList.add(new BirthdayOnHoliday());
	}

	@Override
	public List<Service> getServices() {
		return this.servicesList;
	}

	@Override
	public Parent getContent() {
		return mainContentVc.getContent();
	}

	@Override
	public void initView(Consumer<UniformSystemPlugin> viewVisit) {
		mainContentVc.initView(initContent -> viewVisit.accept(this));
	}
}
