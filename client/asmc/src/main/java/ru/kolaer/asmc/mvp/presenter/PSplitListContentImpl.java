package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VSplitListContent;
import ru.kolaer.asmc.mvp.view.VSplitListContentImpl;

/**
 * Created by danilovey on 21.02.2017.
 */
public class PSplitListContentImpl implements PSplitListContent {
    private VSplitListContent view;
    private PGroupList groupList;
    private PContentLabel contentLabel;

    public PSplitListContentImpl() {
        this.view = new VSplitListContentImpl();
    }

    @Override
    public void setPGroupList(PGroupList groupList) {
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
            group.getLabelList().stream()
                    .map(label -> {
                        PLabelImpl pLabel = new PLabelImpl(label);
                        pLabel.setOnDelete(labelForDel -> {
                            group.getLabelList().remove(labelForDel);
                            return null;
                        });
                        return pLabel;
                    })
                    .forEach(this.contentLabel::addPLabel);
            return null;
        });
    }
}
