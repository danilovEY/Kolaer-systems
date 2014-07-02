package ru.kolaer.GUI;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.alee.laf.optionpane.WebOptionPane;

/**
 * Запускает задачу.
 * @author Danilov E.Y.
 *
 */
public class Application implements ApplicationInt, Runnable
{
	private String pathApp;
	
	public Application(String path)
	{
		this.pathApp = path;
	}
	
	public void start()
	{
		if(this.pathApp != null && !this.pathApp.equals(""))
		  new Thread(this).start();
		else
			return;
	}
	
    public static boolean isWindows(){

        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf( "win" ) >= 0); 

    }

    public static boolean isMac(){

        String os = System.getProperty("os.name").toLowerCase();
        //Mac
        return (os.indexOf( "mac" ) >= 0); 

    }
    	
    public static boolean isUnix (){

        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);

    }
    public static String getOSVerion() {
        String os = System.getProperty("os.version");
        return os;
    }
    
    /**
     * Проверка на правильность url-ссылки.
     * @param url - Ссылка.
     * @return - Возвращает true если это ссылка.
     */
    public static boolean isURL(String url)
    {
    	boolean urlCheck = false;
    	try
		{
			new java.net.URL(url);
			urlCheck = true;
		} 
    	catch (MalformedURLException e)
		{
    		urlCheck = false;
		}

    	return urlCheck;
    }

	@Override
	public void run()
	{
		if(this.pathApp == null || this.pathApp.equals(""))
		{
			 WebOptionPane.showMessageDialog ( null, "Не указан файл!", "Ошибка", WebOptionPane.ERROR_MESSAGE );
			 return;
		}
		
		try
		{
			Runtime r =Runtime.getRuntime();
			
			if(isWindows())
			{
				if(isURL(pathApp))
				{
					r.exec(Resources.WEB_BROWSER+" "+"\""+this.pathApp+"\"");
				}
				else
				{
					if(new File(this.pathApp).isDirectory())
					{
						r.exec("explorer.exe "+"\""+this.pathApp+"\"");
					}
					else
					{
						r.exec("cmd /C "+"\""+this.pathApp+"\"");
					}
				}
			}
			else
			{
				if(isUnix())
				{
					WebOptionPane.showMessageDialog ( null, "В данной версии Unix системы не поддерживаются!", "Ошибка", WebOptionPane.ERROR_MESSAGE );
				}
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getPathApp()
	{
		return pathApp;
	}

	public void setPathApp(String pathApp)
	{
		this.pathApp = pathApp;
	}
	
	
}
