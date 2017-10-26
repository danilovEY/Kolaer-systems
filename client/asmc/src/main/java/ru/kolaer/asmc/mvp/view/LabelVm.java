package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.presenter.Access;

import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface LabelVm extends BaseView<LabelVm, BorderPane>, Access {
    void setOnCopy(Consumer<LabelVm> consumer);
    void setOnDelete(Consumer<LabelVm> consumer);
    void setOnEdit(Consumer<LabelVm> consumer);

    MLabel getMode();
}
