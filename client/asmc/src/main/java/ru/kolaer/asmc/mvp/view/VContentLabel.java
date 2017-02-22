package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface VContentLabel extends BaseView<BorderPane> {
    void addVLabel(VLabel label);
    void removeVLabel(VLabel label);

    void clear();
}
