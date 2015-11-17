package ru.kolaer.asmc.ui.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebEditorPane;

import ru.kolaer.asmc.tools.Resources;

/**
 * Фрейм о программе.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class AboutFrame extends WebDialog
{
	public AboutFrame(Frame pane)
	{
		super(pane, "О программе");
		
		String aboutMes = "<html>" +
				"<b>Версия: </b>"+ Resources.VERSION +
				"<br><b>Автор: </b>"+ "Данилов Евгений Юрьевич"+"</br>"+
				"<br><b>E-mail: </b>"+ "DanilovEugene@yandex.ru"+"</br>"+
				//"<a href=\"ggg\">Агрианские топорники</a>"+
		"</html>";
		
		WebEditorPane editorPane = new WebEditorPane ("text/html", aboutMes);
        editorPane.setEditable(false);

        WebScrollPane editorPaneScroll = new WebScrollPane ( editorPane );
        //editorPaneScroll.setPreferredSize ( new Dimension ( 200, 150 ) );
		
        WebButton okBut = new WebButton("OK");
        okBut.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
			}
		});
        
        WebImage aboutImage = new WebImage(Resources.ABOUT_INFO_IMAGE);
        aboutImage.setDisplayType(DisplayType.fitComponent);
        
        WebPanel infoPanel = new WebPanel(new BorderLayout());
        infoPanel.add(editorPaneScroll,BorderLayout.CENTER);
        infoPanel.add(okBut, BorderLayout.SOUTH);
        
		WebPanel mainPanel = new WebPanel(new BorderLayout());
		mainPanel.add(aboutImage, BorderLayout.CENTER);
		mainPanel.add(infoPanel,BorderLayout.SOUTH);
		setContentPane(mainPanel);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
}
