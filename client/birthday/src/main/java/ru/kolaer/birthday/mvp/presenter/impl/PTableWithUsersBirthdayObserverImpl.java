package ru.kolaer.birthday.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.PSearchUsers;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBirthdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Presenter таблици с сотрудниками.
 *
 * @author danilovey
 * @version 0.1
 */
public class PTableWithUsersBirthdayObserverImpl implements PTableWithUsersBirthdayObserver{
	private final Logger LOG = LoggerFactory.getLogger(PTableWithUsersBirthdayObserverImpl.class);
	/**View таблици.*/
	private final VTableWithUsersBirthday table = new VTableWithUsersBirthdayImpl();
	private final UniformSystemEditorKit editorKid;
	
	public PTableWithUsersBirthdayObserverImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.initWithEditorKid();
	}

	private void initWithEditorKid()  {	
		
		this.table.setMouseClick(e -> {
			if(e.getClickCount() == 2) {
				new VMDetailedInformationStageImpl(this.table.getSelectModel()).show();
			}
		});
		
		this.showTodayBirthday();
		
		final PSearchUsers searchUsers = new PSearchImpl(this.editorKid);
		this.table.addSearch(searchUsers.getView());
		searchUsers.addObserver(this);
	}

	@Override
	public VTableWithUsersBirthday getView() {
		return this.table;
	}

	@Override
	public void updateSelectedDate(final LocalDate date, final List<UserModel> users) {
		if(users.size() > 0) {
			this.table.setData(users);
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			this.table.setTitle("\"" + date.format(formatter) + "\" день рождения у:");
		} else {
			this.table.clear();
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			this.table.setTitle("\"" + date.format(formatter) + "\" именинники отсутствуют!");
			this.table.setNoContentText("\"" + date.format(formatter) + "\" именинники отсутствуют!");
		}
	}


	@Override
	public void showTodayBirthday() {
		CompletableFuture.runAsync(() -> {
			final EmployeeEntity[] usersKolaer = editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable().getUsersBirthdayToday();
			for(final EmployeeEntity user : usersKolaer) {
				final UserModel userModel = new UserModelImpl(user);
				table.addData(userModel);
			}

			final EmployeeOtherOrganization[] usersOther = editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getEmployeeOtherOrganizationTable().getUsersBirthdayToday();
			for(final EmployeeOtherOrganization user : usersOther) {
				final UserModel userModel = new UserModelImpl(user);
				userModel.setOrganization(user.getOrganization());
				table.addData(userModel);
			}
		});
	}

	@Override
	public void updateUsers(List<UserModel> users) {
		this.table.setData(users);
	}
}