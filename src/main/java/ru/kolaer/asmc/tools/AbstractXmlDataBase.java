package ru.kolaer.asmc.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * @author Danilov E.Y.
 *
 */
public abstract class AbstractXmlDataBase implements XmlDataBaseInt
{
	private static final Logger log = LoggerFactory.getLogger(AbstractXmlDataBase.class);	
	
	/**Путь к файлу.*/
	protected String pathXml;
	
	/**Файл.*/
	protected File fileXml;
	
	protected DocumentBuilderFactory documentBuilderFactory;
	
	protected Document document;
	
	
	
	public AbstractXmlDataBase(String pathFile, String rootElement)throws ParserConfigurationException, SAXException, IOException
	{
		this.pathXml = pathFile;
		
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
			log.info("Создание файла: " + this.pathXml);
			try
			{
				writer = new PrintWriter(this.pathXml, "WINDOWS-1251");
				writer.println("<?xml version=\"1.0\" encoding=\"WINDOWS-1251\" ?>");
				writer.println("<"+rootElement+">");
				writer.println("</"+rootElement+">");
				writer.close();
				this.fileXml = new File(this.pathXml);
			} 
			catch (FileNotFoundException | UnsupportedEncodingException e)
			{
				log.error("Невозможно создать файл!");
				log.error(e.getMessage());
				throw e;
			}
		}
		log.info("Парсинг файла...");
		
		try
		{
			this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
			this.documentBuilderFactory.setValidating(true);
			DocumentBuilder db = this.documentBuilderFactory.newDocumentBuilder();
			this.document=db.parse(this.fileXml);
		} catch (ParserConfigurationException e)
		{
			log.error(e.getMessage());
			throw e;
		} 
		catch (SAXException e)
		{
			log.error(e.getMessage());
			throw e;
		} 
		catch (IOException e)
		{
			log.error(e.getMessage());
			throw e;
		}
		
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
	
	public Element getElementById(Node doc, String id)
    {
        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile("//*[@id = '" + id + "']");
            Element result = (Element) expr.evaluate(doc, XPathConstants.NODE);
            return result;
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
	
	public void save() throws IOException
	{		
		log.info("Формирование xml документа...");
		
		OutputFormat format = new OutputFormat(document,"WINDOWS-1251",true);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        
		XMLSerializer serializer = new XMLSerializer(format);
		

		log.info("Сохранение xml документа...");

		
		try
		{
			serializer.setOutputCharStream(new PrintWriter(this.pathXml, "WINDOWS-1251"));
			serializer.serialize(document);
		} 
		catch (IOException e)
		{
			log.error(e.getMessage());
			throw e;
		}

	}
}
