package ru.kolaer.asmc.mvp.presenter;

import lombok.Data;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.view.VGroupTree;
import ru.kolaer.asmc.mvp.view.VGroupTreeImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.02.2017.
 */
@Data
public class PGroupTreeImpl implements PGroupTree {
    private final Map<MGroup, PGroupTreeItem> modelPresGroupMap = new HashMap<>();
    private MGroupDataService model;
    private VGroupTree view;

    public PGroupTreeImpl() {
        this.view = new VGroupTreeImpl();
    }

    @Override
    public void updateView() {
        Optional.ofNullable(this.view).ifPresent(view -> {
            view.clear();

            this.view.setOnAddGroup(group -> {
                if(group.getKey() == null) {
                    model.addGroup(group.getValue());

                    final PGroupTreeItem pGroupTreeItem = new PGroupTreeItemImpl(group.getValue());
                    this.modelPresGroupMap.put(group.getValue(), pGroupTreeItem);
                    this.view.addVGroupTreeItem(pGroupTreeItem.getView());
                    this.view.sort();
                } else {
                    final PGroupTreeItem pGroupTreeItem = this.modelPresGroupMap.get(group.getKey());

                    final List<MGroup> groupList = Optional.ofNullable(group.getKey().getGroups())
                            .orElse(new ArrayList<>());
                    groupList.add(group.getValue());

                    final PGroupTreeItem newPGroupTreeItem = new PGroupTreeItemImpl(group.getValue());
                    this.modelPresGroupMap.put(group.getValue(), newPGroupTreeItem);
                    pGroupTreeItem.getView().addGroupTreeItem(newPGroupTreeItem.getView());
                }
                this.model.saveData();
                return null;
            });

            this.view.setOnEditGroup(group -> {
                this.view.sort();
                this.model.saveData();
                return null;
            });

            this.view.setOnDeleteGroup(group -> {
                this.view.removeVGroupTreeItem(this.modelPresGroupMap.get(group.getValue()).getView());
                if(group.getKey() != null ) {
                    group.getKey().getGroups().remove(group.getValue());
                } else {
                    this.model.removeGroup(group.getValue());
                }
                this.model.saveData();
                return null;
            });

            this.modelPresGroupMap.clear();

            this.model.getModel().forEach(group -> this.putGroup(group, null));

            this.view.sort();
        });
    }

    private void putGroup(MGroup group, PGroupTreeItemImpl pGroupTreeItem) {
        if(pGroupTreeItem == null) {
            final PGroupTreeItemImpl newPGroupTreeItem = new PGroupTreeItemImpl(group);
            this.modelPresGroupMap.put(group, newPGroupTreeItem );
            Optional.ofNullable(group.getGroups())
                    .ifPresent(groups -> groups.forEach(g -> this.putGroup(g, newPGroupTreeItem )));
            this.view.addVGroupTreeItem(newPGroupTreeItem.getView());
        } else {
            final PGroupTreeItemImpl newPGroupTreeItem = new PGroupTreeItemImpl(group);
            this.modelPresGroupMap.put(group, newPGroupTreeItem);

            pGroupTreeItem.getView().addGroupTreeItem(newPGroupTreeItem.getView());

            Optional.ofNullable(group.getGroups())
                    .ifPresent(groups -> groups.stream()
                            .forEach(g -> this.putGroup(g, newPGroupTreeItem )));
        }

    }

    @Override
    public void setOnSelectItem(Function<MGroup, Void> function) {
        Optional.ofNullable(this.view).ifPresent(view ->
            view.setOnSelectItem(function)
        );
    }
}
