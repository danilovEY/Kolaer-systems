package ru.kolaer.asmc.GUI;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.alee.laf.panel.WebPanel;

import ru.kolaer.asmc.tools.DataBaseLabelsXml;
import ru.kolaer.asmc.tools.DataBaseSettingXml;
import ru.kolaer.asmc.tools.XmlElementInt;

/**
 * Абстрактный класс для ярлыков.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public abstract class DesktopLabel extends WebPanel implements DesktopLabelInt, XmlElementInt
{	
	private static final Logger log = LoggerFactory.getLogger(DesktopLabel.class);	
	
	/**Атрибут в xml для названия ярлыка.*/
	public static final String NAME_ATR = "titleName";
	/**Атрибут в xml для пути к иконки.*/
	public static final String IMAGE_PATH_ATR = "imagePaph";
	/**Атрибут в xml для пути к задаче.*/
	public static final String APP_PATH_ATR = "appPath";
	/**Атрибут в xml для описания ярлыка.*/
	public static final String INFO_ATR = "info";

	/**Настройки*/
	protected DataBaseSettingXml setting;
	
	/**Название ярлыка.*/
	protected String titleName;
	
	/**Путь к задаче.*/
	protected String app;
	
	/**Путь к иконки.*/
	protected String image;
	
	/**Описание ярлыка.*/
	protected String info;
	
	/**БД для сохранение изменений.*/
	protected DataBaseLabelsXml xmlFile;
	
	/**Запись/чтение данных в формате xml.*/
	protected Element xmlLabelElement;
	
	/**
	 * 
	 * @param titleName - Название ярлыка.
	 * @param app - Путь для запуска.
	 * @param image - Путь для иконки.
	 * @param info - Информация.
	 */
	protected DesktopLabel(DataBaseSettingXml setting, String titleName,String app, String image,String info)
	{
		this.setting = setting;
		this.titleName = titleName;
		this.app = app;
		this.image = image;
		this.info = info;
		this.xmlLabelElement = null;
		this.xmlFile = null;
		
	}

	public DesktopLabel(DataBaseSettingXml setting)
	{
		this(setting,"",null,null,"");
	}

	public DesktopLabel(DataBaseSettingXml setting,DataBaseLabelsXml xmlFile, Element element)
	{
		this(setting,element.getAttribute(NAME_ATR),
				element.getAttribute(APP_PATH_ATR),
				element.getAttribute(IMAGE_PATH_ATR),
				element.getAttribute(INFO_ATR));
		
		this.xmlLabelElement = element;
		this.xmlFile = xmlFile;
	}
	
	public void updataXML()
	{
		this.xmlLabelElement.setAttribute(NAME_ATR, this.titleName);
		this.xmlLabelElement.setAttribute(IMAGE_PATH_ATR, this.image);
		this.xmlLabelElement.setAttribute(APP_PATH_ATR, this.app);
		this.xmlLabelElement.setAttribute(INFO_ATR, this.info);
		
		try
		{
			if(this.xmlFile!= null)
				this.xmlFile.save();
		} 
		catch (IOException e)
		{
			log.error("Сохранить ярлык \""+this.titleName+"\" не удалось");
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Устанавливает редактируемый режим. 
	 * @param mode
	 */
	public abstract void setEditMode(boolean mode);
	
	/**
	 * Возвращает состояния редактируемого режима.
	 */
	public abstract boolean isEditMode();

	public String getTitleName()
	{
		return titleName;
	}

	public void setTitleName(String titleName)
	{
		this.titleName = titleName;
	}

	public String getApp()
	{
		return app;
	}

	public void setApp(String app)
	{
		this.app = app;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public Element getXmlLabelElement()
	{
		return xmlLabelElement;
	}

	public void setXmlLabelElement(Element xmlLabelElement)
	{
		this.xmlLabelElement = xmlLabelElement;
	}

	public DataBaseLabelsXml getXmlFile()
	{
		return xmlFile;
	}

	public void setXmlFile(DataBaseLabelsXml xmlFile)
	{
		this.xmlFile = xmlFile;
	}

	public DataBaseSettingXml getSetting()
	{
		return this.setting;
	}

	public void setSetting(DataBaseSettingXml setting)
	{
		this.setting = setting;
	}

}
