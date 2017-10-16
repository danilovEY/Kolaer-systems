package ru.kolaer.client.usa.mvp.view.swing;

import ru.kolaer.client.usa.mvp.view.ApplicationUiRunner;

import javax.swing.*;
import java.awt.*;

/**
 * Created by danilovey on 13.10.2017.
 */
public class SwingUiRunner implements ApplicationUiRunner {
    @Override
    public void run(String[] args) {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JButton("Button"), BorderLayout.CENTER);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}
