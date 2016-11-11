package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.server.webportal.mvc.model.entities.other.PersonalPageData;

/**
 * Created by danilovey on 10.11.2016.
 */
public interface PersonalPageService {
    PersonalPageData getPersonalPageData(Integer id);
    PersonalPageData getPersonalPageData(GeneralAccountsEntity accountsEntity);
}
