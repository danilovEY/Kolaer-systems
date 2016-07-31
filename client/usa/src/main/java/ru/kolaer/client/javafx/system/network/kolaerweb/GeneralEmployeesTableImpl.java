package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;

import java.util.Date;

/**
 * Created by Danilov on 31.07.2016.
 */
public class GeneralEmployeesTableImpl implements GeneralEmployeesTable {
    private String URL_GET_ALL;
    private final RestTemplate restTemplate = new RestTemplate();


    public GeneralEmployeesTableImpl(String path) {
        this.URL_GET_ALL = path + "/get/all";
    }

    @Override
    public GeneralEmployeesEntity[] getAllUser() {
        GeneralEmployeesEntity[] list = restTemplate.getForObject(this.URL_GET_ALL, GeneralEmployeesEntity[].class);
        return list;
    }

    @Override
    public GeneralEmployeesEntity[] getUsersMax(int maxCount) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByBirthday(Date date) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersBirthdayToday() {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByInitials(String initials) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public int getCountUsersBirthday(Date date) {
        return 0;
    }

}
