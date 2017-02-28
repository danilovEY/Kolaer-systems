package ru.kolaer.asmc.runnable;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.model.MGroupDataServiceImpl;
import ru.kolaer.asmc.mvp.presenter.*;

import java.net.URL;
import java.util.Collection;

/**
 * Created by danilovey on 20.02.2017.
 */
public class AsmcPlugin implements UniformSystemPlugin {
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

        final MGroupDataService mGroupDataService = new MGroupDataServiceImpl();

        final PGroupTree pGroupTree = new PGroupTreeImpl();
        pGroupTree.setModel(mGroupDataService);

        final PContentLabel pContentLabel = new PContentLabelImpl();

        final PSplitListContent pSplitListContent = new PSplitListContentImpl(this.editorKit);
        pSplitListContent.setPContentLabel(pContentLabel);
        pSplitListContent.setPGroupList(pGroupTree);

        this.mainPane.setCenter(pSplitListContent.getView().getContent());

        mGroupDataService.loadData();
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
