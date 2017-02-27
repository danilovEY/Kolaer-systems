package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.view.VGroupTreeItem;
import ru.kolaer.asmc.mvp.view.VGroupTreeItemImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.02.2017.
 */
public class PGroupTreeItemImpl implements PGroupTreeItem {
    private MGroup model;
    private VGroupTreeItem view;

    public PGroupTreeItemImpl() {
        this.view = new VGroupTreeItemImpl();
    }

    public PGroupTreeItemImpl(MGroup model) {
        this();
        this.model = model;
        this.updateView();
    }

    @Override
    public MGroup getModel() {
        return this.model;
    }

    @Override
    public void setModel(MGroup model) {
        this.model = model;
    }

    @Override
    public VGroupTreeItem getView() {
        return this.view;
    }

    @Override
    public void setView(VGroupTreeItem view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.view.updateView(this.model);
    }

    @Override
    public void clear() {
        this.view.updateView(null);
    }
}
