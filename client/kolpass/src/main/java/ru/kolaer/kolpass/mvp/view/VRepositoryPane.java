package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface VRepositoryPane extends BaseView<BorderPane> {
    void addRepositoryPassword(VRepositoryPassword vRepositoryPassword);
    void removeRepositoryPassword(VRepositoryPassword vRepositoryPassword);
    void clear();

    void setOnAddRepository(Function<RepositoryPassword, Void> function);
}
