package ru.kolaer.asmc.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPluginAwt;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.awt.*;
import java.net.URL;
import java.util.Collection;
import java.util.function.Function;

/**
 * Created by danilovey on 18.10.2017.
 */
public class AsmcPluginAwt implements UniformSystemPluginAwt {
    private final Logger log = LoggerFactory.getLogger(AsmcPluginAwt.class);

    private UniformSystemEditorKit editorKit;
    private Panel mainPanel;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
    }

    @Override
    public void setContent(Panel content) {

    }

    @Override
    public Panel getContent() {
        return mainPanel;
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

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void initView(Function<Panel, Void> viewVisit) {
        mainPanel = new Panel(new FlowLayout());
        mainPanel.add(new Button("AAAAA"));

        viewVisit.apply(mainPanel);
    }
}
