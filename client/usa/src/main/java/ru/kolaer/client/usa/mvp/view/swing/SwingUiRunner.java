package ru.kolaer.client.usa.mvp.view.swing;

import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.usa.mvp.view.AbstractApplicationUiRunner;
import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by danilovey on 13.10.2017.
 */
public class SwingUiRunner extends AbstractApplicationUiRunner {

    @Override
    public boolean initializeUi() {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JButton("Button"), BorderLayout.CENTER);

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
    public TypeUi getTypeUi() {
        return TypeUi.MEDIUM;
    }

    @Override
    public void shutdown() {

    }
}
