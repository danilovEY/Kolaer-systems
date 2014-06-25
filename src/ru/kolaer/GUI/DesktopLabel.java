package ru.kolaer.GUI;

import org.w3c.dom.Element;

import com.alee.laf.panel.WebPanel;

/**
 * Абстрактный класс для ярлыков.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public abstract class DesktopLabel extends WebPanel implements DesktopLabelInt, DataXML
{	
	/**Атрибут в xml для названия ярлыка.*/
	private final String NAME_ATR = "titleName";
	/**Атрибут в xml для пути к иконки.*/
	private final String IMAGE_PATH_ATR = "imagePaph";
	/**Атрибут в xml для пути к задаче.*/
	private final String APP_PATH_ATR = "appPath";
	/**Атрибут в xml для описания ярлыка.*/
	private final String INFO_ATR = "info";

	/**Название ярлыка.*/
	protected String titleName;
	
	/**Путь к задаче.*/
	protected String app;
	
	/**Путь к иконки.*/
	protected String image;
	
	/**Описание ярлыка.*/
	protected String info;
	
	/**Запись/чтение данных в формате xml.*/
	protected Element xmlLabelElement;
	
	/**
	 * 
	 * @param titleName - Название ярлыка.
	 * @param app - Путь для запуска.
	 * @param image - Путь для иконки.
	 * @param info - Информация.
	 */
	protected DesktopLabel(String titleName,String app, String image,String info)
	{
		this.titleName = titleName;
		this.app = app;
		this.image = image;
		this.info = info;
		this.xmlLabelElement = null;
	}

	public DesktopLabel()
	{
		this.xmlLabelElement = null;
		this.titleName = "";
		this.app = null;
		this.image = null;
		this.info = "";
	}

	public DesktopLabel(Element element)
	{
		this.xmlLabelElement = element;
		this.titleName = this.xmlLabelElement.getAttribute(NAME_ATR);
		this.app = this.xmlLabelElement.getAttribute(APP_PATH_ATR);
		this.image  = this.xmlLabelElement.getAttribute(IMAGE_PATH_ATR);
		this.info  = this.xmlLabelElement.getAttribute(INFO_ATR);
	}
	
	public void updataXML()
	{
		this.xmlLabelElement.setAttribute(NAME_ATR, this.titleName);
		this.xmlLabelElement.setAttribute(IMAGE_PATH_ATR, this.image);
		this.xmlLabelElement.setAttribute(APP_PATH_ATR, this.app);
		this.xmlLabelElement.setAttribute(INFO_ATR, this.info);
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

}
