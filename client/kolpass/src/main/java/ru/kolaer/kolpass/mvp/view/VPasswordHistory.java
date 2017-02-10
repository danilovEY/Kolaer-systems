package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface VPasswordHistory extends BaseView<BorderPane> {
    String getDate();
    void setDate(String date);

    String getLogin();
    void setLogin(String login);

    String getPassword();
    void setPassword(String name);

    void setEditable(boolean edit);
    boolean isEditable();
}
