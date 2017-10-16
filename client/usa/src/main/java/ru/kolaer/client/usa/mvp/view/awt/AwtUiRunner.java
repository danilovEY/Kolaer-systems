package ru.kolaer.client.usa.mvp.view.awt;

import ru.kolaer.client.usa.mvp.view.ApplicationUiRunner;

import java.awt.*;

/**
 * Created by danilovey on 13.10.2017.
 */
public class AwtUiRunner implements ApplicationUiRunner {
    @Override
    public void run(String[] args) {
        Frame frame = new Frame();

        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.add(new Button("Button"), BorderLayout.CENTER);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}
