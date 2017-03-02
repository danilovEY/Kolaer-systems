package ru.kolaer.asmc.mvp.presenter;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.view.VGroupTree;
import ru.kolaer.asmc.mvp.view.VGroupTreeImpl;
import ru.kolaer.asmc.mvp.view.VGroupTreeItem;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.02.2017.
 */
@Data
public class PGroupTreeImpl implements PGroupTree {
    private static final Logger log = LoggerFactory.getLogger(PGroupTreeImpl.class);
    private final Map<MGroup, PGroupTreeItem> modelPresGroupMap = new HashMap<>();
    private final UniformSystemEditorKit editorKit;
    private MGroup bufferGroup;
    private MGroupDataService model;
    private VGroupTree view;

    public PGroupTreeImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
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
                } else {
                    final PGroupTreeItem pGroupTreeItem = this.modelPresGroupMap.get(group.getKey());
                    this.modelPresGroupMap.remove(group.getKey());

                    final List<MGroup> groupList = Optional.ofNullable(group.getKey().getGroups())
                            .orElse(new ArrayList<>());
                    groupList.add(group.getValue());
                    group.getKey().setGroups(groupList);

                    this.modelPresGroupMap.put(group.getKey(), pGroupTreeItem);

                    final PGroupTreeItem newPGroupTreeItem = new PGroupTreeItemImpl(group.getValue());
                    this.modelPresGroupMap.put(group.getValue(), newPGroupTreeItem);
                    pGroupTreeItem.getView().addGroupTreeItem(newPGroupTreeItem.getView());
                }
                this.view.sort();

                this.model.saveDataOnThread();
                return null;
            });

            this.view.setOnEditGroup(group -> {
                this.view.sort();
                this.model.saveDataOnThread();
                return null;
            });

            this.view.setOnDeleteGroup(group -> {
                if(group.getKey() != null ) {
                    final PGroupTreeItem groupToRemove = this.modelPresGroupMap.get(group.getValue());
                    final PGroupTreeItem groupFromRemove = this.modelPresGroupMap.get(group.getKey());
                    groupFromRemove.getView().removeGroupTreeItem(groupToRemove.getView());
                    this.modelPresGroupMap.remove(group.getKey());
                    group.getKey().getGroups().remove(group.getValue());
                    this.modelPresGroupMap.put(group.getKey(), groupFromRemove);
                } else {
                    this.view.removeVGroupTreeItem(this.modelPresGroupMap.get(group.getValue()).getView());
                    this.model.removeGroup(group.getValue());
                    this.modelPresGroupMap.remove(group.getValue());
                }
                this.model.saveDataOnThread();
                return null;
            });

            this.view.setOnCopyItem(mGroup -> {
                this.bufferGroup = new MGroup(mGroup);
                return null;
            });

            this.view.setOnPlaceItem(mGroup -> {
                if(this.bufferGroup != null) {
                    if(mGroup == null) {
                        model.addGroup(this.bufferGroup);

                        final PGroupTreeItem pGroupTreeItem = new PGroupTreeItemImpl(this.bufferGroup);
                        this.modelPresGroupMap.put(this.bufferGroup, pGroupTreeItem);
                        this.view.addVGroupTreeItem(pGroupTreeItem.getView());
                    } else {
                        final PGroupTreeItem pGroupTreeItem = this.modelPresGroupMap.get(mGroup);
                        this.modelPresGroupMap.remove(mGroup);

                        final List<MGroup> groupList = Optional.ofNullable(mGroup.getGroups())
                                .orElse(new ArrayList<>());
                        groupList.add(this.bufferGroup);
                        mGroup.setGroups(groupList);

                        this.modelPresGroupMap.put(mGroup, pGroupTreeItem);

                        this.putGroup(this.bufferGroup, pGroupTreeItem);
                    }

                    this.view.sort();
                    this.bufferGroup = null;

                    this.model.saveDataOnThread();
                }

                return null;
            });

            this.modelPresGroupMap.clear();

            this.model.getModel().forEach(group -> this.putGroup(group, null));

            this.view.sort();
        });
    }

    private void putGroup(MGroup group, PGroupTreeItem pGroupTreeItem) {
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
                    .ifPresent(groups -> groups.forEach(g -> this.putGroup(g, newPGroupTreeItem )));
        }

    }

    @Override
    public void setOnSelectItem(Function<MGroup, Void> function) {
        Optional.ofNullable(this.view).ifPresent(view ->
            view.setOnSelectItem(function)
        );
    }
}
