package ru.kolaer.asmc.ui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;
import com.alee.utils.SwingUtils;

import ru.kolaer.asmc.tools.DataBaseSettingXml;

/**
 * Окно с настройками.
 * @author Danilov E.Y.
 *
 */
public class SettingFrame extends WebDialog
{
	private static final long serialVersionUID = -3880632350993293410L;
	private static final Logger log = LoggerFactory.getLogger(SettingFrame.class);
	
	private final static String TITLE_FRAME = "Настройки";
	
	private DataBaseSettingXml dataBaseSettingXml;
	
	private MainFrame mainFrame;
	
	public SettingFrame(DataBaseSettingXml settingXml,MainFrame mainFrame)
	{
		super(mainFrame,TITLE_FRAME);
		
		this.mainFrame = mainFrame;
		this.dataBaseSettingXml = settingXml;
		
		this.init();
	}

	private void init() 
	{
		//==================Настройки формы======================
		this.setDefaultCloseOperation ( WebDialog.DISPOSE_ON_CLOSE );
		this.setResizable ( true );
		this.setModal ( true );
		
		
		//==================Основная форма======================
		TableLayout layout = new TableLayout ( new double[][]{ { TableLayout.FILL},
                { TableLayout.PREFERRED, 
			TableLayout.PREFERRED, 
			TableLayout.PREFERRED, 
			TableLayout.PREFERRED, TableLayout.FILL } } );
        layout.setHGap ( 30 );
        layout.setVGap ( 5 );
        
        WebPanel mainPanel = new WebPanel(layout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        Border sizeBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        
        
        //==========TOP BANNER==============
        final WebTextField pathTopBanner = new WebTextField(15);
        pathTopBanner.setText(this.dataBaseSettingXml.getPathTopBanner());
        
        WebButton selectImageTopBanner = new WebButton("Обзор");
        selectImageTopBanner.addActionListener ( new ActionListener ()
        {
            private WebFileChooser fileChooser = new WebFileChooser();
            private File file = null;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
            	fileChooser.setCurrentDirectory(pathTopBanner.getText());
            	fileChooser.setMultiSelectionEnabled ( false );
            	fileChooser.setAcceptAllFileFilterUsed ( false );
            	fileChooser.addChoosableFileFilter ( com.alee.global.GlobalConstants.IMAGES_FILTER );
            	
                if ( fileChooser == null )
                {
                    fileChooser = new WebFileChooser ();
                    fileChooser.setMultiSelectionEnabled ( false );
                }
                if ( file != null )
                {
                    fileChooser.setSelectedFile ( file );
                }
                if ( fileChooser.showOpenDialog ( getOwner() ) == WebFileChooser.APPROVE_OPTION )
                {
                    file = fileChooser.getSelectedFile ();
                    pathTopBanner.setEditable(true);
                    pathTopBanner.setText(file.getPath());
                }
            }
        } );
        
        Border settingsPanel = BorderFactory.createTitledBorder(null, 
        		"Верхний баннер", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 12));
        settingsPanel = BorderFactory.createCompoundBorder(
        		settingsPanel, sizeBorder);
        
        WebPanel topBannerSetting = new WebPanel(new BorderLayout());
        topBannerSetting.add(pathTopBanner,BorderLayout.CENTER);
        topBannerSetting.add(selectImageTopBanner, BorderLayout.EAST); 
        topBannerSetting.setBorder(settingsPanel);
        
        //==========WEB BROWSER==============
        final WebTextField pathWebBrowser = new WebTextField(15);
        pathWebBrowser.setText(this.dataBaseSettingXml.getPathWebBrowser());
        
