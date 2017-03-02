package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VContentLabel;
import ru.kolaer.asmc.mvp.view.VContentLabelImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by danilovey on 21.02.2017.
 */
public class PContentLabelImpl implements PContentLabel {
    private final List<PLabel> labels = new ArrayList<>();
    private VContentLabel view;

    public PContentLabelImpl() {
        this.view = new VContentLabelImpl();
    }

    @Override
    public void addPLabel(PLabel label) {
        this.labels.add(label);
        this.view.addVLabel(label.getView());
    }

    @Override
    public void removePLabel(PLabel label) {
        this.labels.remove(label);
        this.view.removeVLabel(label.getView());
    }

    @Override
    public void clear() {
        this.labels.clear();
        this.view.clear();
    }

    @Override
    public void setOnAddLabel(Function<MLabel, Void> function) {
        this.view.setOnAddLabel(function);
    }

    @Override
    public void setOnPlaceLabel(Function<Void, Void> function) {
        this.view.setOnPlaceLabel(function);
    }

    @Override
    public VContentLabel getView() {
        return this.view;
    }

    @Override
    public void setView(VContentLabel view) {
        this.view = view;
    }

    @Override
    public void updateView() {

    }

    @Override
    public void setAccess(boolean access) {
        this.labels.forEach(pLabel -> pLabel.setAccess(access));
        this.view.setAccess(access);
    }

    @Override
    public boolean isAccess() {
        return this.view.isAccess();
    }
}
