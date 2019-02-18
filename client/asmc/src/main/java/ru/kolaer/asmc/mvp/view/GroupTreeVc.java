package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.mvp.model.DataServiceObserver;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.presenter.Access;
import ru.kolaer.client.core.mvp.view.BaseView;

import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface GroupTreeVc extends BaseView<GroupTreeVc, BorderPane>, DataServiceObserver, Access {
    void setOnSelectItem(Consumer<MGroup> consumer);
    void sort();
}
