package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.common.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface VRepositoryContent extends BaseView<VRepositoryContent, BorderPane> {
    void addRepositoryPassword(VRepositoryPassword vRepositoryPassword);
    void removeRepositoryPassword(VRepositoryPassword vRepositoryPassword);
    void clear();
    void setContextMenu(boolean enable);

    void setOnAddRepository(Function<PasswordRepositoryDto, Void> function);
}
