package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.presenter.Access;

import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface LabelVc extends BaseView<LabelVc, BorderPane>, Access {
    void setOnCopy(Consumer<LabelVc> consumer);
    void setOnDelete(Consumer<LabelVc> consumer);
    void setOnEdit(Consumer<LabelVc> consumer);

    MLabel getMode();
}
