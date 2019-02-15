package ru.kolaer.birthday.mvp.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.ObserverSearch;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VSearchUsersImpl implements VSearchUsers {
	private List<ObserverSearch> observers = new ArrayList<>();
	private BorderPane mainPane;

	private void init() {
		this.mainPane = new BorderPane();

		TextField searchField = new TextField();

		Button startSearchBut = new Button("Поиск");
		startSearchBut.defaultButtonProperty().bind(searchField.focusedProperty());
		
		this.mainPane.setCenter(searchField);
		this.mainPane.setRight(startSearchBut);
		BorderPane.setAlignment(startSearchBut, Pos.CENTER);

		startSearchBut.setOnAction(e -> {
			List<UserModel> users = new ArrayList<>();

			ServerResponse<List<EmployeeDto>> dbDataAllArray = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getGeneralEmployeesTable()
					.getUsersByInitials(searchField.getText());

			if(!dbDataAllArray.isServerError()) {
				users.addAll(dbDataAllArray.getResponse().stream().map(UserModelImpl::new).collect(Collectors.toList()));
			}

			ServerResponse<List<EmployeeOtherOrganizationDto>> dbBirthdayAllArray = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getEmployeeOtherOrganizationTable()
					.getUsersByInitials(searchField.getText());

			if(!dbBirthdayAllArray.isServerError()) {
				users.addAll(dbBirthdayAllArray.getResponse().stream().map(UserModelImpl::new).collect(Collectors.toList()));
			}

			notifySearchUsers(users);
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
