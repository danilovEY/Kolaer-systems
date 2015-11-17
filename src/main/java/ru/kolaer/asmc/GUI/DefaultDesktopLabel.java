package ru.kolaer.asmc.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.w3c.dom.Element;

import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.extended.painter.BorderPainter;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.tooltip.TooltipManager;

import ru.kolaer.asmc.tools.Application;
import ru.kolaer.asmc.tools.ApplicationInt;
import ru.kolaer.asmc.tools.DataBaseLabelsXml;
import ru.kolaer.asmc.tools.DataBaseSettingXml;
import ru.kolaer.asmc.tools.Resources;

/**
 * Стандартный вид ярлыка.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class DefaultDesktopLabel extends DesktopLabel
{	
	/**Запуск задач.*/
	private ApplicationInt application;
	
	/**Название ярлыка.*/
	private WebLabel labelTitle;
	
	/**Иконка ярлыка.*/
	private WebImage iconLabel;
	
	/**Панель я информацией о ярлыке.*/
	//private WebTextArea editPanel;
	
	private WebPanel iconPanel;
	
	/**Кнопка запускающая задачу.*/
	private WebButton runBut;
	
	/**Меню.*/
	private WebPopupMenu popMenu;
	
	private boolean editMode = false;
	
	protected DefaultDesktopLabel(DataBaseSettingXml setting)
	{
		super(setting);
		initialize();
	}
	
	/**
	 * 
	 * @param titleName - Название ярлыка.
	 * @param app - Путь для запуска.
	 * @param image - Путь для иконки.
	 * @param info - Информация.
	 */
	protected DefaultDesktopLabel(DataBaseSettingXml setting, String titleName, String app,
			String image, String info)
	{
		super(setting,titleName, app, image, info);
		initialize();
	}

	public DefaultDesktopLabel(DataBaseSettingXml setting,DataBaseLabelsXml xmlFile, Element element)
	{
		super(setting,xmlFile, element);		
		initialize();
	}

	private void initialize()
	{	
		this.application = new Application(this.setting, this.app);
		
		//=================PopUp Menu===========================
		this.popMenu = new WebPopupMenu();
	    this.popMenu.setBackground(new Color(150,150,150));
		
		WebMenuItem editLabelMenu = new WebMenuItem("Редактировать ярлык");
		editLabelMenu.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FormCreateLabel form = new FormCreateLabel(DefaultDesktopLabel.this.setting, getRootPane(), DefaultDesktopLabel.this);
				form.pack();
				form.setVisible(true);
				
				update();
				updataXML();
			}
		});
		
		WebMenuItem removeLabelMenu = new WebMenuItem("Удалить ярлык");
		removeLabelMenu.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(xmlLabelElement!=null)
				{
				  xmlLabelElement.getParentNode().removeChild(xmlLabelElement);
				  updataXML();
				  NotificationManager.showNotification ( "Ярлык удален.", NotificationIcon.information.getIcon() );
				}
				
				setVisible(false);
			}
		});
		
		this.popMenu.add(editLabelMenu);
		this.popMenu.add(removeLabelMenu);
		
		this.addMouseListener(new MouseAdapter()
		{
		    public void mousePressed(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }

		    public void mouseReleased(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }

		    private void doPop(MouseEvent e)
		    {
		    	if(editMode)
		    		popMenu.show(e.getComponent(), e.getX()-30, e.getY());
		    }
		});
		

		//==============Desktop Panel=================
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setBackground(new Color(50,50,50));
		Dimension sizeDLabel = new Dimension(500, 100);
		this.setPreferredSize(sizeDLabel);
		this.setMinimumSize(sizeDLabel);
	    this.setMaximumSize(sizeDLabel);
	    
		//==============Border for Main panel=================
		@SuppressWarnings("rawtypes")
		BorderPainter borderPainterMainPanel = new BorderPainter ();
		borderPainterMainPanel.setColor(new Color ( 39, 95, 173 ));
		
		//==============Main panel=================
		WebPanel mainPanel = new WebPanel(new BorderLayout());
		mainPanel.setPainter( borderPainterMainPanel ).setMargin(5);
		mainPanel.setBackground(new Color(50,50,50));
		
		//==============Title Label=================
		this.labelTitle  = new WebLabel(this.titleName);
		this.labelTitle.setForeground(Color.YELLOW);
		this.labelTitle.setFont(new Font("Tahoma", Font.BOLD, 17));
		this.labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//==============Panel for Button and TextInfo=================
		WebPanel contPanel = new WebPanel();
		contPanel.setLayout(new WrapLayout());
		contPanel.setBackground(new Color(50,50,50));
		
		//==============Button=================
		runBut = new WebButton(this.info);
		Dimension sizeBut = new Dimension(350, 30);
		runBut.setPreferredSize(sizeBut);
		runBut.setBottomBgColor(new Color(20,20,20));
		runBut.setTopBgColor(new Color(100,100,100));
		runBut.setShadeColor(Color.YELLOW);
		runBut.setForeground(Color.WHITE);
		runBut.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		runBut.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				application.start();
			}
		});
		
		//==============TextInfo=================
		/*this.editPanel = new WebTextArea();
		this.editPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.editPanel.setEditable(false);
		this.editPanel.setText(this.info);
		this.editPanel.setLineWrap ( true );
		this.editPanel.setWrapStyleWord ( true );

        WebScrollPane textInfoScroll = new WebScrollPane ( this.editPanel );
        textInfoScroll.setPreferredSize ( new Dimension ( 200, 150 ) );*/
        
        //==============Image=================
		iconLabel = new WebImage();

		if (this.image.equals("default"))
		{
			iconLabel = new WebImage(Resources.AER_ICON);
		} 
		else
		{
			if (this.image.equals("null") || this.image.equals(""))
			{
				iconLabel.setVisible(false);
			} 
			else
			{
				iconLabel = new WebImage(this.image);
			}
		}

		iconLabel.setDisplayType(DisplayType.fitComponent);

        iconLabel.setPreferredSize ( new Dimension(100, 100) );
       
		
        //============Tooltip===========================
        TooltipManager.setTooltip ( runBut, this.info, com.alee.managers.language.data.TooltipWay.down, 0 );
        
        //==============Panel for icon=================
        iconPanel = new WebPanel(new BorderLayout());
        iconPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        iconPanel.setBackground(new Color(50,50,50));
        iconPanel.add(iconLabel);
        iconPanel.setVisible(iconLabel.isVisible());
		
		//==============Panel for TextInfo=================
		/*WebPanel panelEditPanel = new WebPanel(new BorderLayout());
		panelEditPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelEditPanel.setBackground(new Color(50,50,50));
		panelEditPanel.add(textInfoScroll);*/
		
        WebPanel labelAndButPanel = new WebPanel(new BorderLayout());
        labelAndButPanel.setBackground(new Color(50,50,50));
        labelAndButPanel.add(labelTitle,BorderLayout.NORTH);
        labelAndButPanel.add(runBut,BorderLayout.CENTER);
		
		contPanel.add(labelAndButPanel);
		//contPanel.add(runBut);
		
		mainPanel.add(contPanel,BorderLayout.CENTER);
		mainPanel.add(iconPanel,BorderLayout.WEST);
		
		this.add(mainPanel);
	}

	public void setEditMode(boolean mode)
	{
		this.editMode=mode;
	}

	@Override
	public void update()
	{
		this.labelTitle.setText(this.titleName);
		iconLabel = new WebImage();

		if (this.image.equals("default"))
		{
			iconLabel = new WebImage(Resources.AER_ICON);
		} 
		else
		{
			if (this.image.equals("null") || this.image.equals(""))
			{
				iconLabel.setVisible(false);
			} 
			else
			{
				iconLabel = new WebImage(this.image);
			}
		}

		iconLabel.setDisplayType(DisplayType.fitComponent);

        iconLabel.setPreferredSize ( new Dimension(100, 100) );
        
		iconPanel.removeAll();
		iconPanel.add(this.iconLabel,BorderLayout.CENTER);
		
		this.application = new Application(this.setting, this.app);
		// this.editPanel.setText(this.info);
		this.runBut.setText(this.info);
		
		
		
	}

	@Override
	public boolean isEditMode()
	{
		return this.editMode;
	}
}
