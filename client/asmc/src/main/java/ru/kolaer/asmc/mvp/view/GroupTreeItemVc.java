package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.TreeItem;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.client.core.mvp.view.BaseView;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface GroupTreeItemVc extends BaseView<GroupTreeItemVc, TreeItem<MGroup>> {
    void addGroupTreeItem(GroupTreeItemVc item);
    void removeGroupTreeItem(GroupTreeItemVc item);
    void updateView(MGroup group);
}
