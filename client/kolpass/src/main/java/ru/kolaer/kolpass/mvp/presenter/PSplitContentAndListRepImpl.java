package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.client.core.system.UniformSystemEditorKit;
import ru.kolaer.client.core.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VSplitContentAndListRep;
import ru.kolaer.kolpass.mvp.view.VSplitContentAndListRepImpl;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 16.02.2017.
 */
public class PSplitContentAndListRepImpl implements PSplitContentAndListRep {
    private final UniformSystemEditorKit editorKit;
    private KolpassTable kolpassTable;
    private VSplitContentAndListRep view;
    private PEmployeeRepositoryList list;
    private PRepositoryContent content;

    public PSplitContentAndListRepImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.setModel(this.editorKit.getUSNetwork().getKolaerWebServer()
                .getApplicationDataBase().getKolpassTable());
        this.view = new VSplitContentAndListRepImpl();
    }

    @Override
    public VSplitContentAndListRep getView() {
        return this.view;
    }

    @Override
    public void setView(VSplitContentAndListRep view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.list.setOnSelectEmployee(repList -> {
            Optional.ofNullable(repList).ifPresent(r -> {
                this.content.clear();
                this.content.updateView();
                this.content.setEmployee(r.getKey());
                this.content.setAllRepositoryPassword(r.getValue().stream()
                        .map(rep -> new PRepositoryPasswordImpl(this.editorKit, rep))
                        .collect(Collectors.toList()));
            });

            return null;
        });

        this.list.setModel(this.kolpassTable);
        this.content.setModel(this.kolpassTable);

        this.view.setContent(this.content.getView());
        this.view.setEmployeeList(this.list.getView());
        this.list.selectIndex(0);
    }

    @Override
    public void setEmployeeList(PEmployeeRepositoryList list) {
        this.list = list;
    }

    @Override
    public void setContent(PRepositoryContent content) {
        this.content = content;
    }

    @Override
    public void clear() {
        Optional.ofNullable(this.content).ifPresent(PRepositoryContent::clear);
        Optional.ofNullable(this.list).ifPresent(PEmployeeRepositoryList::clear);
    }

    @Override
    public KolpassTable getModel() {
        return this.kolpassTable;
    }

    @Override
    public void setModel(KolpassTable model) {
        this.kolpassTable = model;
    }
}
