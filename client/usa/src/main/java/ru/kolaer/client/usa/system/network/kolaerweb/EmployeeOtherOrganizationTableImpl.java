package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.kolaerweb.EmployeeOtherOrganizationTable;
import ru.kolaer.client.usa.system.network.RestTemplateService;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Реализация работы с таблицой через RESTful.
 *
 * @author danilovey
 * @version 0.1
 */
public class EmployeeOtherOrganizationTableImpl implements EmployeeOtherOrganizationTable, RestTemplateService {
	private static Logger logger = LoggerFactory.getLogger(EmployeeOtherOrganizationTableImpl.class);

	private final String URL_GET_USERS_MAX;
	private final String URL_GET_ALL;
	private final String URL_GET_USERS;
	private final String URL_GET_USERS_BY_INITIALS;
	private final String URL_GET_USERS_BIRTHDAY;
	private final String URL_GET_USERS_BIRTHDAY_TODAY;
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;

	public EmployeeOtherOrganizationTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, final String path) {
		this.objectMapper = objectMapper;
		this.restTemplate = globalRestTemplate;
		this.URL_GET_USERS = path + "/get/users";
		this.URL_GET_ALL = path + "/get/all";
		this.URL_GET_USERS_MAX = this.URL_GET_USERS + "/max";
		this.URL_GET_USERS_BIRTHDAY = this.URL_GET_USERS + "/birthday";
		this.URL_GET_USERS_BIRTHDAY_TODAY = this.URL_GET_USERS + "/birthday/today";
		this.URL_GET_USERS_BY_INITIALS = this.URL_GET_USERS + "/by/initials";
	}

	@Override
	public ServerResponse<PageDto<EmployeeOtherOrganizationDto>> getAllUser() {
		return getPageResponse(restTemplate, URL_GET_ALL, EmployeeOtherOrganizationDto.class, objectMapper);
	}

	@Override
	public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersMax(final int maxCount) {
		return getServerResponses(restTemplate, this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount),
				EmployeeOtherOrganizationDto[].class, objectMapper);
	}

	@Override
	public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(LocalDate date) {
		final SimpleStringProperty property = new SimpleStringProperty();
		property.setValue(dateTimeFormatter.format(date));

		return getServerResponses(restTemplate, this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(),
				EmployeeOtherOrganizationDto[].class,
				objectMapper);
	}

	@Override
	public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByRangeBirthday(final LocalDate dateBegin, final LocalDate dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
		final SimpleStringProperty propertyEnd = new SimpleStringProperty();
		propertyBegin.setValue(dateTimeFormatter.format(dateBegin));
		propertyEnd.setValue(dateTimeFormatter.format(dateEnd));

		return getServerResponses(restTemplate, URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(),
				EmployeeOtherOrganizationDto[].class,
				objectMapper);
	}

	@Override
	public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersBirthdayToday() {
		return getServerResponses(restTemplate, URL_GET_USERS_BIRTHDAY_TODAY,
				EmployeeOtherOrganizationDto[].class,
				objectMapper);
	}

	@Override
	public ServerResponse<Integer> getCountUsersBirthday(final LocalDate date) {
		final SimpleStringProperty property = new SimpleStringProperty();
		property.setValue(dateTimeFormatter.format(date));

		return getServerResponse(restTemplate, URL_GET_USERS_BIRTHDAY + "/" + property.getValue() + "/count",
				Integer.class,
				objectMapper);
	}

	@Override
	public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByInitials(final String initials) {
		return getServerResponses(restTemplate, URL_GET_USERS_BY_INITIALS + "/" + initials,
				EmployeeOtherOrganizationDto[].class,
				objectMapper);
	}

	@Override
	public ServerResponse insertUserList(List<EmployeeOtherOrganizationDto> userList) {
		return null;
	}

	@Override
	public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(LocalDate date, String organization) {
		final SimpleStringProperty property = new SimpleStringProperty();
		property.setValue(dateTimeFormatter.format(date));
		return getServerResponses(restTemplate, URL_GET_USERS + "/" + organization + "/birthday/" + property.getValue(),
				EmployeeOtherOrganizationDto[].class,
				objectMapper);
	}

	@Override
	public ServerResponse<Integer> getCountUsersBirthday(LocalDate date, String organization) {
		final SimpleStringProperty property = new SimpleStringProperty();
		property.setValue(dateTimeFormatter.format(date));

		return getServerResponse(restTemplate, URL_GET_USERS + "/" + organization + "/birthday/" + property.getValue() + "/count",
				Integer.class,
				objectMapper);
	}
}
