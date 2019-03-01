package ru.kolaer.birthday.mvp.view;

import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Presenter календаря КолАЕРа.
 *
 * @author danilovey
 * @version 0.1
 */
public class CalendarVcKAER extends CalendarVcBase {
	
	public CalendarVcKAER() {
		super("КолАтомэнергоремонт");
	}

	@Override
	public void notifySelectedDate(LocalDate date) {
		if (this.observerCalendar != null) {

			ServerResponse<List<EmployeeDto>> usersDataAll = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getGeneralEmployeesTable()
					.getUsersByBirthday(date);

			if(!usersDataAll.isServerError()) {
				List<UserModel> users = usersDataAll
						.getResponse()
						.stream()
						.map(UserModelImpl::new)
						.collect(Collectors.toList());
				observerCalendar.updateSelectedDate(organization, date, users);
			} else {
				UniformSystemEditorKitSingleton.getInstance()
						.getUISystemUS()
						.getNotification()
						.showErrorNotify(usersDataAll.getExceptionMessage());
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

				return countUsersBirthday.isServerError()
						? 0
						: countUsersBirthday.getResponse();
			}));

			viewVisit.accept(this);
		});
	}
}