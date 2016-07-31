package ru.kolaer.client.javafx.system.network.kolaerweb;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.restful.DbBirthdayAll;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;
import ru.kolaer.client.javafx.system.JsonConverterSingleton;

import java.util.Date;
import java.util.List;

/**
 * Created by Danilov on 31.07.2016.
 */
public class GeneralEmployeesTableImpl implements GeneralEmployeesTable {
    private WebResource webResource;

    public GeneralEmployeesTableImpl(WebResource general) {
        this.webResource = general;
    }

    @Override
    public GeneralEmployeesEntity[] getAllUser() {
        List<GeneralEmployeesEntity> list = JsonConverterSingleton.getInstance().getEntities(this.webResource.path("get").path("all"), GeneralEmployeesEntity.class);
        return listToArray(list);
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
    public GeneralEmployeesEntity[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd) {
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

    private GeneralEmployeesEntity[] listToArray(final List<GeneralEmployeesEntity> list) {
        if(list == null || list.size() == 0) {
            return new GeneralEmployeesEntity[0];
        } else {
            final GeneralEmployeesEntity[] array = list.toArray(new GeneralEmployeesEntity[list.size()]);
            list.clear();
            return array;
        }
    }
}
