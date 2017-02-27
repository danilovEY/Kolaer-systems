package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface VLabel extends BaseView<BorderPane> {
    void updateView(MLabel label);

    void setOnEdit(Function<MLabel, Void> function);
    void setOnDelete(Function<MLabel, Void> function);
    void setOnAction(Function<MLabel, Void> function);

    void setAccess(boolean access);
}
