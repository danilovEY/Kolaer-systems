package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public class VGroupTreeImpl implements VGroupTree {
    private BorderPane mainPane;
    private TreeView<MGroup> treeView;
    private final TreeItem<MGroup> rootNode;

    public VGroupTreeImpl() {
        this.rootNode = new TreeItem<>(new MGroup("КолАЭР",0));
        this.mainPane = new BorderPane();
        this.treeView = new TreeView<>(this.rootNode);
        this.init();
    }

    private void init() {
        this.treeView.setEditable(false);
        this.treeView.setShowRoot(false);
        this.treeView.setCellFactory(call -> new TextFieldTreeCell<MGroup>() {
            @Override
            public void updateItem(MGroup item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null)
                    this.setText(item.getNameGroup());
                else
                    this.setText("");
            }
        });

        this.mainPane.setCenter(this.treeView);
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void addVGroupTreeItem(VGroupTreeItem item) {
        this.rootNode.getChildren().add(item.getContent());
    }

    @Override
    public void removeVGroupTreeItem(VGroupTreeItem item) {
        this.rootNode.getChildren().remove(item.getContent());
    }

    @Override
    public void clear() {
        this.clearItem(this.rootNode);
    }

    @Override
    public void setOnSelectItem(Function<MGroup, Void> function) {
        this.treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            function.apply(newValue.getValue());
        });
    }

    private void clearItem(TreeItem<MGroup> item) {
        item.getChildren().forEach(this::clearItem);
        item.getChildren().clear();
    }
}
