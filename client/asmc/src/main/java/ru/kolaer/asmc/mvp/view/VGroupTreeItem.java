package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.TreeItem;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface VGroupTreeItem extends BaseView<TreeItem<MGroup>> {
    void addGroupTreeItem(VGroupTreeItem item);
    void removeGroupTreeItem(VGroupTreeItem item);
    void updateView(MGroup group);
}
