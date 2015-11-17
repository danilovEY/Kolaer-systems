package ru.kolaer.asmc.GUI;

import javax.swing.SwingUtilities;

import com.alee.laf.WebLookAndFeel;

public class AERDesktopManager
{

	public static void main(final String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
                WebLookAndFeel.install();
                
                //WebLookAndFeel.setDecorateAllWindows(true);
                
				@SuppressWarnings("unused")
				MainFrame frame = new MainFrame(args);
			}
		});
	}

}
