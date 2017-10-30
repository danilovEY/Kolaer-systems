package ru.kolaer.birthday.mvp.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.ObserverSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VSearchUsersImpl implements VSearchUsers {
	private List<ObserverSearch> observers = new ArrayList<>();
	private BorderPane mainPane;

	public VSearchUsersImpl() {
		this.init();
	}
	
	private void init() {
		this.mainPane = new BorderPane();

		TextField searchField = new TextField();

		Button startSearchBut = new Button("Поиск");
		startSearchBut.defaultButtonProperty().bind(searchField.focusedProperty());
		
		this.mainPane.setCenter(searchField);
		this.mainPane.setRight(startSearchBut);
		BorderPane.setAlignment(startSearchBut, Pos.CENTER);

		startSearchBut.setOnAction(e -> {
			ServerResponse<List<EmployeeDto>> dbDataAllArray = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getGeneralEmployeesTable()
					.getUsersByInitials(searchField.getText());

			ServerResponse<List<EmployeeOtherOrganizationDto>> dbBirthdayAllArray = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getEmployeeOtherOrganizationTable()
					.getUsersByInitials(searchField.getText());

			List<UserModel> users = new ArrayList<>();

			for(EmployeeDto user : dbDataAllArray.getResponse()) {
				users.add(new UserModelImpl(user));
			}

			for(EmployeeOtherOrganizationDto user : dbBirthdayAllArray.getResponse()) {
				UserModelImpl userModel = new UserModelImpl(user);
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
	public void notifySearchUsers(List<UserModel> users) {
		this.observers.forEach(obs -> obs.updateUsers(users));
	}

	@Override
	public void initView(Consumer<VSearchUsers> viewVisit) {
		init();

		viewVisit.accept(this);
	}

	@Override
	public Parent getContent() {
		return mainPane;
	}
}
