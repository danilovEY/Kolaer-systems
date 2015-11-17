package ru.kolaer.asmc.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
//import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ru.kolaer.asmc.tools.Application;
import ru.kolaer.asmc.tools.DataBaseLabelsXml;
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.core.FileAppender;
import ru.kolaer.asmc.tools.DataBaseSettingXml;
import ru.kolaer.asmc.tools.FTPUpload;
import ru.kolaer.asmc.tools.Language;
import ru.kolaer.asmc.tools.Resources;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationListener;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.NotificationOption;
import com.alee.managers.notification.WebNotificationPopup;

/**
 * Главное окно приложения.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends WebFrame
{
	private static final Logger log = LoggerFactory.getLogger(MainFrame.class);	

	/**Названия окна.*/
	private final String TITLE_NAME = "АЭР - Десктоп Менеджер";
	
	private final String PATH_DATA_BASE_LABELS = "Data.xml";
	private final String PATH_DATA_BASE_SETTING = "Utilites/Setting.xml";
	
	private final Font DEFAULT_FONT_MENU_BAR = new Font("Times New Roman", Font.PLAIN, 14);
	
	/**Меню панель окна.*/
	private WebMenuBar menuBar;
	
	/**БД в формате xml.*/
	private DataBaseLabelsXml data;
	
	private DataBaseSettingXml settingData;
	
	/**FTP для обновлений.*/
	private FTPUpload ftp;
	
	/**Редактирекмый режим.*/
	private boolean editMode = false;
	
	/**Поток для чтения данных.*/
	private Thread  threadData;
	/**Вкладка с контентом.*/
	private WebTabbedPane tabbedPane;
	
	private Thread autoCheckUpdate;
	
	private LoadingFrame loadDialog;
	
	private final WebNotificationPopup notifNewVersion = NotificationManager
			.showNotification(getContentPane(),"Доступна новая версия. Загрузить?",
					NotificationIcon.question.getIcon(),
					NotificationOption.yes, NotificationOption.no, NotificationOption.cancel);
	
	/**Ключ для включения редактируемого режима.*/
	private final String accesKey = "-root_set";
	
	/**Ключ для вычисления md5 суммы.*/
	private final String makeMD5HashKey = "-hash";
	
	/**Ключ флаг для определения через что была запуженна программа.*/
	private final String launcherKey = "-launcher";

	private WebPanel topBannerImagePanel;
	
	public MainFrame(String[] args)
	{	
		super();
		
		if (args.length > 0)
		{
			for (int i=0; i<args.length; i++)
			{
				if (args[i].equals(accesKey))
				{
					editMode = true;
					try
                    {
						LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
				        JoranConfigurator configurator = new JoranConfigurator();
				        configurator.setContext(context);
				        context.reset();
				 
				        try {
				            configurator.doConfigure(getClass().getClassLoader().getResourceAsStream("logback1.xml"));
				        } 
				        catch (JoranException je) 
				        {
				            StatusPrinter.print(context);
				        }
                    }
                    catch(SecurityException e)
                    {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
				} 
				else if (args[i].equals(makeMD5HashKey))
				{
					String md5 = FTPUpload.checkSum("Utilites/AER-DecktopManager.jar");
					if (md5 != null)
					{
						try
						{
							OutputStream outputStream2 = new BufferedOutputStream(
									new FileOutputStream(md5 + ".md5"));
							String data = "Version: "
									+ "\""
									+ Resources.VERSION
									+ "\" "
									+ new SimpleDateFormat("dd-MM-yy hh:mm")
											.format(new Date());
							outputStream2.write(data.getBytes());
							outputStream2.close();
						} catch (IOException e)
						{
							log.error(e.getMessage());
							System.exit(1);
						}
					}
					System.exit(0);
				}
				else if(args[i].equals(launcherKey))
				{
					log.info("Открыт через лаунчер. Версия: " + args[i+1]);
				}
			}
		}

		log.info("=================== Программа открыта ("+new SimpleDateFormat("dd-MM-yy hh:mm:ss").format(new Date())+") || Версия: \""+Resources.VERSION+"\" =================");
		
		init();		
	}
	
	private void init()
	{
		try 
		{
			this.settingData = new DataBaseSettingXml(PATH_DATA_BASE_SETTING);
		} 
		catch (IOException e)
		{
			WebOptionPane.showMessageDialog ( getOwner(), "Не удалось загрузить файл:" + this.data.getFileXml().getAbsolutePath(), "Ошибка", WebOptionPane.ERROR_MESSAGE );
			System.exit(1);
		}
		catch( SAXException | ParserConfigurationException e)
		{
			WebOptionPane.showMessageDialog ( getOwner(), "Не удалось правильно прочитать файл:" + this.data.getFileXml().getAbsolutePath(), "Ошибка", WebOptionPane.ERROR_MESSAGE );
			System.exit(1);
		}
		
		
		
		loadDialog = new LoadingFrame(getRootPane(), Language.DIALOG_LOADING_COMPONENTS);
		loadDialog.setVisible(true);
		
		this.ftp = new FTPUpload();
		
		Image img = new ImageIcon(Resources.AER_LOGO).getImage();
		
		this.topBannerImagePanel = new WebPanel(new BorderLayout());
		
	    this.setIconImage(img);
	    this.setTitle(TITLE_NAME);
		this.setSize(new Dimension(500, 500));
		this.setDefaultCloseOperation ( WebFrame.DISPOSE_ON_CLOSE );
	    
		this.setTopBg(new Color(100, 100, 100));
		this.setMiddleBg(new Color(100, 100, 100));
		this.getContentPane().setBackground(new Color(100, 100, 100));
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
                {
	                MainFrame.this.settingData.save();
                }
                catch(IOException e1)
                {
                }
				log.info("=================== Программа закрыта ("+new SimpleDateFormat("dd-MM-yy hh:mm:ss").format(new Date())+") =================");
				System.exit(0);
			}
		});		

		if(editMode)
		{
    		//==============Проверка новой версии==============
			this.autoCheckUpdate= new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while(true)
					{
						try
						{
							if(ftp.isConnected())
							{
								if(ftp.isNewVersion())
								{
									notifNewVersion.setVisible(true);
								}
							}
							else
							{
								ftp.connectFTP();
								continue;
							}
						}
						catch(IOException e){}

						try
						{
							// Проверка каждые 5 минут
							Thread.sleep(5 * 60 * 1000);
						}
						catch(InterruptedException e)
						{
							return;
						}

					}
				}
			});
			autoCheckUpdate.setDaemon(true);
			autoCheckUpdate.start();
		}
		
		
		// ===============Notifycations=================
		notifNewVersion.setVisible(false);

		notifNewVersion.addNotificationListener(new NotificationListener()
		{

			@Override
			public void optionSelected(NotificationOption option)
			{
				if (option.equals(NotificationOption.yes))
				{
					Thread thread = new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							if(!ftp.isConnected())
							{
								
								try
								{
									log.info("Подключение к FTP...");
									ftp.connectFTP();
								} 
								catch (SocketException e)
								{
									ftp.disconnectFTP();
									WebNotificationPopup notificationPopup = NotificationManager.showNotification (getContentPane(), "Не удалось соединиться с сервером для обновлений!", NotificationIcon.error.getIcon() );
									notificationPopup.setDisplayTime ( 5000 );
									//WebOptionPane.showMessageDialog ( getOwner(), "Не удалось соединиться с сервером для обновлений!", "Ошибка", WebOptionPane.ERROR_MESSAGE );
								} 
								catch (IOException e){}
							}
							
							boolean isDownload = false;

							isDownload = ftp.downloadNewVersion();
							
							if(isDownload)
							{
								log.warn("Перезапуск программы.");
								
								WebOptionPane.showMessageDialog(null, "Перезагрузка программы...", 
										"Загруженно!",WebOptionPane.INFORMATION_MESSAGE);
								
								new Application(MainFrame.this.settingData, new File(ftp.getPathNewVer()).getName()).startUpdate();
							}
							else
							{
								WebNotificationPopup notificationPopup = NotificationManager.showNotification (getContentPane(), "Не удалось загрузить обновление!", NotificationIcon.error.getIcon() );
								notificationPopup.setDisplayTime ( 5000 );
							}
							

						}
					});
					thread.setDaemon(true);
					thread.start();
				}
				else
				{
					if(option.equals(NotificationOption.no))
					{
						notifNewVersion.setVisible(false);
						autoCheckUpdate.interrupt();
					}
					else
					{
						if(option.equals(NotificationOption.cancel))
						{
							notifNewVersion.setVisible(false);
						}
					}
				}
			}

			@Override
			public void closed()
			{

			}

			@Override
			public void accepted()
			{
			}
		});
		
		//===============Content tab panel===================
		tabbedPane = new WebTabbedPane ();
		tabbedPane.setPreferredSize ( new Dimension ( 600, 500 ) );
		tabbedPane.setTabPlacement ( WebTabbedPane.LEFT );
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 13));
		tabbedPane.setTopBg(new Color(150, 150, 150));
		tabbedPane.setBackground(new Color(150, 150, 150));
		tabbedPane.setBottomBg(new Color(150, 150, 150));
		//tabbedPane.setForeground(Color.RED); - цвет текста
		
		//=============Menu bar==================
		menuBar = new WebMenuBar();
		menuBar.setUndecorated ( true );
		menuBar.setBackground(new Color(100, 100, 100));
				
		WebMenu fileMenu = new WebMenu ( Language.MENU_BAR_FILE);
		fileMenu.setFont(this.DEFAULT_FONT_MENU_BAR);
		WebMenu tabsMenu = new WebMenu(Language.MENU_BAR_TABS);
		tabsMenu.setFont(this.DEFAULT_FONT_MENU_BAR);
		WebMenu helpMenu = new WebMenu(Language.MENU_BAR_HELP);
		helpMenu.setFont(this.DEFAULT_FONT_MENU_BAR);
		
		WebMenuItem settingMenuItem = new WebMenuItem(Language.MENU_BAR_ITEM_SETTING);
		settingMenuItem.setFont(this.DEFAULT_FONT_MENU_BAR);
		
		WebMenuItem exitMenuItem = new WebMenuItem(Language.MENU_BAR_ITEM_EXIT);
		exitMenuItem.setFont(this.DEFAULT_FONT_MENU_BAR);
		
		WebMenuItem aboutItemMenu = new WebMenuItem(Language.MENU_BAR_ITEM_ABOUT);
		aboutItemMenu.setFont(this.DEFAULT_FONT_MENU_BAR);
		
		WebMenuItem updateTabs = new WebMenuItem(Language.MENU_BAR_ITEM_UPDATE_CONTENTS);
		updateTabs.setFont(this.DEFAULT_FONT_MENU_BAR);
		
		WebMenuItem checkUpdate = new WebMenuItem(Language.MENU_BAR_ITEM_CHECK_NEW_VERSION);
		checkUpdate.setFont(this.DEFAULT_FONT_MENU_BAR);
		
		WebMenuItem addTab = new WebMenuItem(Language.MENU_BAR_ITEM_ADD_TAB);
		addTab.setFont(this.DEFAULT_FONT_MENU_BAR);
		addTab.setAccelerator(Hotkey.CTRL_N);
		
		final WebMenuItem removeTab = new WebMenuItem(Language.MENU_BAR_ITEM_DELETE_TAB);
		removeTab.setFont(this.DEFAULT_FONT_MENU_BAR);
		removeTab.setVisible(false);
		
		aboutItemMenu.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				new AboutFrame(null).setVisible(true);
			}
		});
		
		updateTabs.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				tabbedPane.removeAll();
				
				new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						updateData();
					}
				}).start();

			}
		});
		
		checkUpdate.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{				
				Thread thread = new Thread(new Runnable()
				{					
					@Override
					public void run()
					{
						LoadingFrame loadDialog = new LoadingFrame(getRootPane(), "Проверка обновлений");
						
						loadDialog.setVisible(true);
						
						MainFrame.this.checkNewVersion();
						
						loadDialog.setVisible(false);
					}
				});
				thread.setDaemon(true);
				thread.start();
			}
		});
		
		addTab.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				FormCreateGroupDesktopLabels form = new FormCreateGroupDesktopLabels(MainFrame.this.settingData, getRootPane());
				form.setLocationRelativeTo(getRootPane());
				form.pack();
				form.setVisible(true);

				GroupDesktopLabels groupLabels = form.getCreatedGroupLabels();

				if(groupLabels!=null)
				{
					log.info("Добовление вкладки...");
					groupLabels.setXmlElement(data.creatGroupDesktopLabels());
					groupLabels.setXmlFile(data);
					groupLabels.updataXML();
					groupLabels.setEditMode(editMode);
					WebScrollPane scrollPanel = new WebScrollPane(groupLabels, false, true);
					tabbedPane.add(groupLabels.getGroupName(),scrollPanel);
					NotificationManager.showNotification (getContentPane(), "Вкладка добавленна.", NotificationIcon.information.getIcon() ).setDisplayTime ( 5000 );
				}
			}
		});
			
		removeTab.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				WebScrollPane scPanel = (WebScrollPane) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
				if(scPanel == null)
				{
					NotificationManager.showNotification (getContentPane(), "Не удалось удалить вкладку!", NotificationIcon.information.getIcon() ).setDisplayTime ( 5000 );
					log.info("scPanel == null");
					return;
				}
				
				GroupDesktopLabels groupLabels = (GroupDesktopLabels) scPanel.getViewport().getView();
				
				if(WebOptionPane.showConfirmDialog ( getOwner(), "Вы действительно хотите удалить вкладку \""+groupLabels.getGroupName()+"\" и все ее ярлыки?",
						"Удалить вкладку?",
                        WebOptionPane.YES_NO_OPTION,
                        WebOptionPane.QUESTION_MESSAGE ) == WebOptionPane.YES_OPTION)
				{
					log.info("Удаление вкладки...");
					tabbedPane.remove(tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));
					groupLabels.getXmlElement().getParentNode().removeChild(groupLabels.getXmlElement());
					groupLabels.updataXML();
					NotificationManager.showNotification (getContentPane(), "Вкладка удалена.", NotificationIcon.information.getIcon()).setDisplayTime ( 5000 );
				}
			
			}
		});
		
		
		exitMenuItem.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		tabsMenu.addMenuListener(new MenuListener()
		{
			
			@Override
			public void menuSelected(MenuEvent arg0)
			{

				if(tabbedPane.getTabCount()>0)
				{
					removeTab.setText("Удалить вкладку: "+tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
					removeTab.setVisible(true);
				}
				else
				{
					removeTab.setVisible(false);
				}
			}
			
			@Override
			public void menuDeselected(MenuEvent arg0)
			{
			}
			
			@Override
			public void menuCanceled(MenuEvent arg0)
			{
			}
		});
		
		settingMenuItem.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				new SettingFrame(MainFrame.this.settingData, MainFrame.this).setVisible(true);
				MainFrame.this.acceptSetting();
			}
		});
		
		fileMenu.add(exitMenuItem);

		tabsMenu.add(updateTabs);
		
		helpMenu.add(aboutItemMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(tabsMenu);
		menuBar.add(helpMenu);
		
		this.setJMenuBar(this.menuBar);
		
        this.add(tabbedPane,BorderLayout.CENTER);
        this.add(this.topBannerImagePanel,BorderLayout.NORTH);
        
        setVisible(true);
        
		//=======================LOAD DATA BASE=========================
		this.threadData = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				updateData();		        
			}
		});
		
		threadData.setDaemon(true);
		threadData.start();
		
        if(editMode)
        {
        	fileMenu.removeAll();
        	fileMenu.add(settingMenuItem);
    		fileMenu.addSeparator();
    		fileMenu.add(exitMenuItem);
    		
    		tabsMenu.removeAll();
    		tabsMenu.add(updateTabs);
    		tabsMenu.add(addTab);
    		tabsMenu.add(removeTab);
    		
    		helpMenu.removeAll();
    		helpMenu.add(checkUpdate);
    		helpMenu.add(aboutItemMenu);
    		
    		menuBar.removeAll();
    		menuBar.add(fileMenu);
    		menuBar.add(tabsMenu);
    		menuBar.add(helpMenu);
        }

        this.acceptSetting();
        this.setLocationRelativeTo(null);
	}
	
	/**Применяются конфиги с файла*/
	private void acceptSetting()
	{
		WebImage topBannerImage = new WebImage(this.settingData.getPathTopBanner());
		topBannerImage.setDisplayType ( DisplayType.preferred );

		if(new File(this.settingData.getPathTopBanner()).exists())
		{
			this.topBannerImagePanel.removeAll();
			this.topBannerImagePanel.setVisible(true);
			this.topBannerImagePanel.add(topBannerImage, BorderLayout.CENTER);
		}
		else
		{
			this.topBannerImagePanel.setVisible(false);
		}
		
		if(this.settingData.isStaticSizeFrame())
		{
			this.setPreferredSize(new Dimension(this.settingData.getWidthSizeFrame(), this.settingData.getHeidthSizeFrame()));
		}
		else
		{
			this.pack();
		}
		
	}
	
	/**
	 * Проверка новой версии
	 */
	private synchronized void checkNewVersion()
	{
		if(!ftp.isConnected())
		{
			try
			{
				log.info("Подключение к FTP...");
				ftp.connectFTP();
			} 
			catch (SocketException e)
			{
				ftp.disconnectFTP();
				//loadDialog.setVisible(false);
				WebNotificationPopup notificationPopup = NotificationManager.showNotification (getContentPane(), "Не удалось соединиться с сервером для обновлений!", NotificationIcon.error.getIcon() );
				notificationPopup.setDisplayTime ( 5000 );
				
				return;
			} 
			catch (IOException e){}
		}
		
		boolean isNewVer = false;
		try
		{
			isNewVer = ftp.isNewVersion();
		} 
		catch (IOException e)
		{
			NotificationManager.showNotification (getContentPane(), "Не удалось проверить обновления!", NotificationIcon.error.getIcon() ).setDisplayTime ( 5000 );
			return;
		}

		if (isNewVer)
		{
			//log.info("Доступны обновления.");
			notifNewVersion.setVisible(true);
		}
		else
		{
			NotificationManager.showNotification (getContentPane(), "Программа не нуждается в обновлении",NotificationIcon.information.getIcon() ).setDisplayTime ( 5000 );
		}
	}
	
	/**
	 * Обновление/загрузка данных.
	 */
	private void updateData()
	{
		try
		{
			this.data = new DataBaseLabelsXml(this.settingData, PATH_DATA_BASE_LABELS);
			
		} 
		catch (IOException e)
		{
			WebOptionPane.showMessageDialog ( getOwner(), "Не удалось загрузить файл:" + this.data.getFileXml().getAbsolutePath(), "Ошибка", WebOptionPane.ERROR_MESSAGE );
			System.exit(1);
		}
		catch( SAXException | ParserConfigurationException e)
		{
			WebOptionPane.showMessageDialog ( getOwner(), "Не удалось правильно прочитать файл:" + this.data.getFileXml().getAbsolutePath(), "Ошибка", WebOptionPane.ERROR_MESSAGE );
			System.exit(1);
		}
		
		
		log.info("Загрузка компонентов...");
		
		final ArrayList<GroupDesktopLabels> groupDesktopLabels = this.data.getAllGroupDesktopLabels();
        
		int sizeText = 0;
		
		
		
		if (groupDesktopLabels.size() > 0)
		{
			for (int iGroup = 0; iGroup < groupDesktopLabels.size(); iGroup++)
			{
				GroupDesktopLabels groupLabels = groupDesktopLabels.get(iGroup);
				
				groupLabels.setEditMode(editMode);
				
				WebScrollPane scrollPanel = new WebScrollPane(groupLabels, false, true);
				
				String groupName = groupLabels.getGroupName();
				
				//log.info("Добовление компонента: "+ groupName);
				
				tabbedPane.add(groupName, scrollPanel);

				if (groupName.length() > sizeText)
				{
					sizeText = groupName.length();
				}
			}
							
			
			tabbedPane.setPreferredSize(new Dimension(600 + (sizeText * 10), (groupDesktopLabels.size()/3 + 150) * 5));
		}
		
		log.info("Загрузка закончена!");
		
		pack();
		setLocationRelativeTo(null);
		loadDialog.setVisible(false);
	}
	
}
