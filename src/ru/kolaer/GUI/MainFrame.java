
package ru.kolaer.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ru.kolaer.tools.FTPUpload;
import ru.kolaer.tools.XMLFile;

import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
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
	/**Названия окна.*/
	private final String TITLE_NAME = "АЭР - Десктоп Менеджер";
	
	/**Меню панель окна.*/
	private WebMenuBar menuBar;
	
	/**БД в формате xml.*/
	private XMLFile data;
	
	/**FTP для обновлений.*/
	private FTPUpload ftp;
	
	/**Редактирекмый режим.*/
	public boolean editMode = false;
	
	private final WebNotificationPopup notifNewVersion = NotificationManager
			.showNotification("Доступна новая версия. Загрузить?",
					NotificationIcon.question.getIcon(),
					NotificationOption.yes, NotificationOption.no);
	
	/**Ключ для включения редактируемого режима.*/
	private final String accesKey = "-root_set";
	
	/**Ключ для вычисления md5 суммы.*/
	private final String makeMD5Hash = "-makeMD5Hash";
	
	public MainFrame(String[] args)
	{	
		super();
		
		if(args.length>0)
		{
			if(args[0].equals(accesKey))
			{
				editMode = true;
			}
			else if(args[0].equals(makeMD5Hash))
			{
				String md5 = FTPUpload.checkSum("AER-DecktopManager.jar");
				if (md5 != null)
				{
					File md5Hash = new File(md5 + ".md5");
					try
					{
						md5Hash.createNewFile();
					} 
					catch (IOException e)
					{
						System.exit(1);
					}
				}
				System.exit(0);
			}
		}
		
		init();
	}
	
	private void init()
	{
		Image img = new ImageIcon(Resources.AER_LOGO).getImage();
		
		
	    this.setIconImage(img);
	    this.setTitle(TITLE_NAME);
		this.setSize(new Dimension(500, 500));
		//this.setMaximumSize(new Dimension(600, 600));
		this.setDefaultCloseOperation ( WebFrame.DISPOSE_ON_CLOSE );
	    
		this.setTopBg(new Color(100, 100, 100));
		this.setMiddleBg(new Color(100, 100, 100));
		this.getContentPane().setBackground(new Color(100, 100, 100));
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if(editMode)
				{
					data.save();
					ftp.disconnectFTP();
				}
			}
		});
		
		
		
		// ===============Notifycations=================
		notifNewVersion.setVisible(false);

		notifNewVersion.addNotificationListener(new NotificationListener()
		{

			@Override
			public void optionSelected(NotificationOption option)
			{
				if (option.equals(NotificationOption.yes))
				{
					new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							LoadingFrame loadDialog = new LoadingFrame(getRootPane(), "Загрузка обновлений");
							
							loadDialog.setVisible(true);
							
							boolean isDownload = ftp.downloadNewVersion();
							
							loadDialog.setVisible(false);
							
							if(isDownload)
							{
								WebOptionPane.showMessageDialog(null, "Загруженно! Путь к новой версии: \""+
							new File(ftp.getPathNewVer()).getAbsolutePath()+"\"");
							}	
						}
					}).start();
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
		final WebTabbedPane tabbedPane = new WebTabbedPane ();
		//tabbedPane.setPreferredSize ( new Dimension ( 600, 500 ) );
		tabbedPane.setTabPlacement ( WebTabbedPane.LEFT );
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 15));
		tabbedPane.setTopBg(new Color(150, 150, 150));
		tabbedPane.setBackground(new Color(150, 150, 150));
		tabbedPane.setBottomBg(new Color(150, 150, 150));
		//tabbedPane.setForeground(Color.RED); - цвет текста
		
		//=============Menu bar==================
		menuBar = new WebMenuBar();
		menuBar.setUndecorated ( true );
		menuBar.setBackground(new Color(100, 100, 100));
				
		WebMenu fileMenu = new WebMenu ( "Файл" );
		WebMenu tabsMenu = new WebMenu("Вкладки");
		WebMenu helpMenu = new WebMenu("Справка");
		
		WebMenuItem checkUpdate = new WebMenuItem("Проверить наличие обновлений");
		checkUpdate.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{				
				new Thread(new Runnable()
				{					
					@Override
					public void run()
					{
						LoadingFrame loadDialog = new LoadingFrame(getRootPane(), "Проверка обновлений");
						
						loadDialog.setVisible(true);
						


						if (ftp.isNewVersion())
						{
							loadDialog.setVisible(false);
							notifNewVersion.setVisible(true);
						}
						else
						{
							loadDialog.setVisible(false);
							NotificationManager.showNotification ( "Программа не нуждается в обновлении" );
						}
					}
				}).start();
			
			}
		});
		
		WebMenuItem addTab = new WebMenuItem("Добавить вкладку");
		addTab.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				FormCreateGroupDesktopLabels form = new FormCreateGroupDesktopLabels(getRootPane());
				form.setLocationRelativeTo(getRootPane());
				form.pack();
				form.setVisible(true);

				GroupDesktopLabels groupLabels = form.getCreatedGroupLabels();

				if(groupLabels!=null)
				{
					groupLabels.setXmlElement(data.creatGroupDesktopLabels());
					groupLabels.updataXML();
					groupLabels.setEditMode(editMode);
					WebScrollPane scrollPanel = new WebScrollPane(groupLabels, false, true);
					tabbedPane.add(groupLabels.getGroupName(),scrollPanel);
				}
			}
		});
		
		final WebMenuItem removeTab = new WebMenuItem("Удалить вкладку");
		removeTab.setVisible(false);
			
		removeTab.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				WebScrollPane scPanel = (WebScrollPane) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
				if(scPanel == null)
				{
					return;
				}
				GroupDesktopLabels groupLabels = (GroupDesktopLabels) scPanel.getViewport().getView();
				
				if(WebOptionPane.showConfirmDialog ( getOwner(), "Вы действительно хотите удалить вкладку \""+groupLabels.getGroupName()+"\" и все ее ярлыки?",
						"Удалить вкладку?",
                        WebOptionPane.YES_NO_OPTION,
                        WebOptionPane.QUESTION_MESSAGE ) == WebOptionPane.YES_OPTION)
				{
				tabbedPane.remove(tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));
				groupLabels.getXmlElement().getParentNode().removeChild(groupLabels.getXmlElement());
				}
			
			}
		});
		
		WebMenuItem exitMenuItem = new WebMenuItem("Выход");
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
				
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		
		tabsMenu.add(addTab);
		tabsMenu.add(removeTab);
		
		helpMenu.add(checkUpdate);
		
		menuBar.add(fileMenu);
		menuBar.add(tabsMenu);
		menuBar.add(helpMenu);
		

        if(editMode)
        {
        	this.add(this.menuBar,BorderLayout.NORTH);
        	
        	this.ftp = new FTPUpload();
        	this.ftp.connectFTP();
    		
        	
        }
		
        this.add(tabbedPane,BorderLayout.CENTER);
        
		//=======================LOAD DATA BASE=========================
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				data = new XMLFile("Data.xml");
				final ArrayList<GroupDesktopLabels> groupDesktopLabels = data.getAllGroupDesktopLabels();
		        
				int sizeText = 0;
				
				LoadingFrame loadDialog = new LoadingFrame(getRootPane(), "Загрузка компонентов");
				
				if (groupDesktopLabels.size() > 0)
				{
					loadDialog.setVisible(true);

					for (int iGroup = 0; iGroup < groupDesktopLabels.size(); iGroup++)
					{
						GroupDesktopLabels groupLabels = groupDesktopLabels.get(iGroup);
						
						groupLabels.setEditMode(editMode);
						
						WebScrollPane scrollPanel = new WebScrollPane(groupLabels, false, true);
						
						String groupName = groupLabels.getGroupName();
						
						synchronized (tabbedPane)
						{
							tabbedPane.add(groupName, scrollPanel);
						}

						if (groupName.length() > sizeText)
						{
							sizeText = groupName.length();
						}
					}
									
					
					tabbedPane.setPreferredSize(new Dimension(
							600 + (sizeText * 10), 500));
					pack();
					setLocationRelativeTo(null);

					loadDialog.setVisible(false);
				}
		        
			}
		}).start();

        setLocationRelativeTo(null);
	}
	
}
