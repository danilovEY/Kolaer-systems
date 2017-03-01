package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public class VGroupTreeImpl implements VGroupTree {
    private final BorderPane mainPane;
    private final TreeView<MGroup> treeView;
    private final VGroupTreeItemImpl rootNode;
    private final MenuItem addGroup;
    private final ContextMenu contextMenu;
    private final MenuItem deleteGroup;
    private final MenuItem editGroup;

    public VGroupTreeImpl() {
        this.rootNode = new VGroupTreeItemImpl(new MGroup("КолАЭР",0));
        this.mainPane = new BorderPane();
        this.treeView = new TreeView<>(this.rootNode.getContent());
        this.addGroup = new MenuItem("Добавить группу");
        this.deleteGroup = new MenuItem("Удалить группу");
        this.editGroup = new MenuItem("Редактировать группу");
        this.contextMenu = new ContextMenu(this.addGroup, this.editGroup, this.deleteGroup);
        this.init();
    }

    private void init() {
        this.treeView.setEditable(false);
        this.treeView.setShowRoot(true);
        this.treeView.setCellFactory(call -> new TextFieldTreeCell<MGroup>() {
            @Override
            public void updateItem(MGroup item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null && !empty)
                    this.setText(item.getNameGroup());
                else
                    this.setText("");
            }
        });

        this.treeView.setContextMenu(contextMenu);
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
        this.rootNode.getContent().getChildren().add(item.getContent());
    }

    @Override
    public void removeVGroupTreeItem(VGroupTreeItem item) {
        this.rootNode.getContent().getChildren().remove(item.getContent());
    }

    @Override
    public void clear() {
        this.clearItem(this.rootNode.getContent());
    }

    @Override
    public void sort() {
        this.rootNode.getContent().getChildren()
                .sort((g1, g2) -> Integer.compare(g1.getValue().getPriority(), g2.getValue().getPriority()));
    }

    @Override
    public void setOnSelectItem(Function<MGroup, Void> function) {
        this.treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            function.apply(newValue.getValue());
        });
    }

    @Override
    public void setOnAddGroup(Function<Pair<MGroup, MGroup>, Void> function) {
        this.addGroup.setOnAction(e -> {
            final TreeItem<MGroup> selectedItem = this.treeView.getSelectionModel().getSelectedItem();
            if(selectedItem == this.rootNode.getContent()) {
                new VAddingGroupLabelsDialog().showAndWait()
                        .ifPresent(group -> function.apply(new Pair<>(null, group)));
            } else {
                new VAddingGroupLabelsDialog().showAndWait()
                        .ifPresent(group -> function.apply(new Pair<>(selectedItem.getValue(), group)));
            }
        });
    }

    @Override
    public void setOnEditGroup(Function<MGroup, Void> function) {
        this.editGroup.setOnAction(e -> {
            final TreeItem<MGroup> selectedItem = this.treeView.getSelectionModel().getSelectedItem();
            if(selectedItem != this.rootNode.getContent()) {
                new VAddingGroupLabelsDialog(selectedItem.getValue()).showAndWait()
                        .ifPresent(function::apply);
            }
        });
    }

    @Override
    public void setOnDeleteGroup(Function<Pair<MGroup, MGroup>, Void> function) {
        this.deleteGroup.setOnAction(e -> {
            final TreeItem<MGroup> selectedItem = this.treeView.getSelectionModel().getSelectedItem();
            if(selectedItem != this.rootNode.getContent()) {
                if(selectedItem.getParent() == this.rootNode.getContent())
                    function.apply(new Pair<>(null,selectedItem.getValue()));
                else
                    function.apply(new Pair<>(selectedItem.getParent().getValue(),selectedItem.getValue()));
            }
        });
    }

    private void clearItem(TreeItem<MGroup> item) {
        item.getChildren().forEach(this::clearItem);
        item.getChildren().clear();
    }
}
