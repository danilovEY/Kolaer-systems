package ru.kolaer.client.usa.mvp.view;

import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;

/**
 * Created by danilovey on 13.10.2017.
 */
public interface ApplicationUiRunner<U extends UniformSystemPlugin<T>, T> {
    void run(String[] args);

    boolean initializeUi();
    UniformSystemEditorKit initializeUniformSystemEditorKit();

    VMainFrame<T> getFrame();
    VTabExplorer<U, T> getExplorer();
    TypeUi getTypeUi();

    void shutdown();
}
