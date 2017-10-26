package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.mvp.model.DataService;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 20.02.2017.
 */
public class GroupTreeVcImpl implements GroupTreeVc {
    private DataService model;

    private BorderPane mainPane;
    private TreeView<MGroup> treeView;
    private VGroupTreeItemImpl rootNode;
    private MenuItem addGroup;
    private ContextMenu contextMenu;
    private MenuItem deleteGroup;
    private MenuItem editGroup;
    private MenuItem copyGroup;
    private MenuItem placeGroup;

    private MGroup bufferGroup;

    public GroupTreeVcImpl(DataService dataService) {
        this.model = dataService;
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void setOnSelectItem(Consumer<MGroup> consumer) {
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        consumer.accept(newValue.getValue())
                );
    }

    @Override
    public void sort() {
        this.rootNode.getContent().getChildren()
                .sort(Comparator.comparingInt(g -> g.getValue().getPriority()));
    }

    @Override
    public void setAccess(boolean access) {
        this.treeView.setContextMenu(access ? this.contextMenu : null);
    }

    @Override
    public boolean isAccess() {
        return this.treeView.getContextMenu() != null;
    }

    private void onPlace(TreeItem<MGroup> selectedItem) {
        if(this.bufferGroup != null) {
            MGroup bufferGroupModel = new MGroup(bufferGroup);

            if(selectedItem.getValue() == rootNode.getContent().getValue()) {
                model.addGroup(bufferGroupModel);

                VGroupTreeItem pGroupTreeItem = new VGroupTreeItemImpl(bufferGroupModel);
                pGroupTreeItem.initView(initVew -> rootNode.getContent().getChildren()
                        .add(initVew.getContent()));
            } else {
                MGroup selectGroup = selectedItem.getValue();

                List<MGroup> groupList = Optional.ofNullable(selectGroup.getGroups())
                        .orElse(new ArrayList<>());

                groupList.add(bufferGroupModel);

                selectGroup.setGroups(groupList);


                VGroupTreeItem pGroupTreeItem = new VGroupTreeItemImpl(bufferGroupModel);
                pGroupTreeItem.initView(initVew -> selectedItem.getChildren()
                        .add(initVew.getContent()));
            }

            bufferGroup = null;
            model.saveDataOnThread();
            sort();
        }
    }

    private void editGroup(MGroup childrenGroup) {
        if(childrenGroup == null) {
            return;
        }

        model.saveDataOnThread();
    }

    private void deleteGroup(TreeItem<MGroup> selectedItem) {
        selectedItem.getParent().getChildren().remove(selectedItem);
        model.removeGroup(selectedItem.getValue());
        model.saveDataOnThread();
    }

    private void addGroup(MGroup childrenGroup) {
        MGroup parentGroup = treeView.getSelectionModel().getSelectedItem().getValue();

        if(parentGroup == null) {
            VGroupTreeItem pGroupTreeItem = new VGroupTreeItemImpl(childrenGroup);
            pGroupTreeItem.initView(initGroupTree -> rootNode.getContent().getChildren()
                    .add(initGroupTree.getContent()));
        } else {
            VGroupTreeItem pGroupTreeItem = new VGroupTreeItemImpl(childrenGroup);

            List<MGroup> groupList = Optional.ofNullable(parentGroup.getGroups())
                    .orElse(new ArrayList<>());
            groupList.add(childrenGroup);
            parentGroup.getGroups().addAll(groupList);


            VGroupTreeItem newPGroupTreeItem = new VGroupTreeItemImpl(childrenGroup);
            pGroupTreeItem.addGroupTreeItem(newPGroupTreeItem);
        }
        this.sort();

        this.model.saveDataOnThread();
    }

    @Override
    public void initView(Consumer<GroupTreeVc> viewVisit) {
        rootNode = new VGroupTreeItemImpl(new MGroup("КолАЭР",0));
        mainPane = new BorderPane();
        treeView = new TreeView<>(this.rootNode.getContent());
        addGroup = new MenuItem("Добавить группу");
        deleteGroup = new MenuItem("Удалить группу");
        editGroup = new MenuItem("Редактировать группу");
        copyGroup = new MenuItem("Копировать группу");
        placeGroup = new MenuItem("Вставить группу");
        contextMenu = new ContextMenu(addGroup, editGroup, deleteGroup,
                new SeparatorMenuItem(), copyGroup, placeGroup);

        init();

        viewVisit.accept(this);
    }

    private void init() {
        treeView.setEditable(false);
        treeView.setShowRoot(true);
        treeView.setContextMenu(contextMenu);
        treeView.setCellFactory(call -> new TextFieldTreeCell<MGroup>() {
            @Override
            public void updateItem(MGroup item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null && !empty)
                    this.setText(item.getNameGroup());
                else
                    this.setText("");
            }
        });

        mainPane.setCenter(treeView);

        addGroup.setOnAction(e -> VAddingGroupLabelsDialog
                .showAndWait()
                .ifPresent(this::addGroup));

        editGroup.setOnAction(e -> VAddingGroupLabelsDialog
                .showAndWait(treeView.getSelectionModel().getSelectedItem().getValue())
                .ifPresent(this::editGroup));

        deleteGroup.setOnAction(e -> deleteGroup(treeView.getSelectionModel().getSelectedItem()));

        copyGroup.setOnAction(e -> {
            TreeItem<MGroup> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem != rootNode.getContent()) {
                bufferGroup = selectedItem.getValue();
            }
        });

        placeGroup.setOnAction(e -> onPlace(treeView.getSelectionModel().getSelectedItem()));
    }

    @Override
    public void updateData(List<MGroup> groupList) {

    }
}
