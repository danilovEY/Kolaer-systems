package ru.kolaer.birthday.mvp.view;

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

/**
 * Календарь для филиалов.
 *
 * @author danilovey
 * @version 0.1
 */
public class CalendarVcAffiliates extends CalendarVcBase {

	public CalendarVcAffiliates(String organization) {
		super(organization);
	}

	@Override
	public void notifySelectedDate(LocalDate date) {
		if (this.observerCalendar != null) {
			ServerResponse<List<EmployeeOtherOrganizationDto>> usersDataAll = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getEmployeeOtherOrganizationTable()
					.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), organization);

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
						.getEmployeeOtherOrganizationTable()
						.getCountUsersBirthday(date, organization);

				return countUsersBirthday.isServerError()
						? 0
						: countUsersBirthday.getResponse();

			}));

			viewVisit.accept(this);
		});
	}
}