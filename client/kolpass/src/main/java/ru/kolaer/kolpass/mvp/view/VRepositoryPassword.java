package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface VRepositoryPassword extends BaseView<BorderPane> {
    void setName(String name);
    String getName();

    void setLastPassword(VPasswordHistory password);
    void setFirstPassword(VPasswordHistory password);
    void setPrevPassword(VPasswordHistory password);

    VPasswordHistory getLastPassword();
    VPasswordHistory getFirstPassword();
    VPasswordHistory getPrevPassword();

    void addPasswordHistory(VPasswordHistory passwordHistory);
    void removePasswordHistory(VPasswordHistory passwordHistory);
}
