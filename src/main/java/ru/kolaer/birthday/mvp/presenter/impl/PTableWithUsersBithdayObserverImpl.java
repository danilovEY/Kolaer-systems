package ru.kolaer.birthday.mvp.presenter.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBirthdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbBirthdayAll;
import ru.kolaer.server.dao.entities.DbDataAll;

/**
 * Presenter таблици с сотрудниками.
 *
 * @author danilovey
 * @version 0.1
 */
public class PTableWithUsersBithdayObserverImpl implements PTableWithUsersBirthdayObserver{
	private final Logger LOG = LoggerFactory.getLogger(PTableWithUsersBithdayObserverImpl.class);
	/**View таблици.*/
	private final VTableWithUsersBirthday table = new VTableWithUsersBirthdayImpl();
	private final UniformSystemEditorKit editorKid;
	
	public PTableWithUsersBithdayObserverImpl() {
		this(null);
	}
	
	
	public PTableWithUsersBithdayObserverImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.initWithEditorKid();
	}

	private void initWithEditorKid()  {	
		
		this.table.setMouseClick(e -> {
			if(e.getClickCount() == 2) {
				new VMDetailedInformationStageImpl(this.table.getSelectModel()).show();
			}
		});
		
		CompletableFuture.runAsync(() -> {
			final Service<Void> service = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							this.updateTitle("КолАтомэнергоремонт");
							this.updateMessage("Загрузка данных с сервера");
							this.updateProgress(0, 10);
							final DbDataAll[] users = editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
							this.updateProgress(users.length, users.length * 2);
							this.updateMessage("Чтение данных");
							int index = 0;
							for(final DbDataAll user : users) {
								final UserModel userModel = new UserModelImpl();
								userModel.setOrganization("КолАтомэнергоремонт");
								userModel.setEmail(user.getEmail());
								userModel.setInitials(user.getInitials());
								userModel.setFirstName(user.getName());
								userModel.setSecondName(user.getSurname());
								userModel.setThirdName(user.getPatronymic());
								userModel.setBirthday(user.getBirthday());
								userModel.setDepartament(user.getDepartament());
								userModel.setPhoneNumber(user.getPhone());
								userModel.setIcon(user.getVCard());
								userModel.setPost(user.getPost());
								table.addData(userModel);
								this.updateProgress(index, users.length * 2);
								index++;
							}
							this.updateProgress(users.length * 2, users.length * 2);
							return null;
						}
					};
				}
			};
			service.start();
			this.editorKid.getUISystemUS().getDialog().showLoadingDialog(service);
		}).exceptionally(t -> {
			LOG.error("Ошибка!", t);
			return null;
		}).thenRunAsync(() -> {
			final Service<Void> service = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							this.updateTitle("Филиалы");
							this.updateMessage("Загрузка данных с сервера");
							this.updateProgress(0, 10);
							final DbBirthdayAll[] users = editorKid.getUSNetwork().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersBirthdayToday();
							this.updateProgress(users.length, users.length * 2);
							this.updateMessage("Чтение данных");
							int index = 0;
							for(final DbBirthdayAll user : users) {
								final UserModel userModel = new UserModelImpl();
								userModel.setOrganization(user.getOrganization());
								userModel.setInitials(user.getInitials());
								userModel.setBirthday(user.getBirthday());
								userModel.setEmail(user.getEmail());
								userModel.setDepartament(user.getDepartament());
								userModel.setPhoneNumber(user.getPhone());
								userModel.setPost(user.getPost());
								table.addData(userModel);
								this.updateProgress(index, users.length * 2);
								index++;
							}
							this.updateProgress(users.length * 2, users.length * 2);
							return null;
						}
					};
				}
			};
			service.start();
			this.editorKid.getUISystemUS().getDialog().showLoadingDialog(service);
		});
	}

	@Override
	public VTableWithUsersBirthday getView() {
		return this.table;
	}

	@Override
	public void updateSelectedDate(final LocalDate date, final List<UserModel> users) {
		this.table.setData(users);
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		this.table.setTitle("\"" + date.format(formatter) + "\" день рождения у:");
	}
	
}
