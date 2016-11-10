package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.entities.other.PersonalPageData;

import java.util.List;

/**
 * Created by danilovey on 10.11.2016.
 */
public interface PersonalPageService {
    PersonalPageData getPersonalPageDataById(Integer id);
}
