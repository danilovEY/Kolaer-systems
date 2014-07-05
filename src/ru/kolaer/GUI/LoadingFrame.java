package ru.kolaer.GUI;

import java.awt.BorderLayout;

import javax.swing.JRootPane;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.rootpane.WebDialog;

/**
 * Окно, показывающая прогресс загрузки.
 * 
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class LoadingFrame extends WebDialog
{
	public LoadingFrame(JRootPane pane, String title)
	{
		super(pane, title);

		WebProgressBar progressBar3 = new WebProgressBar();
		progressBar3.setIndeterminate(true);
		progressBar3.setStringPainted(true);
		progressBar3.setString("Пожалуйста подождите...");

		WebPanel mainPanel = new WebPanel(new BorderLayout());
		mainPanel.add(progressBar3, BorderLayout.CENTER);
		setContentPane(mainPanel);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
}
