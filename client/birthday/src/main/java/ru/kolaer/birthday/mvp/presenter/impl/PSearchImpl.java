package ru.kolaer.birthday.mvp.presenter.impl;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.ObserverSearch;
import ru.kolaer.birthday.mvp.presenter.PSearchUsers;
import ru.kolaer.birthday.mvp.view.VSearchUsers;
import ru.kolaer.birthday.mvp.view.impl.VSearchUsersImpl;

import java.util.ArrayList;
import java.util.List;

public class PSearchImpl implements PSearchUsers {
	private List<ObserverSearch> observers = new ArrayList<>();
	private final VSearchUsers view = new VSearchUsersImpl();
	private final UniformSystemEditorKit editorKid;
	
	public PSearchImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		
		this.view.setSearchAction(e -> {
			final ServerResponse<List<EmployeeDto>> dbDataAllArray = this.editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable().getUsersByInitials(this.view.getSearchText());
			final ServerResponse<List<EmployeeOtherOrganizationDto>> dbBirthdayAllArray = this.editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getEmployeeOtherOrganizationTable().getUsersByInitials(this.view.getSearchText());

			final List<UserModel> users = new ArrayList<>();

			for(final EmployeeDto user : dbDataAllArray.getResponse()) {
				users.add(new UserModelImpl(user));
			}

			for(final EmployeeOtherOrganizationDto user : dbBirthdayAllArray.getResponse()) {
				final UserModelImpl userModel = new UserModelImpl(user);
				userModel.setOrganization(user.getOrganization());
				users.add(userModel);
			}

			this.notifySearchUsers(users);
		});
	}
	
	@Override
	public void addObserver(final ObserverSearch observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(final ObserverSearch observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifySearchUsers(final List<UserModel> users) {
		this.observers.parallelStream().forEach(obs -> obs.updateUsers(users));
	}

	@Override
	public VSearchUsers getView() {
		return this.view;
	}
}
