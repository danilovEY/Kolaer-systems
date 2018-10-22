package ru.kolaer.birthday.mvp.view;

import ru.kolaer.common.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.common.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.common.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CalendarVcAll extends CalendarVcBase {
	public CalendarVcAll() {
		super("Все");
	}

	@Override
	public void notifySelectedDate(LocalDate date) {
		if (this.observerCalendar != null) {
			ServerResponse<List<EmployeeDto>> usersDataAll = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getGeneralEmployeesTable()
					.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));

			ServerResponse<List<EmployeeOtherOrganizationDto>> usersDataOtherAll = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getEmployeeOtherOrganizationTable()
					.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), organization);

			if(!usersDataAll.isServerError() && !usersDataOtherAll.isServerError()) {
				List<UserModel> users = usersDataAll
						.getResponse()
						.stream()
						.map(UserModelImpl::new)
						.collect(Collectors.toList());

				List<UserModelImpl> otherUsers = usersDataOtherAll
						.getResponse()
						.stream()
						.map(UserModelImpl::new)
						.collect(Collectors.toList());

				users.addAll(otherUsers);
				observerCalendar.updateSelectedDate(organization, date, users);
			} else {
				ServerExceptionMessage exceptionMessage;

				if(usersDataAll.isServerError()) {
					exceptionMessage = usersDataAll.getExceptionMessage();
				} else {
					exceptionMessage = usersDataOtherAll.getExceptionMessage();
				}

				UniformSystemEditorKitSingleton.getInstance()
						.getUISystemUS()
						.getNotification()
						.showErrorNotify(exceptionMessage);
			}
		}
	}

	@Override
	public void initView(Consumer<CalendarVc> viewVisit) {
		super.initView(initBaseView -> {
			initBaseView.setDayCellFactory(new CustomCallback(date -> {
				ServerResponse<Integer> countUsersBirthday = UniformSystemEditorKitSingleton
						.getInstance()
						.getUSNetwork()
						.getKolaerWebServer()
						.getApplicationDataBase()
						.getGeneralEmployeesTable()
						.getCountUsersBirthday(date);

				ServerResponse<Integer> countOtherUsersBirthday = UniformSystemEditorKitSingleton
						.getInstance()
						.getUSNetwork()
						.getKolaerWebServer()
						.getApplicationDataBase()
						.getEmployeeOtherOrganizationTable()
						.getCountUsersBirthday(date, organization);

				return countUsersBirthday.isServerError() || countOtherUsersBirthday.isServerError()
						? 0
						: countUsersBirthday.getResponse() + countOtherUsersBirthday.getResponse();
			}));

			viewVisit.accept(this);
		});
	}
}
