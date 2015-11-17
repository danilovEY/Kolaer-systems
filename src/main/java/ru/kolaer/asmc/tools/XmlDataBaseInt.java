package ru.kolaer.asmc.tools;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;

/**
 * @author Danilov E.Y.
 *
 */
public interface XmlDataBaseInt 
{
	public void save() throws IOException;
	
	public File getFileXml();
	public void setFileXml(File fileXml);
	public Document getDocument();
}
