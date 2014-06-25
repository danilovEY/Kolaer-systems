package ru.kolaer.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import ru.kolaer.GUI.DefaultDesktopLabel;
import ru.kolaer.GUI.GroupDesktopLabels;

/**
 * Чтение/запись данных в формате .xml
 * @author Danilov E.Y.
 *
 */
public class XMLFile
{
	/**Путь к файлу.*/
	private String pathXml;
	
	/**Файл.*/
	private File fileXml;
	
	public static final String ROOT_DESKTOP_LABELS = "DesktopLables";
	public static final String LABEL_GROUP = "GroupLabel";
	public static final String LABEL = "DesktopLable";
	
	private DocumentBuilderFactory documentBuilderFactory;
	
	private Document document;
	
	private ArrayList<GroupDesktopLabels> groupList;
	
	public XMLFile(String pathFile)
	{
		this.pathXml = pathFile;
		groupList = new ArrayList<GroupDesktopLabels>();
		
		try
		{
			this.fileXml = new File(this.pathXml);
			if(!this.fileXml.exists())
			{
				throw new NullPointerException();
			}
		}
		catch(NullPointerException ex)
		{
			PrintWriter writer = null;
			try
			{
				writer = new PrintWriter(this.pathXml, "UTF-8");
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				writer.println("<"+ROOT_DESKTOP_LABELS+">");
				writer.println("</"+ROOT_DESKTOP_LABELS+">");
				writer.close();
				this.fileXml = new File(this.pathXml);
			} catch (FileNotFoundException | UnsupportedEncodingException e)
			{
				//TODO log
				e.printStackTrace();
			}
		}
		
		try
        {
			this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db=this.documentBuilderFactory.newDocumentBuilder();
            this.document=db.parse(this.fileXml);
        }
        catch(Exception ei)
        {
        	ei.printStackTrace();
        }
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
        
        NodeList desktopLabelList=this.document.getElementsByTagName(LABEL_GROUP);
        
        //List of GroupLabel
        for(int je=0;je<desktopLabelList.getLength();je++)
        {
            Node nodeGroup=desktopLabelList.item(je);
            
            if(nodeGroup.getNodeType()==Node.ELEMENT_NODE)
            {
                Element groupEl=(Element)nodeGroup;
                
                GroupDesktopLabels groupLabels = new GroupDesktopLabels(groupEl);
                
               //List of Label
                NodeList labelList=groupEl.getElementsByTagName(LABEL);
				for (int i = 0; i < labelList.getLength(); i++)
				{
					Element labelElement = (Element) labelList.item(i);
					
					DefaultDesktopLabel label = new DefaultDesktopLabel(labelElement);
					groupLabels.addLabel(label);
				}
				 groupList.add(groupLabels);
            }
           
        }
		
		return groupList;
	}

	public String getPathXml()
	{
		return pathXml;
	}

	public void setPathXml(String pathXml)
	{
		this.pathXml = pathXml;
	}

	public File getFileXml()
	{
		return fileXml;
	}

	public void setFileXml(File fileXml)
	{
		this.fileXml = fileXml;
	}

	public Document getDocument()
	{
		return document;
	}

	public ArrayList<GroupDesktopLabels> getGroupList()
	{
		return groupList;
	}

	public void save()
	{
		OutputFormat format = new OutputFormat(document);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        
		XMLSerializer serializer = new XMLSerializer(format);

	    try
		{
			serializer.setOutputCharStream(new java.io.FileWriter(this.fileXml));
			serializer.serialize(document);
		} 
	    catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
}
