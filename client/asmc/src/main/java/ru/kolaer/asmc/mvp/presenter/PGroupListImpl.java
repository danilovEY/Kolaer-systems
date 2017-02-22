package ru.kolaer.asmc.mvp.presenter;

import lombok.Data;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.view.VGroupTree;
import ru.kolaer.asmc.mvp.view.VGroupTreeImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.02.2017.
 */
@Data
public class PGroupListImpl implements PGroupList {
    private final Map<MGroup, PGroupTreeItem> modelPresGroupMap = new HashMap<>();
    private MGroupDataService model;
    private VGroupTree view;

    public PGroupListImpl() {
        this.view = new VGroupTreeImpl();
    }

    @Override
    public void updateView() {
        Optional.ofNullable(this.view).ifPresent(view -> {
            view.clear();
            this.modelPresGroupMap.clear();

            this.modelPresGroupMap.putAll(this.model.getModel().stream()
                    .collect(Collectors.toMap(g -> g, PGroupTreeItemImpl::new)));

            this.modelPresGroupMap.values().stream()
                    .sorted((group1, group2) ->
                            Integer.compare(group1.getModel().getPriority(), group2.getModel().getPriority()))
                    .map(PGroupTreeItem::getView)
                    .forEach(this.view::addVGroupTreeItem);
        });
    }

    @Override
    public void setOnSelectItem(Function<MGroup, Void> function) {
        Optional.ofNullable(this.view).ifPresent(view ->
            view.setOnSelectItem(function)
        );
    }
}
