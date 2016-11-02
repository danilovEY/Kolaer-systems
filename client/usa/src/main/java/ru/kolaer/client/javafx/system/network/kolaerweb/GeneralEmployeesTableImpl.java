package ru.kolaer.client.javafx.system.network.kolaerweb;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
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
    private final RestTemplate restTemplate = new RestTemplate();


    public GeneralEmployeesTableImpl(String path) {
        this.URL_GET_ALL = path + "/get/all";
        this.URL_GET_USERS_MAX = path + "/get/max";
        this.URL_GET_USERS_BIRTHDAY = path + "/get/birthday";
        this.URL_GET_USERS_BIRTHDAY_TODAY = path + "/get/birthday/today";
        this.URL_GET_USERS_BY_INITIALS = path + "/get/by/initials";
    }

    @Override
    public GeneralEmployeesEntity[] getAllUser() {
        GeneralEmployeesEntity[] list = restTemplate.getForObject(this.URL_GET_ALL, GeneralEmployeesEntity[].class);
        return list;
    }

    @Override
    public GeneralEmployeesEntity[] getUsersMax(final int maxCount) {
        final GeneralEmployeesEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), GeneralEmployeesEntity[].class);
        return users;
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByBirthday(final Date date) {
        final SimpleStringProperty property = new SimpleStringProperty();
        property.setValue(dateFormat.format(date));

        final GeneralEmployeesEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), GeneralEmployeesEntity[].class);
        return users;
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
        final SimpleStringProperty propertyBegin = new SimpleStringProperty();
        final SimpleStringProperty propertyEnd = new SimpleStringProperty();
        propertyBegin.setValue(dateFormat.format(dateBegin));
        propertyEnd.setValue(dateFormat.format(dateEnd));

        final GeneralEmployeesEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), GeneralEmployeesEntity[].class);
        return users;
    }

    @Override
    public GeneralEmployeesEntity[] getUsersBirthdayToday() {
        final GeneralEmployeesEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY_TODAY, GeneralEmployeesEntity[].class);
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
    public GeneralEmployeesEntity[] getUsersByInitials(final String initials) {
        if(initials == null || initials.isEmpty())
            throw new NullPointerException("Initials is null!");
        final GeneralEmployeesEntity[] users = restTemplate.getForObject(this.URL_GET_USERS_BY_INITIALS + "/" + initials, GeneralEmployeesEntity[].class);
        return users;
    }

}
