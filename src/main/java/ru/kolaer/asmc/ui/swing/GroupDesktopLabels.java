package ru.kolaer.asmc.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.alee.extended.painter.BorderPainter;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;

import ru.kolaer.asmc.tools.DataBaseLabelsXml;
import ru.kolaer.asmc.tools.DataBaseSettingXml;
import ru.kolaer.asmc.tools.XmlElementInt;

/**
 * Группирование ярлыков.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class GroupDesktopLabels extends WebPanel implements XmlElementInt
{
	private static final Logger log = LoggerFactory.getLogger(GroupDesktopLabels.class);	
	
	/**Сохранение данных в xml док-те.*/
	private Element xmlElement;
	
	/**Имя группы.*/
	private String groupName;
	
	/**Панель со всеми ярлыками.*/
	private WebPanel contentPanel;
	
	/**Системная панель.*/
	private WebPanel systemPanel;
	
	private DataBaseSettingXml setting;
	
	private DataBaseLabelsXml xmlFile;
	
	public GroupDesktopLabels(DataBaseSettingXml setting, DataBaseLabelsXml xmlFile,Element groupEl)
	{
		this.xmlElement = groupEl;
		this.xmlFile = xmlFile;
		this.groupName = this.xmlElement.getAttribute("name");
		this.setting = setting;
		init();
	}

	/**
	 * 
	 * @param name - Название группы.
	 */
	public GroupDesktopLabels(DataBaseSettingXml setting, String name)
	{
		this.groupName = name;
		this.xmlFile = null;
		this.setting = setting;
		init();
		
	}
	
	private void init()
	{
		this.setLayout(new BorderLayout());
 		this.setBackground(new Color(50,50,50));
 		
 		//================Системная панель===================
		@SuppressWarnings("rawtypes")
		BorderPainter borderPainterSysPanel = new BorderPainter ();
		borderPainterSysPanel.setColor(Color.ORANGE);
 		this.systemPanel = new WebPanel(new FlowLayout());
 		this.systemPanel.setPainter(borderPainterSysPanel);
 		this.systemPanel.setVisible(false);
 		
 		WebButton addLables = new WebButton("Добавить ярлык");
 		addLables.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FormCreateLabel form = new FormCreateLabel(GroupDesktopLabels.this.setting, getRootPane());
				form.pack();
				form.setVisible(true);
				
				DesktopLabel label = form.getCreatedDesktopLabel();
				if(label!=null)
				{
					contentPanel.add(label);

					Element labelElement = xmlElement.getOwnerDocument().createElement(DataBaseLabelsXml.LABEL);

					xmlElement.appendChild(labelElement);
					
					label.setXmlLabelElement(labelElement);
					label.setXmlFile(xmlFile);
					label.updataXML();
				}
			}
		});

 		
 		
		
 		WebPanel mainPanel = new WebPanel(new BorderLayout());
		
		//===========Panel for list labels==============
		this.contentPanel = new WebPanel();
 		this.contentPanel.setLayout(new WrapLayout());
 		this.contentPanel.setBackground(new Color(50,50,50));
				
		
 		//================Adds===================
 		this.systemPanel.add(addLables);
 		
 		mainPanel.add(this.contentPanel,BorderLayout.CENTER);
 		
 		add(mainPanel,BorderLayout.CENTER);
 		add(this.systemPanel,BorderLayout.NORTH);	
	}
	

	/**
	 * Установить режим редактирования.
	 * @param mode 
	 */
	public void setEditMode(boolean mode)
	{
		this.systemPanel.setVisible(mode);
		for(Component label : this.contentPanel.getComponents())
		{
			((DesktopLabel)label).setEditMode(this.systemPanel.isVisible());
		}
	}
	
	/**
	 * Добавить ярлык.
	 * @param label - ярлык.
	 */
	public void addLabel(DesktopLabel label)
	{
		this.contentPanel.add(label);
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public Element getXmlElement()
	{
		return xmlElement;
	}

	public void setXmlElement(Element xmlElement)
	{
		this.xmlElement = xmlElement;
	}

	@Override
	public void updataXML()
	{
		this.xmlElement.setAttribute("name", this.groupName);
		
		try
		{
			if(this.xmlFile != null)
				this.xmlFile.save();
		} catch (IOException e)
		{
			log.error("Сохранить группу ярлыков \""+this.groupName+"\" не удалось");
			log.error(e.getMessage());
		}
		
	}

	public DataBaseLabelsXml getXmlFile()
	{
		return xmlFile;
	}

	public void setXmlFile(DataBaseLabelsXml xmlFile)
	{
		this.xmlFile = xmlFile;
	}
	
	
}
