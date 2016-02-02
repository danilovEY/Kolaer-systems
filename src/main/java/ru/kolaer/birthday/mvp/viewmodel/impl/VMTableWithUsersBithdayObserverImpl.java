package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBirthdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthdayObserver;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class VMTableWithUsersBithdayObserverImpl implements VMTableWithUsersBirthdayObserver{
	private final VTableWithUsersBirthday table = new VTableWithUsersBirthdayImpl();
	private final Logger LOG = LoggerFactory.getLogger(VMTableWithUsersBithdayObserverImpl.class);
	private final UniformSystemEditorKit editorKid;
	
	public VMTableWithUsersBithdayObserverImpl() {
		this(null);
	}
	
	
	public VMTableWithUsersBithdayObserverImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.initWithEditorKid();
	}

	private void initWithEditorKid()  {		
		CompletableFuture.runAsync(() -> {
			final Service<Void> service = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							this.updateTitle("Загрузка");
							this.updateMessage("Загрузка данных с сервера");
							this.updateProgress(0, 10);
							final DbDataAll[] users = editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
							this.updateProgress(users.length, users.length * 2);
							this.updateMessage("Чтение данных");
							int index = 0;
							final List<UserModel> userModelList = new ArrayList<>();
							for(final DbDataAll user : users) {
								final UserModel userModel = new UserModelImpl();
								userModel.setOrganization("КолАЭР");
								userModel.setFirstName(user.getName());
								userModel.setSecondName(user.getSurname());
								userModel.setThirdName(user.getPatronymic());
								userModel.setBirthday(user.getBirthday());
								userModel.setDepartament(user.getDepartamentAbbreviated());
								userModel.setPhoneNumber(user.getPhone());
								userModel.setIcon(user.getVCard());
								userModelList.add(userModel);	
								this.updateProgress(index, users.length * 2);
								index++;
							}
							this.updateMessage("Добавление данных");
							table.setData(userModelList);
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
