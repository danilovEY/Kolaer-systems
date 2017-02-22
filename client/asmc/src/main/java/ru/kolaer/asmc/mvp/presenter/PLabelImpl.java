package ru.kolaer.asmc.mvp.presenter;

import lombok.Data;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VLabel;
import ru.kolaer.asmc.mvp.view.VLabelImpl;
import ru.kolaer.asmc.mvp.view.VLabelCss;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
@Data
public class PLabelImpl implements PLabel {
    private MLabel model;
    private VLabel view;

    public PLabelImpl() {
        this.view = new VLabelCss();
    }

    public PLabelImpl(MLabel model) {
        this.view = new VLabelCss();
        this.view.setAccess(true);
        this.view.setOnEdit(label -> {
            this.model = label;
            this.view.updateView(label);
            return null;
        });
        this.model = model;
        this.updateView();
    }

    @Override
    public void updateView() {
        this.view.updateView(this.model);
    }

    @Override
    public void setOnEdit(Function<MLabel, Void> function) {
        this.view.setOnEdit(label -> {
            this.model = label;
            this.view.updateView(label);
            function.apply(label);
            return null;
        });
    }

    @Override
    public void setOnDelete(Function<MLabel, Void> function) {

    }
}
