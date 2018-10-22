package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface VPasswordHistory extends BaseView<VPasswordHistory, BorderPane> {
    String getDate();
    void setDate(String date);

    String getLogin();
    void setLogin(String login);

    String getPassword();
    void setPassword(String name);

    void setEditable(boolean edit);
    boolean isEditable();

    boolean isChangeData(PasswordHistoryDto passwordHistory);

    void setOnGeneratePass(Function function);
}