        WebButton selectWebBrowser = new WebButton("Обзор");
        selectWebBrowser.addActionListener ( new ActionListener ()
        {
            private WebFileChooser fileChooser = new WebFileChooser();
            private File file = null;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
            	fileChooser.setCurrentDirectory(pathWebBrowser.getText());
            	fileChooser.setMultiSelectionEnabled ( false );
            	fileChooser.setAcceptAllFileFilterUsed ( false );
            	
                if ( fileChooser == null )
                {
                    fileChooser = new WebFileChooser ();
                    fileChooser.setMultiSelectionEnabled ( false );
                }
                if ( file != null )
                {
                    fileChooser.setSelectedFile ( file );
                }
                if ( fileChooser.showOpenDialog ( getOwner() ) == WebFileChooser.APPROVE_OPTION )
                {
                    file = fileChooser.getSelectedFile ();
                    pathWebBrowser.setEditable(true);
                    pathWebBrowser.setText(file.getAbsolutePath());
                }
            }
        } );
        
        settingsPanel = BorderFactory.createTitledBorder(null, 
        		"Путь к веб браузеру", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 12));
        settingsPanel = BorderFactory.createCompoundBorder(
        		settingsPanel, sizeBorder);
        
        WebPanel webBrowserSetting = new WebPanel(new BorderLayout());
        webBrowserSetting.add(pathWebBrowser,BorderLayout.CENTER);
        webBrowserSetting.add(selectWebBrowser, BorderLayout.EAST);
        webBrowserSetting.setBorder(settingsPanel);
        
        //================FRAME SIZE==================
        final WebButton autoSizeFrameBut = new WebButton("Автоматический размер окна");
        final WebButton staticSizeFrameBut = new WebButton("Зафиксированный размер окна");
        staticSizeFrameBut.setEnabled(false);
        autoSizeFrameBut.setEnabled(false);
        
        if(!this.dataBaseSettingXml.isStaticSizeFrame())
        	staticSizeFrameBut.setEnabled(true);
        else
        	autoSizeFrameBut.setEnabled(true);
        
        ActionListener radioBut = new ActionListener()
        {
        	@Override
			public void actionPerformed(ActionEvent e)
			{
				if(staticSizeFrameBut.isEnabled())
				{
					autoSizeFrameBut.setEnabled(true);
					staticSizeFrameBut.setEnabled(false);
				}
				else
				{
					autoSizeFrameBut.setEnabled(false);
					staticSizeFrameBut.setEnabled(true);
				}
			}
        };
        
        autoSizeFrameBut.addActionListener(radioBut);
        staticSizeFrameBut.addActionListener(radioBut);
        
        SwingUtils.equalizeComponentsWidths(autoSizeFrameBut,staticSizeFrameBut);

        settingsPanel = BorderFactory.createTitledBorder(null, 
        		"Размер окна", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 12));
        settingsPanel = BorderFactory.createCompoundBorder(
        		settingsPanel, sizeBorder);
        
        WebPanel frameSizeSetting = new WebPanel(new BorderLayout());
        frameSizeSetting.add(new GroupPanel(2,true, autoSizeFrameBut, staticSizeFrameBut),BorderLayout.CENTER);
        frameSizeSetting.setBorder(settingsPanel);
        
        WebButton okSetting = new WebButton("OK");
        WebButton cancelSetting = new WebButton("Отмена");
        WebButton acceptSetting = new WebButton("Применить");
        
        cancelSetting.addActionListener(new ActionListener() 
        {	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				SettingFrame.this.setVisible(false);
			}
		});
        
        okSetting.addActionListener(new ActionListener() 
        {	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				SettingFrame.this.dataBaseSettingXml.setPathTopBanner(pathTopBanner.getText());
				SettingFrame.this.dataBaseSettingXml.setPathWebBrowser(pathWebBrowser.getText());

				if(!staticSizeFrameBut.isEnabled())
				{
					SettingFrame.this.dataBaseSettingXml.setStaticSizeFrame(true);
					SettingFrame.this.dataBaseSettingXml.setWidthSizeFrame(SettingFrame.this.mainFrame.getWidth());
					SettingFrame.this.dataBaseSettingXml.setHeidthSizeFrame(SettingFrame.this.mainFrame.getHeight());
				}
				else
					SettingFrame.this.dataBaseSettingXml.setStaticSizeFrame(false);
				
				try 
				{
					SettingFrame.this.dataBaseSettingXml.save();
				} 
				catch (IOException e) 
				{
					log.error("Не удалось сохранить настройки!");
					WebOptionPane.showMessageDialog(SettingFrame.this.getRootPane(), "Не удалось сохранить настройки!", "Ошибка!", WebOptionPane.ERROR_MESSAGE);
				}
				
				SettingFrame.this.dispose();
			}
		});
        
        
        WebPanel butPanel = new WebPanel(new FlowLayout());
        butPanel.add(okSetting);
        butPanel.add(cancelSetting);
        //butPanel.add(acceptSetting);

        mainPanel.add(topBannerSetting,"0,0");
        mainPanel.add(webBrowserSetting,"0,1");
        mainPanel.add(frameSizeSetting, "0,2");
        mainPanel.add(butPanel,"0,4");
        
        this.add(mainPanel, BorderLayout.CENTER);
        
        this.pack();
        this.setLocationRelativeTo(null);
        
        
	}
}
