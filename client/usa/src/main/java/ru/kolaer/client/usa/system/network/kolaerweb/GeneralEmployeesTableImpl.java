package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.kolaerweb.GeneralEmployeesTable;
import ru.kolaer.client.usa.system.network.RestTemplateService;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Danilov on 31.07.2016.
 */
public class GeneralEmployeesTableImpl implements GeneralEmployeesTable, RestTemplateService {
    private String URL_GET_ALL;
    private final String URL_GET_USERS_MAX;
    private final String URL_GET_USERS_BY_INITIALS;
    private final String URL_GET_USERS_BIRTHDAY;
    private final String URL_GET_USERS_BIRTHDAY_TODAY;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    public GeneralEmployeesTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;
        this.URL_GET_ALL = path + "/get/all";
        this.URL_GET_USERS_MAX = path + "/get/max";
        this.URL_GET_USERS_BIRTHDAY = path + "/get/birthday";
        this.URL_GET_USERS_BIRTHDAY_TODAY = path + "/get/birthday/today";
        this.URL_GET_USERS_BY_INITIALS = path + "/get/by/initials";
    }

    @Override
    public ServerResponse<PageDto<EmployeeDto>> getAllUser() {
        return getPageResponse(restTemplate, this.URL_GET_ALL, EmployeeDto.class, objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersMax(final int maxCount) {
        return getServerResponses(restTemplate, this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), 
                EmployeeDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByBirthday(final LocalDate date) {
        final SimpleStringProperty property = new SimpleStringProperty();
        property.setValue(dateTimeFormatter.format(date));

        return getServerResponses(restTemplate, this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(),
                EmployeeDto[].class,
                objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByRangeBirthday(final LocalDate dateBegin, final LocalDate dateEnd) {
        final SimpleStringProperty propertyBegin = new SimpleStringProperty();
        final SimpleStringProperty propertyEnd = new SimpleStringProperty();
        propertyBegin.setValue(dateTimeFormatter.format(dateBegin));
        propertyEnd.setValue(dateTimeFormatter.format(dateEnd));

        return getServerResponses(restTemplate, URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), 
                EmployeeDto[].class,
                objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersBirthdayToday() {
        return getServerResponses(restTemplate, URL_GET_USERS_BIRTHDAY_TODAY, 
                EmployeeDto[].class,
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
    public ServerResponse<List<EmployeeDto>> getUsersByInitials(final String initials) {
        return getServerResponses(restTemplate, URL_GET_USERS_BY_INITIALS + "/" + initials, 
                EmployeeDto[].class,
                objectMapper);
    }
}
