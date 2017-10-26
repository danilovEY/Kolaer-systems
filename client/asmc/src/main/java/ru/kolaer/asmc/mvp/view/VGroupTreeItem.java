package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.TreeItem;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.model.MGroup;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface VGroupTreeItem extends BaseView<VGroupTreeItem, TreeItem<MGroup>> {
    void addGroupTreeItem(VGroupTreeItem item);
    void removeGroupTreeItem(VGroupTreeItem item);
    void updateView(MGroup group);
}
