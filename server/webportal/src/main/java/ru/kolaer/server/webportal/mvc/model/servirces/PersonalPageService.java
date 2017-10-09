package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.mvc.model.dto.PersonalPageDataDto;

/**
 * Created by danilovey on 10.11.2016.
 */
public interface PersonalPageService {
    PersonalPageDataDto getPersonalPageData(Integer id);
    PersonalPageDataDto getPersonalPageData(AccountDto accountsEntity);
}
