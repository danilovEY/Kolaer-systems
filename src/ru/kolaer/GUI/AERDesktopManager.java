package ru.kolaer.GUI;

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
                
				MainFrame frame = new MainFrame(args);
				frame.setVisible(true);
			}
		});
	}

}
