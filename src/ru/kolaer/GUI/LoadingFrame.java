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
	private WebProgressBar progressBar;
	
	public LoadingFrame(JRootPane pane, String title)
	{
		super(pane, title);

		this.progressBar = new WebProgressBar();
		this.progressBar.setIndeterminate(true);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("Пожалуйста подождите...");

		WebPanel mainPanel = new WebPanel(new BorderLayout());
		mainPanel.add(this.progressBar, BorderLayout.CENTER);
		setContentPane(mainPanel);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
	
}
