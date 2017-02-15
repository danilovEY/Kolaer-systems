package ru.kolaer.client.javafx.system.network.kolaerweb;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Danilov on 31.07.2016.
 */
public class GeneralEmployeesTableImpl implements GeneralEmployeesTable {
    private String URL_GET_ALL;
    private final String URL_GET_USERS_MAX;
    private final String URL_GET_USERS_BY_INITIALS;
    private final String URL_GET_USERS_BIRTHDAY;
    private final String URL_GET_USERS_BIRTHDAY_TODAY;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final RestTemplate restTemplate;


    public GeneralEmployeesTableImpl(RestTemplate globalRestTemplate, String path) {
        this.restTemplate = globalRestTemplate;
        this.URL_GET_ALL = path + "/get/all";
        this.URL_GET_USERS_MAX = path + "/get/max";
        this.URL_GET_USERS_BIRTHDAY = path + "/get/birthday";
        this.URL_GET_USERS_BIRTHDAY_TODAY = path + "/get/birthday/today";
        this.URL_GET_USERS_BY_INITIALS = path + "/get/by/initials";
    }

    @Override
    public EmployeeEntity[] getAllUser() {
        EmployeeEntity[] list = restTemplate.getForObject(this.URL_GET_ALL, EmployeeEntity[].class);
        return list;
    }

    @Override
    public EmployeeEntity[] getUsersMax(final int maxCount) {
        final EmployeeEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), EmployeeEntity[].class);
        return users;
    }

    @Override
    public EmployeeEntity[] getUsersByBirthday(final Date date) {
        final SimpleStringProperty property = new SimpleStringProperty();
        property.setValue(dateFormat.format(date));

        final EmployeeEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), EmployeeEntity[].class);
        return users;
    }

    @Override
    public EmployeeEntity[] getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
        final SimpleStringProperty propertyBegin = new SimpleStringProperty();
        final SimpleStringProperty propertyEnd = new SimpleStringProperty();
        propertyBegin.setValue(dateFormat.format(dateBegin));
        propertyEnd.setValue(dateFormat.format(dateEnd));

        final EmployeeEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), EmployeeEntity[].class);
        return users;
    }

    @Override
    public EmployeeEntity[] getUsersBirthdayToday() {
        final EmployeeEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY_TODAY, EmployeeEntity[].class);
        return users;
    }

    @Override
    public int getCountUsersBirthday(final Date date) {
        final SimpleStringProperty property = new SimpleStringProperty();
        property.setValue(dateFormat.format(date));
        final Integer countUsers = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue() + "/count", Integer.class);
        return countUsers;
    }

    @Override
    public EmployeeEntity[] getUsersByInitials(final String initials) {
        if(initials == null || initials.isEmpty())
            throw new NullPointerException("Initials is null!");
        final EmployeeEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BY_INITIALS + "/" + initials, EmployeeEntity[].class);
        return users;
    }

}
