package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.model.DataService;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by danilovey on 21.02.2017.
 */
public class ContentLabelVcImpl implements ContentLabelVc {
    private final List<LabelVc> labels = new ArrayList<>();
    private final DataService dataService;
    private MGroup selectedGroup;
    private MLabel bufferedLabel;
    private ContextMenu contextMenu;
    private BorderPane mainPane;
    private FlowPane contentPane;
    private ScrollPane scrollPane;

    public ContentLabelVcImpl(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void setSelectedGroup(MGroup mGroup) {
        selectedGroup = mGroup;
        labels.clear();
        contentPane.getChildren().clear();
        if(selectedGroup != null) {
            Optional.ofNullable(selectedGroup.getLabelList())
                    .orElse(Collections.emptyList())
                    .forEach(this::onlyAddLabel);
            sort();
        }
    }

    private void onlyAddLabel(MLabel mLabel) {
        LabelVc vLabelCss = new LabelVcCss(mLabel);
        vLabelCss.initView(initLabel -> {
            contentPane.getChildren().add(initLabel.getContent());

            initLabel.setOnCopy(label -> bufferedLabel = label.getMode());
            initLabel.setOnEdit(label -> {
                sort();
                dataService.saveData();
            });
            initLabel.setOnDelete(label -> {
                selectedGroup.getLabelList().remove(label.getMode());
                contentPane.getChildren().remove(label.getContent());
                dataService.saveData();
            });
            vLabelCss.setAccess(isAccess());
        });

        labels.add(vLabelCss);
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void setAccess(boolean access) {
        scrollPane.setContextMenu(access ? this.contextMenu : null);
        labels.forEach(label -> label.setAccess(access));
    }

    @Override
    public boolean isAccess() {
        return this.scrollPane.getContextMenu() != null;
    }

    private void addLabel(MLabel mLabel) {
        if(selectedGroup != null) {
            List<MLabel> mLabels = Optional
                    .ofNullable(selectedGroup.getLabelList())
                    .orElse(new ArrayList<>());

            mLabels.add(mLabel);
            selectedGroup.setLabelList(mLabels);

            dataService.saveData();

            onlyAddLabel(mLabel);

            sort();
        }
    }

    @Override
    public void initView(Consumer<ContentLabelVc> viewVisit) {
        mainPane = new BorderPane();
        contentPane = new FlowPane();
        scrollPane = new ScrollPane(this.contentPane);
        MenuItem addLabel = new MenuItem("Добавить ярлык");
        MenuItem placeLabel = new MenuItem("Вставить ярлык");
        contextMenu = new ContextMenu(addLabel, new SeparatorMenuItem(), placeLabel);

        contentPane.setStyle("-fx-background-image: url(" +
                this.getClass().getResource("/background-repiat.jpg").toString() + ");");
        contentPane.setAlignment(Pos.TOP_CENTER);
        contentPane.setPadding(new Insets(5,5,5,5));
        contentPane.setVgap(5);
        contentPane.setHgap(5);

        scrollPane.setContextMenu(contextMenu);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        mainPane.setCenter(scrollPane);

        addLabel.setOnAction(e ->
                AddingLabelDialogVc.showAndWait().ifPresent(this::addLabel)
        );

        placeLabel.setOnAction(e -> {
            if(bufferedLabel != null) {
                addLabel(new MLabel(bufferedLabel));
            }
        });

        viewVisit.accept(this);
    }

    private void sort() {
        Tools.runOnWithOutThreadFX(() -> {
            labels.sort(Comparator.comparing(label -> label.getMode().getPriority()));
            contentPane.getChildren().clear();
            labels.stream().map(BaseView::getContent).forEach(contentPane.getChildren()::add);
        });
    }

    @Override
    public void updateData(List<MGroup> groupList) {
        Tools.runOnWithOutThreadFX(() -> setSelectedGroup(null));
    }

    @Override
    public void saveData() {

    }
}
