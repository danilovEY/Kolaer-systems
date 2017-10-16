package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;
import ru.kolaer.client.usa.system.network.RestTemplateService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
    public ServerResponse<Page<EmployeeDto>> getAllUser() {
        return getPageResponse(restTemplate.getForEntity(this.URL_GET_ALL, String.class), objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersMax(final int maxCount) {
        return getServerResponses(restTemplate.getForEntity(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), String.class),
                EmployeeDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByBirthday(final Date date) {
        final SimpleStringProperty property = new SimpleStringProperty();
        property.setValue(dateFormat.format(date));

        return getServerResponses(restTemplate.getForEntity(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), String.class),
                EmployeeDto[].class,
                objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
        final SimpleStringProperty propertyBegin = new SimpleStringProperty();
        final SimpleStringProperty propertyEnd = new SimpleStringProperty();
        propertyBegin.setValue(dateFormat.format(dateBegin));
        propertyEnd.setValue(dateFormat.format(dateEnd));

        return getServerResponses(restTemplate.getForEntity(URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), String.class),
                EmployeeDto[].class,
                objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersBirthdayToday() {
        return getServerResponses(restTemplate.getForEntity(URL_GET_USERS_BIRTHDAY_TODAY, String.class),
                EmployeeDto[].class,
                objectMapper);
    }

    @Override
    public ServerResponse<Integer> getCountUsersBirthday(final Date date) {
        final SimpleStringProperty property = new SimpleStringProperty();
        property.setValue(dateFormat.format(date));

        return getServerResponse(restTemplate.getForEntity(URL_GET_USERS_BIRTHDAY + "/" + property.getValue() + "/count", String.class),
                Integer.class,
                objectMapper);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByInitials(final String initials) {
        return getServerResponses(restTemplate.getForEntity(URL_GET_USERS_BY_INITIALS + "/" + initials, String.class),
                EmployeeDto[].class,
                objectMapper);
    }
}
