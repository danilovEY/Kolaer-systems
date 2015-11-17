package ru.kolaer.asmc.tools;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.kolaer.asmc.ui.swing.DefaultDesktopLabel;
import ru.kolaer.asmc.ui.swing.GroupDesktopLabels;

/**
 * Чтение/запись данных в формате .xml
 * @author Danilov E.Y.
 *
 */
public class DataBaseLabelsXml extends AbstractXmlDataBase
{
	private static final Logger log = LoggerFactory.getLogger(DataBaseLabelsXml.class);	
	
	public static final String ROOT_DESKTOP_LABELS = "DesktopLables";
	public static final String LABEL_GROUP = "GroupLabel";
	public static final String LABEL = "DesktopLable";
	
	private ArrayList<GroupDesktopLabels> groupList;
	private DataBaseSettingXml setting;
	
	public DataBaseLabelsXml(DataBaseSettingXml setting ,String pathFile) throws ParserConfigurationException, SAXException, IOException 
	{
		super(pathFile, ROOT_DESKTOP_LABELS);
		this.setting = setting;
		this.groupList = new ArrayList<GroupDesktopLabels>();	
	}
	
	public Element creatGroupDesktopLabels()
	{
		Element rootElem = this.document.getDocumentElement();

		Element groupElement = this.document.createElement(LABEL_GROUP);

		rootElem.appendChild(groupElement);
		
		return groupElement;
	}
	
	public ArrayList<GroupDesktopLabels> getAllGroupDesktopLabels()
	{
		
		groupList = new ArrayList<GroupDesktopLabels>();
		
		//log.info("Получение элемента: " + LABEL_GROUP);
        NodeList desktopLabelList=this.document.getElementsByTagName(LABEL_GROUP);
        
        //List of GroupLabel
        log.info("Получение элементов: " + desktopLabelList.getLength()
        		);
        for(int je=0;je<desktopLabelList.getLength();je++)
        {
            Node nodeGroup=desktopLabelList.item(je);
            
            if(nodeGroup.getNodeType()==Node.ELEMENT_NODE)
            {
                Element groupEl=(Element)nodeGroup;
                
                GroupDesktopLabels groupLabels = new GroupDesktopLabels(this.setting, this, groupEl);
                
               //List of Label
                //log.info("Получение элемента: " + groupLabels.getGroupName());
                NodeList labelList=groupEl.getElementsByTagName(LABEL);
                
				for (int i = 0; i < labelList.getLength(); i++)
				{
					Element labelElement = (Element) labelList.item(i);
					
					DefaultDesktopLabel label = new DefaultDesktopLabel(this.setting, this, labelElement);
					groupLabels.addLabel(label);
				}
				 groupList.add(groupLabels);
            }
           
        }
		
		return groupList;
	}

	

	public ArrayList<GroupDesktopLabels> getGroupList()
	{
		return groupList;
	}

	
	
	
}
