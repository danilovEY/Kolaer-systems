package ru.kolaer.client.usa.mvp.view.awt;

import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.usa.mvp.view.AbstractApplicationUiRunner;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;

import java.awt.*;

/**
 * Created by danilovey on 13.10.2017.
 */
public class AwtUiRunner extends AbstractApplicationUiRunner {

    @Override
    public boolean initializeUi() {
        Frame frame = new Frame();

        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.add(new Button("Button"), BorderLayout.CENTER);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
        return true;
    }

    @Override
    public UniformSystemEditorKit initializeUniformSystemEditorKit() {
        return null;
    }

    @Override
    public VMainFrame getFrame() {
        return null;
    }

    @Override
    public VTabExplorer getExplorer() {
        return null;
    }

    @Override
    public void shutdown() {

    }
}
