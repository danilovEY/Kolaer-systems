package ru.kolaer.asmc.tools;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Настройки программы.
 * @author Danilov E.Y.
 *
 */
public class DataBaseSettingXml extends AbstractXmlDataBase
{
	private static Logger log = LoggerFactory.getLogger(DataBaseSettingXml.class);
	
	public static String ROOT_SETTING_ELEMENT = "Settings";
	
	public static String SETTING_TOP_BANNER_ELEMENT = "TopBanner";
	public static String ATR_SETTING_TOP_BANNER_ELEMENT_PATH = "path";
	
	public static String SETTING_WEB_BROWSER_ELEMENT = "WebBrowser";
	public static String ATR_SETTING_WEB_BROWSER_ELEMENT_PATH = "path";
	
	public static String SETTING_FRAME_SIZE_ELEMENT = "FrameSize";
	public static String ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_STATIC = "Static";
	public static String ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_WIDTH = "Width";
	public static String ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_HEIGTH = "Height";

	/**Путь к верхниму баннеру.*/
	private String pathTopBanner;
	/**Путь к браузеру.*/
	private String pathWebBrowser;
	
	private boolean staticSizeFrame;
	private int widthSizeFrame;
	private int heidthSizeFrame;
	
	
	
	public DataBaseSettingXml(String pathFile) throws ParserConfigurationException, SAXException, IOException 
	{
		super(pathFile, ROOT_SETTING_ELEMENT);
		
		this.pathTopBanner = "null";
		this.pathWebBrowser = new File(Resources.WEB_BROWSER).getAbsolutePath();
		this.staticSizeFrame = false;
		this.widthSizeFrame = 0;
		this.heidthSizeFrame = 0;
		
		this.init();
		
		this.save();
		
	}

	public void init() 
	{
		Element rootElement = this.document.getDocumentElement();

		//=============TOP Banner=============
		Element baner = this.getElementById(this.document, SETTING_TOP_BANNER_ELEMENT);
		if(baner == null)
		{
			baner = this.document.createElement(SETTING_TOP_BANNER_ELEMENT+"Element");
			baner.setAttribute("id", SETTING_TOP_BANNER_ELEMENT);
			baner.setIdAttribute("id", true);
			baner.setAttribute(ATR_SETTING_TOP_BANNER_ELEMENT_PATH, this.pathTopBanner);
			rootElement.appendChild(baner);
		}
		else
		{
			this.pathTopBanner = baner.getAttribute(ATR_SETTING_TOP_BANNER_ELEMENT_PATH);
		}
		
		//=============Web Browser=============
		Element browser = this.getElementById(this.document, SETTING_WEB_BROWSER_ELEMENT);
		if(browser == null)
		{
			browser = this.document.createElement(SETTING_WEB_BROWSER_ELEMENT+"Element");
			browser.setAttribute("id", SETTING_WEB_BROWSER_ELEMENT);
			browser.setIdAttribute("id", true);
			browser.setAttribute(ATR_SETTING_WEB_BROWSER_ELEMENT_PATH, this.pathWebBrowser);
			rootElement.appendChild(browser);
		}
		else
		{
			this.pathWebBrowser = browser.getAttribute(ATR_SETTING_WEB_BROWSER_ELEMENT_PATH);
		}
		
		//=============Size Frame=============
		Element sizeFrame = this.getElementById(this.document, SETTING_FRAME_SIZE_ELEMENT);
		if(sizeFrame == null)
		{
			sizeFrame = this.document.createElement(SETTING_FRAME_SIZE_ELEMENT+"Element");
			sizeFrame.setAttribute("id", SETTING_FRAME_SIZE_ELEMENT);
			sizeFrame.setIdAttribute("id", true);
			sizeFrame.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_STATIC, "false");
			sizeFrame.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_WIDTH, "0");
			sizeFrame.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_HEIGTH, "0");
			rootElement.appendChild(sizeFrame);
		}
		else
		{
			if(sizeFrame.getAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_STATIC).equals("true"))
				this.staticSizeFrame = true;
			else
				this.staticSizeFrame = false;
			
			try
			{
				this.widthSizeFrame = Integer.parseInt(sizeFrame.getAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_WIDTH));
				this.heidthSizeFrame = Integer.parseInt(sizeFrame.getAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_HEIGTH));
			}
			catch(NumberFormatException ex)
			{
				this.setStaticSizeFrame(false);
				this.widthSizeFrame = 0;
				this.heidthSizeFrame = 0;
			}
			
		}
		
	}

	public String getPathTopBanner() 
	{
		return pathTopBanner;
	}

	public void setPathTopBanner(String pathTopBanner) 
	{
		this.pathTopBanner = pathTopBanner;
		
		Element baner = this.getElementById(this.document, SETTING_TOP_BANNER_ELEMENT);
		baner.setAttribute(ATR_SETTING_TOP_BANNER_ELEMENT_PATH, this.pathTopBanner);
		
	}

	public String getPathWebBrowser()
	{
		return this.pathWebBrowser;
	}

	public void setPathWebBrowser(String pathWebBrowser)
	{
		this.pathWebBrowser = pathWebBrowser;
		
		Element browser = this.getElementById(this.document, SETTING_WEB_BROWSER_ELEMENT);
		browser.setAttribute(ATR_SETTING_WEB_BROWSER_ELEMENT_PATH, this.pathWebBrowser);
		
	}

	public boolean isStaticSizeFrame()
	{
		return this.staticSizeFrame;
	}

	public void setStaticSizeFrame(boolean staticSizeFrame)
	{
		this.staticSizeFrame = staticSizeFrame;
		
		Element browser = this.getElementById(this.document, SETTING_FRAME_SIZE_ELEMENT);
		if(this.staticSizeFrame)
			browser.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_STATIC, "true");
		else
			browser.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_STATIC, "false");
	}

	public int getWidthSizeFrame()
	{
		return this.widthSizeFrame;
	}

	public void setWidthSizeFrame(int widthSizeFrame)
	{
		this.widthSizeFrame = widthSizeFrame;
		
		Element browser = this.getElementById(this.document, SETTING_FRAME_SIZE_ELEMENT);
		browser.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_WIDTH, String.valueOf(this.widthSizeFrame));
	}

	public int getHeidthSizeFrame()
	{
		return this.heidthSizeFrame;
	}

	public void setHeidthSizeFrame(int heidthSizeFrame)
	{
		this.heidthSizeFrame = heidthSizeFrame;
		
		Element browser = this.getElementById(this.document, SETTING_FRAME_SIZE_ELEMENT);
		browser.setAttribute(ATR_SETTING_FRAME_SIZE_ELEMENT_ELEMENT_HEIGTH, String.valueOf(this.heidthSizeFrame));
	}
}
