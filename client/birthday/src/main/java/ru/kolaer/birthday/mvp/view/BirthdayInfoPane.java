package ru.kolaer.birthday.mvp.view;

import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.common.system.ui.StaticView;

import java.util.List;

/**
 * Created by danilovey on 31.10.2017.
 */
public interface BirthdayInfoPane extends StaticView{
    void put(String title, List<UserModel> users);
}
