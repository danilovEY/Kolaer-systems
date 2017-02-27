package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VContentLabel;
import ru.kolaer.asmc.mvp.view.VContentLabelImpl;

import java.util.function.Function;

/**
 * Created by danilovey on 21.02.2017.
 */
public class PContentLabelImpl implements PContentLabel {
    private VContentLabel view;

    public PContentLabelImpl() {
        this.view = new VContentLabelImpl();
    }

    @Override
    public void addPLabel(PLabel label) {
        this.view.addVLabel(label.getView());
    }

    @Override
    public void removePLabel(PLabel label) {
        this.view.removeVLabel(label.getView());
    }

    @Override
    public void clear() {
        this.view.clear();
    }

    @Override
    public void setOnAddLabel(Function<MLabel, Void> function) {
        this.view.setOnAddLabel(function);
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
}
