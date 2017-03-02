package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VSplitListContent;
import ru.kolaer.asmc.mvp.view.VSplitListContentImpl;
import ru.kolaer.asmc.tools.Application;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by danilovey on 21.02.2017.
 */
public class PSplitListContentImpl implements PSplitListContent {
    private final UniformSystemEditorKit editorKit;
    private boolean isAccess = false;
    private MLabel bufferLabel;
    private VSplitListContent view;
    private PGroupTree groupList;
    private PContentLabel contentLabel;

    public PSplitListContentImpl(UniformSystemEditorKit editorKit) {
        this.view = new VSplitListContentImpl();
        this.editorKit = editorKit;
    }

    @Override
    public void setPGroupList(PGroupTree groupList) {
        this.groupList = groupList;
        this.view.setVGroupList(groupList.getView());
    }

    @Override
    public void setPContentLabel(PContentLabel contentLabel) {
        this.contentLabel = contentLabel;
        this.view.setVContentLabel(contentLabel.getView());
    }

    @Override
    public VSplitListContent getView() {
        return this.view;
    }

    @Override
    public void setView(VSplitListContent view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.groupList.setOnSelectItem(group -> {
            this.contentLabel.clear();

            this.contentLabel.setOnAddLabel(label -> {
                group.addLabel(label);
                this.contentLabel.addPLabel(this.createLabel(label, group));
                this.groupList.getModel().saveDataOnThread();
                return null;
            });

            this.contentLabel.setOnPlaceLabel(v -> {
                if(bufferLabel != null) {
                    group.addLabel(bufferLabel);

                    this.contentLabel.addPLabel(this.createLabel(this.bufferLabel, group));
                    this.bufferLabel = null;

                    this.groupList.getModel().saveDataOnThread();
                }

                return null;
            });

            Optional.ofNullable(group.getLabelList())
                    .ifPresent(mLabels -> mLabels.stream()
                            .filter(Objects::nonNull)
                            .sorted((Comparator.comparingInt(MLabel::getPriority)))
                            .map(label -> this.createLabel(label, group))
                            .forEach(this.contentLabel::addPLabel)
                    );

            return null;
        });
    }

    private PLabel createLabel(MLabel label, MGroup group) {
        final PLabel pLabel = new PLabelImpl(label);

        pLabel.setOnDelete(labelForDel -> {
            group.getLabelList().remove(labelForDel.getModel());
            this.contentLabel.removePLabel(labelForDel);
            this.groupList.getModel().saveDataOnThread();
            return null;
        });

        pLabel.setOnEdit(label1 -> {
            this.groupList.getModel().saveDataOnThread();
            return null;
        });

        pLabel.setOnAction(label1 -> {
            final MLabel model = label1.getModel();
            new Application(model.getPathApplication(), model.getPathOpenAppWith(), this.editorKit)
                    .start();
            return null;
        });

        pLabel.setOnCopy(label1 -> {
            this.bufferLabel = new MLabel(label1.getModel());
            return null;
        });

        pLabel.setAccess(this.isAccess);

        return pLabel;
    }

    @Override
    public void setAccess(boolean access) {
        this.contentLabel.setAccess(access);
        this.groupList.setAccess(access);
        this.isAccess = access;
    }

    @Override
    public boolean isAccess() {
        return this.isAccess;
    }
}
