package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.presenter.Access;

import java.util.function.Function;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface VContentLabel extends BaseView<BorderPane>, Access {
    void addVLabel(VLabel label);
    void removeVLabel(VLabel label);

    void clear();

    void setOnAddLabel(Function<MLabel, Void> function);
    void setOnPlaceLabel(Function<Void, Void> function);
}
