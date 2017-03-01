package ru.kolaer.asmc.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.model.MGroupDataServiceImpl;
import ru.kolaer.asmc.mvp.presenter.*;
import ru.kolaer.asmc.mvp.view.ImageViewPane;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Created by danilovey on 20.02.2017.
 */
public class AsmcPlugin implements UniformSystemPlugin {
    private final static Logger log = LoggerFactory.getLogger(AsmcPlugin.class);
    private UniformSystemEditorKit editorKit;
    private BorderPane mainPane;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
    }

    @Override
    public URL getIcon() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return null;
    }

    @Override
    public void start() throws Exception {
        this.mainPane = new BorderPane();

        final PGroupTree pGroupTree = new PGroupTreeImpl(this.editorKit);

        CompletableFuture.runAsync(() -> {
            final MGroupDataService mGroupDataService = new MGroupDataServiceImpl(this.editorKit);
            if(mGroupDataService.loadData()) {
                pGroupTree.setModel(mGroupDataService);
                Tools.runOnWithOutThreadFX(pGroupTree::updateView);
            }
        }, Executors.newSingleThreadExecutor()).exceptionally(t -> {
            log.error("Ошибка при чтении данных!", t);
            this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Ошибка при чтении данных!");
            return null;
        });

        final PContentLabel pContentLabel = new PContentLabelImpl();

        final PSplitListContent pSplitListContent = new PSplitListContentImpl(this.editorKit);
        pSplitListContent.setPContentLabel(pContentLabel);
        pSplitListContent.setPGroupList(pGroupTree);
        pSplitListContent.updateView();

        this.mainPane.setCenter(pSplitListContent.getView().getContent());

        this.updateBanner();
    }

    private void updateBanner() {
        Tools.runOnThreadFX(() -> {
            final BorderPane imagePane = new BorderPane();
            imagePane.setStyle("-fx-background-color: #FFFFFF"); //,linear-gradient(#f8f8f8, #e7e7e7);
            imagePane.setMaxHeight(300);
            imagePane.setMaxWidth(Double.MAX_VALUE);

            final ImageView left = new ImageView(new Image(this.getClass().getResource("/LR.png").toString(), true));
            left.setPreserveRatio(false);

            final ImageView right = new ImageView(new Image(this.getClass().getResource("/LR.png").toString(), true));
            right.setPreserveRatio(false);

            final ImageViewPane center = new ImageViewPane(new ImageView(new Image(this.getClass().getResource("/Centr.png").toString(), true)));

            imagePane.setRight(right);
            imagePane.setLeft(left);
            imagePane.setCenter(center);

            this.mainPane.setTop(imagePane);
        });
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }
}
