package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface VGroupTree extends BaseView<BorderPane> {
    void addVGroupTreeItem(VGroupTreeItem item);
    void removeVGroupTreeItem(VGroupTreeItem item);
    void clear();

    void setOnSelectItem(Function<MGroup, Void> function);
}
