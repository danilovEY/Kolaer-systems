package ru.kolaer.asmc.tools;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;

/**
 * Запускает задачу.
 * @author Danilov E.Y.
 *
 */
public class Application implements ApplicationInt, Runnable
{
	private static final Logger log = LoggerFactory.getLogger(Application.class);	
	
	private DataBaseSettingXml setting;
	private String pathApp;
	
	public Application(DataBaseSettingXml setting, String path)
	{
		this.pathApp = path;
		this.setting = setting;
	}
	
	public void start()
	{
		if (this.pathApp != null && !this.pathApp.equals(""))
			new Thread(this).start();
		else
		{
			NotificationManager.showNotification ("Путь к файлу/ссылке отсутствует!!", NotificationIcon.error.getIcon() );
			
		}

	}
	
	public void startUpdate()
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{				
		    	try
                {
		    		Process p = Runtime.getRuntime().exec("cmd /C "+"\"AER-DM-WindowsLauncher.exe "+"-root_set "+"-update "+new File(pathApp).getName());
                }
                catch(IOException e)
                {
                	log.error("Не удалось запустить AER-DM-WindowsLauncher.exe");
					log.error(e.getMessage());
                }
		    	
		    	System.exit(0);

			    
			}
		}).start();
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
			 //WebOptionPane.showMessageDialog ( null, "Не указан файл!", "Ошибка", WebOptionPane.ERROR_MESSAGE );
			 NotificationManager.showNotification ( "Не указан файл!", NotificationIcon.error.getIcon() );
	
			 return;
		}
		
		try
		{
			Runtime r =Runtime.getRuntime();
			
			if(isWindows())
			{
				if(isURL(pathApp))
				{
					if(this.setting.getPathWebBrowser().equals("") )
					{
						Desktop desktop;
				        if (Desktop.isDesktopSupported()) {
				            desktop = Desktop.getDesktop();
				            if (desktop.isSupported(Desktop.Action.BROWSE)) {
				                URI uri;
				                try {
				                    uri = new URI(this.pathApp);
				                    desktop.browse(uri);
				                }
				                catch (IOException ioe) {
				                    ioe.printStackTrace();
				                }
				                catch (URISyntaxException use) {
				                    use.printStackTrace();
				                }
				            }
				        }
					}
					else
					{
						File simpleWebBrowser = new File(this.setting.getPathWebBrowser());
						if(!simpleWebBrowser.exists())
						{
							 NotificationManager.showNotification ("Утилита \""+this.setting.getPathWebBrowser()+"\" не найдена!", NotificationIcon.error.getIcon() );
						}
						else
						{
							r.exec(this.setting.getPathWebBrowser()+" "+"\""+this.pathApp+"\"");
						}
					
					}
					
					
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
				else
				{
					if(isMac())
					{
						WebOptionPane.showMessageDialog ( null, "В данной версии Mac системы не поддерживаются!", "Ошибка", WebOptionPane.ERROR_MESSAGE );
					}
				}
			}
			
		} 
		catch (IOException e)
		{
			NotificationManager.showNotification ( "Не удалось запустить программу/ссылку!", NotificationIcon.error.getIcon() );
			log.error("Не удалось запустить программу/ссылку");
			log.error(e.getMessage());
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
