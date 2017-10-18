package ru.kolaer.client.usa.mvp.view.awt;

import ru.kolaer.client.usa.mvp.viewmodel.VMainFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;

/**
 * Created by danilovey on 18.10.2017.
 */
public class VMainFrameAwt implements VMainFrame<Panel> {
    private Panel mainPanel;
    private Frame mainFrame;

    public VMainFrameAwt() {
        mainFrame = new Frame("Единая система КолАЭР");
        mainFrame.setMinimumSize(new Dimension(850,650));
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("/css/aerIcon.png"));
        mainFrame.setExtendedState(mainFrame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        mainPanel = new Panel(new BorderLayout());

        mainFrame.add(mainPanel);
    }

    @Override
    public void setContent(Panel content) {
        mainPanel.add(content, BorderLayout.CENTER);
    }

    @Override
    public Panel getContent() {
        return mainPanel;
    }

    @Override
    public void show() {
        mainFrame.setVisible(true);
    }

    @Override
    public void exit() {
        mainFrame.dispose();
    }

    @Override
    public void hide() {
        mainFrame.setVisible(false);
    }

    @Override
    public void setOnMinimize(Function<Boolean, Void> function) {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                function.apply(true);
            }
        });
    }

    @Override
    public void setOnExit(Function<Object, Void> function) {
        mainFrame.removeWindowListener(mainFrame.getWindowListeners()[mainFrame.getWindowListeners().length - 1]);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                function.apply(e);
            }
        });
    }


}
