package ru.kolaer.client.usa.mvp.view;

import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;

/**
 * Created by danilovey on 13.10.2017.
 */
public interface ApplicationUiRunner {
    void run(String[] args);

    boolean initializeUi();
    UniformSystemEditorKit initializeUniformSystemEditorKit();
    //boolean initializePlugins(UniformSystemEditorKit editorKit);

    VMainFrame getFrame();
    VTabExplorer getExplorer();

    void shutdown();
}
