package ru.kolaer.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRootPane;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;

/**
 * Окно для создания групп с ярлыками.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class FormCreateGroupDesktopLabels extends WebDialog
{	
	private GroupDesktopLabels createdGroupLabels;
	
	public FormCreateGroupDesktopLabels(JRootPane rootPanel)
	{
		super(rootPanel,"Создать группу для ярлыков");
		this.createdGroupLabels = null;
		this.init();
	}
	
	private void init()
	{
		this.setDefaultCloseOperation ( WebDialog.DISPOSE_ON_CLOSE );
		this.setResizable ( false );
		this.setModal ( true );
        this.setTopBg(new Color(100, 100, 100));
		this.setMiddleBg(new Color(100, 100, 100));
        
		WebPanel mainPanel = new WebPanel(new FlowLayout());
        mainPanel.setBackground(new Color(100,100,100));
        
        WebLabel nameGroupLabel = new WebLabel("Название группы: ");
        
        final WebTextField nameGroup = new WebTextField ( 15 );
        nameGroup.setInputPrompt ( "Введите название..." );
        nameGroup.setInputPromptFont ( nameGroup.getFont ().deriveFont ( Font.ITALIC ) );
        
        WebButton createGroupBut = new WebButton("Создать группу");
        createGroupBut.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean showErr = false;
				String messErr = "";
				
				if(nameGroup.getText().equals(""))
				{
					showErr = true;
					messErr += "-Название группы не может быть пустым.\n";
				}
				
				if(showErr)
				{
					WebOptionPane.showMessageDialog ( getOwner(), messErr, "Ошибка", WebOptionPane.ERROR_MESSAGE );
					return;
				}
				
				createdGroupLabels = new GroupDesktopLabels(nameGroup.getText());
				setVisible(false);
			}
		});
        
        WebButton cancel = new WebButton("Отмена");
        cancel.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
        
        WebPanel groupBut = new WebPanel(new FlowLayout());
        groupBut.setBackground(new Color(100,100,100));
        groupBut.add(createGroupBut);
        groupBut.add(cancel);
        
        mainPanel.add(nameGroupLabel);
        mainPanel.add(nameGroup);
        
        add(mainPanel,BorderLayout.CENTER);
        add(groupBut,BorderLayout.SOUTH);
	}

	public GroupDesktopLabels getCreatedGroupLabels()
	{
		return createdGroupLabels;
	}
}
