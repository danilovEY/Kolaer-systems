package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.dto.PersonalPageData;

/**
 * Created by danilovey on 10.11.2016.
 */
public interface PersonalPageService {
    PersonalPageData getPersonalPageData(Integer id);
    PersonalPageData getPersonalPageData(AccountEntity accountsEntity);
}
