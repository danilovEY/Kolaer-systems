package ru.kolaer.asmc.runnable;

import javax.swing.SwingUtilities;

import com.alee.laf.WebLookAndFeel;

import ru.kolaer.asmc.ui.swing.MainFrame;

public class SwingLauncher
{

	public static void main(final String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
                WebLookAndFeel.install();
                
				new MainFrame(args);
			}
		});
	}

}
