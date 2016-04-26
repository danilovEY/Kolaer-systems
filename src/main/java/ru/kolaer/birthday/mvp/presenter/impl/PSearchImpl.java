package ru.kolaer.birthday.mvp.presenter.impl;

import ru.kolaer.api.mvp.model.DbBirthdayAll;
import ru.kolaer.api.mvp.model.DbDataAll;
import ru.kolaer.api.system.ServerStatus;
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
			if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.AVAILABLE) {
				final DbDataAll[] dbDataAllArray = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersByInitials(this.view.getSearchText());
				final DbBirthdayAll[] dbBirthdayAllArray = this.editorKid.getUSNetwork().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersByInitials(this.view.getSearchText());
				
				final List<UserModel> users = new ArrayList<>();
				
				for(final DbDataAll user : dbDataAllArray) {
					users.add(new UserModelImpl(user));
				}
				
				for(final DbBirthdayAll user : dbBirthdayAllArray) {
					final UserModelImpl userModel = new UserModelImpl(user);
					userModel.setOrganization(user.getOrganization());
					users.add(userModel);
				}
				
				this.notifySearchUsers(users);
			}
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
