package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public class VGroupTreeItemImpl implements VGroupTreeItem {
    private final ImageView openIcon =
            new ImageView(new Image(this.getClass().getResourceAsStream("/open-folder.png")));

    private final ImageView closeIcon =
            new ImageView(new Image(this.getClass().getResourceAsStream("/close-folder.png")));

    private TreeItem<MGroup> treeItem;

    public VGroupTreeItemImpl(MGroup group) {
        this.treeItem = new TreeItem<>(group, this.openIcon);
        this.init();
    }

    public VGroupTreeItemImpl() {
        this.treeItem = new TreeItem<>();
        this.init();
    }

    private void init() {
        this.treeItem.setGraphic(this.openIcon);
        this.treeItem.setExpanded(true);
        this.treeItem.expandedProperty().addListener(e ->
            this.treeItem.setGraphic(this.treeItem.isExpanded()
                    ? this.openIcon
                    : this.closeIcon)
        );
    }

    @Override
    public void setContent(TreeItem<MGroup> content) {
        content.getChildren().addAll(this.treeItem.getChildren());
        this.treeItem = content;
    }

    @Override
    public TreeItem<MGroup> getContent() {
        return this.treeItem;
    }

    @Override
    public void addGroupTreeItem(VGroupTreeItem item) {
        this.treeItem.getChildren().add(item.getContent());
    }

    @Override
    public void removeGroupTreeItem(VGroupTreeItem item) {
        this.treeItem.getChildren().remove(item.getContent());
    }

    @Override
    public void updateView(MGroup group) {
        this.treeItem.setValue(group);
    }
}
