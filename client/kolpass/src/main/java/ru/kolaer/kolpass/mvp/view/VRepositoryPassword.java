package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.common.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface VRepositoryPassword extends BaseView<VRepositoryPassword, BorderPane> {
    void setName(String name);
    String getName();

    void setImageUrl(String url);
    String getImageUrl();

    void setLastPassword(VPasswordHistory password);
    void setFirstPassword(VPasswordHistory password);
    void setPrevPassword(VPasswordHistory password);

    VPasswordHistory getLastPassword();
    VPasswordHistory getFirstPassword();
    VPasswordHistory getPrevPassword();

    void addPasswordHistory(VPasswordHistory passwordHistory);
    void removePasswordHistory(VPasswordHistory passwordHistory);

    void setOnSaveData(Function function);
    void setOnEditName(Function<String, Void> function);
    void setOnDelete(Function function);
    void setOnReturnDefault(Function function);
}
