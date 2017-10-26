package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
public class VGroupTreeItemImpl implements VGroupTreeItem {
    private ImageView openIcon;
    private ImageView closeIcon;
    private TreeItem<MGroup> treeItem;

    private MGroup group;

    public VGroupTreeItemImpl(MGroup group) {
        this.group = group;
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

    @Override
    public void initView(Consumer<VGroupTreeItem> viewVisit) {
        treeItem = new TreeItem<>(group, openIcon);
        treeItem.setGraphic(getOpenImage());
        treeItem.setExpanded(true);
        treeItem.expandedProperty().addListener(e ->
                treeItem.setGraphic(treeItem.isExpanded()
                        ? getOpenImage()
                        : getCloseImage())
        );
    }

    private ImageView getOpenImage() {
        return Optional.ofNullable(openIcon)
                .orElse(openIcon = new ImageView(new Image(this.getClass().getResourceAsStream("/open-folder.png"))));
    }

    private ImageView getCloseImage() {
        return Optional.ofNullable(closeIcon)
                .orElse(closeIcon = new ImageView(new Image(this.getClass().getResourceAsStream("/close-folder.png"))));
    }
}
