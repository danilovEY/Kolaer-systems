package ru.kolaer.asmc.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.concurrent.Task;
import ru.kolaer.asmc.ui.javafx.controller.CWebBrowser;

/**
 * Запускает задачу.
 * @author Danilov E.Y.
 *
 */
public class Application implements Runnable
{
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);	
	
	private String pathApp;
	
	public Application(String path)
	{
		this.pathApp = path;
	}
	
	public void start()
	{
		if (this.pathApp != null && !this.pathApp.equals(""))
			new Thread(this).start();
		else
		{
			//NotificationManager.showNotification ("Путь к файлу/ссылке отсутствует!!", NotificationIcon.error.getIcon() );
			
		}

	}
	
    public static boolean isWindows(){

        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "win" ) >= 0); 

    }

    public static boolean isMac(){

        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "mac" ) >= 0); 

    }
    	
    public static boolean isUnix (){

        String os = System.getProperty("os.name").toLowerCase();
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
		try
		{
			Runtime r =Runtime.getRuntime();
			
			if(isWindows())
			{
				if(isURL(pathApp))
				{
					if(SettingSingleton.getInstance().isDefaultWebBrowser())
					{
						if(Application.isURL(pathApp)) {
							Platform.runLater(new Runnable() {
						        @Override
						        public void run() {
						        	final CWebBrowser web = new CWebBrowser();
									web.show();
									web.load(pathApp);
						        }
						      });
						}
					}
					else
					{
						File simpleWebBrowser = new File(SettingSingleton.getInstance().getPathWebBrowser());
						if(simpleWebBrowser.exists() && simpleWebBrowser.isFile())
						{
							 r.exec(simpleWebBrowser.getAbsolutePath()+" \""+this.pathApp+"\"");
						}
						else
						{
							System.out.println("Error");
						}
					}
				}
				else
				{
					File file = new File(this.pathApp);
					if(file.exists()) {
						if(file.isDirectory()) {
							r.exec("explorer.exe \""+this.pathApp+"\"");
						} else {
							r.exec("cmd /C "+"\""+this.pathApp+"\"");
						}
					} else {
						System.out.println("Error");
					}
				}
			}

		} 
		catch (IOException e)
		{
			//NotificationManager.showNotification ( "Не удалось запустить программу/ссылку!", NotificationIcon.error.getIcon() );
			LOG.error("Не удалось запустить программу/ссылку");
			LOG.error(e.getMessage());
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
