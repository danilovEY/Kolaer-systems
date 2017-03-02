package ru.kolaer.asmc.mvp.presenter;

import lombok.Data;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VLabel;
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
        this.model = model;
        this.updateView();
    }

    @Override
    public void updateView() {
        this.view.updateView(this.model);
    }

    @Override
    public void setOnEdit(Function<PLabel, Void> function) {
        this.view.setOnEdit(label -> {
            this.model = label;
            this.view.updateView(label);
            function.apply(this);
            return null;
        });
    }

    @Override
    public void setOnDelete(Function<PLabel, Void> function) {
        this.view.setOnDelete(label ->
            function.apply(this)
        );
    }

    @Override
    public void setOnAction(Function<PLabel, Void> function) {
        this.view.setOnAction(label ->
            function.apply(this)
        );
    }

    @Override
    public void setOnCopy(Function<PLabel, Void> function) {
        this.view.setOnCopy(mLabel ->
            function.apply(this)
        );
    }

    @Override
    public void setAccess(boolean access) {
        this.view.setAccess(access);
    }

    @Override
    public boolean isAccess() {
        return this.view.isAccess();
    }
}
