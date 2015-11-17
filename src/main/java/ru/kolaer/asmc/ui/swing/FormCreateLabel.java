package ru.kolaer.asmc.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JRootPane;
import javax.swing.SwingConstants;

import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import com.alee.utils.swing.UnselectableButtonGroup;

import ru.kolaer.asmc.tools.Application;
import ru.kolaer.asmc.tools.DataBaseSettingXml;
import ru.kolaer.asmc.tools.Resources;

/**
 * Окно для создания/редактирования ярлыка.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class FormCreateLabel extends WebDialog
{
	/**Редактируемый режим.*/
	private boolean edit;
	
	/**Ярлык возвращаемый/редактируемый.*/
	private DesktopLabel label;
	
	/**Поле для названия ярлыка.*/
	private WebTextField nameLabelText;
	
	/**Иконка ярлыка.*/
	private  WebImage iconLabelImage;
	private  WebPanel iconLabelImagePanel;
	
	/**Флаг - без иконки.*/
	private WebRadioButton noneIcon;
	/**Флаг - иконка по-умолчанию.*/
	private WebRadioButton defaultIcon;
	/**Флаг - иконка из файла.*/
	private WebRadioButton fileIcon;
	
	/**Флаг - задача url.*/
	private WebRadioButton urlRadioBut;
	/**Флаг - задача из файла.*/
	private WebRadioButton appRadioBut;
	
	/**Путь к иконке.*/
	private WebTextField pathImageText;
	
	/**Файл для задачи.*/
	private WebFileChooserField fileChooser;
	/**Ссылка.*/
	private WebTextField urlText;
	
	/**Поле для информации о ярлыке.*/
	private  WebTextArea infoTextArea;
	
	private DataBaseSettingXml setting;
	
	/**Текст на кнопке.*/
	private String TEXT_MODE_LABEL = "";
	private final static String TEXT_EDIT_LABEL = "Редактировать ярлык";
	private final static String TEXT_CREATE_LABEL = "Создать ярлык";
	
	
	public FormCreateLabel(DataBaseSettingXml setting, JRootPane rootPanel,DesktopLabel label)
	{
		super(rootPanel,TEXT_EDIT_LABEL);
		this.TEXT_MODE_LABEL = TEXT_EDIT_LABEL;
		this.label = label;
		this.edit = true;
		this.setting = setting;
		init();
		editInit();
	}
	
	public FormCreateLabel(DataBaseSettingXml setting, JRootPane rootPanel)
	{
		super(rootPanel,TEXT_CREATE_LABEL);
		this.TEXT_MODE_LABEL = TEXT_CREATE_LABEL;
		this.label = null; 
		this.edit = false;
		this.setting = setting;
		init();
	}
	
	private void editInit()
	{
		//=========Title Name=============
		this.nameLabelText.setText(this.label.getTitleName());
		
		//=========Image=============
		if(this.label.getImage().equals("null"))
		{
			noneIcon.setSelected(true);
			iconLabelImage.setImage(new WebImage("").getImage());
			pathImageText.setText("null");
		}
		else
		{
			if(this.label.getImage().equals("default"))
			{
				defaultIcon.setSelected(true);
				iconLabelImage.setImage(new WebImage(Resources.AER_ICON).getImage());
				pathImageText.setText("default");
			}
			else
			{
				fileIcon.setSelected(true);
				iconLabelImage.setImage(new WebImage(this.label.getImage()).getImage());
				pathImageText.setText(this.label.getImage());
			}
		}
		
		//=========Apps=============
		if(Application.isURL(this.label.getApp()))
		{
			appRadioBut.setSelected(false);
			fileChooser.setVisible(false);
			urlRadioBut.setSelected(true);
			urlText.setText(this.label.getApp());
		}
		else
		{
			fileChooser.setSelectedFile(new File(this.label.getApp()));
		}
		
		//============Text Info==========
		infoTextArea.setText(this.label.getInfo());
		
	}
	
	private void init()
	{
		//==================Настройки формы======================
		this.setDefaultCloseOperation ( WebDialog.DISPOSE_ON_CLOSE );
		this.setResizable ( false );
		this.setModal ( true );
        
		//==================Основная форма======================
		TableLayout layout = new TableLayout ( new double[][]{ { TableLayout.FILL},
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL } } );
        layout.setHGap ( 15 );
        layout.setVGap ( 15 );
        
        WebPanel mainPanel = new WebPanel(layout);
        iconLabelImagePanel = new WebPanel(new BorderLayout());
        //================Named Labels=======================
        WebLabel nameLabel = new WebLabel("Название ярлыка");
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        //==================Поле для названия ярлыка======================
        nameLabelText = new WebTextField ( 15 );
        nameLabelText.setInputPrompt ( "Введите имя ярлыка..." );
        nameLabelText.setInputPromptFont ( nameLabelText.getFont ().deriveFont ( Font.ITALIC ) );
        
        //=================Group Panel=======================
        WebPanel nameLabelPanel = new WebPanel(new BorderLayout());
        nameLabelPanel.add(nameLabel,BorderLayout.NORTH);
        nameLabelPanel.add(nameLabelText,BorderLayout.CENTER);
        
        //================Icon Labels=======================
        iconLabelImage = new WebImage(Resources.AER_ICON);
        iconLabelImage.setDisplayType ( DisplayType.fitComponent );
        iconLabelImage.setPreferredSize ( new Dimension(64, 64) );
        iconLabelImagePanel.add(iconLabelImage,BorderLayout.CENTER);
        //=============Path Image======================
        pathImageText = new WebTextField ("default");
        pathImageText.setEditable(false);
        
        //============Без иконки================
        noneIcon = new WebRadioButton ( "Без иконки" );
        noneIcon.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pathImageText.setText("null");
				pathImageText.setEditable(false);
				iconLabelImage.setImage( new WebImage("").getImage());
			}
		});
        
        //===============Иконка по-умолчанию================
        defaultIcon = new WebRadioButton ( "Стандартная" );
        //defaultIcon.setSelected ( true );
        defaultIcon.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pathImageText.setText("default");
				pathImageText.setEditable(false);
				//iconLabelImage.setImage( new WebImage(Resources.AER_ICON).getImage());
				iconLabelImage = new WebImage(Resources.AER_ICON);
		        iconLabelImage.setDisplayType ( DisplayType.fitComponent );
		        iconLabelImage.setPreferredSize ( new Dimension(64, 64) );
		        iconLabelImagePanel.removeAll();
		        iconLabelImagePanel.add(iconLabelImage,BorderLayout.CENTER);
		        iconLabelImagePanel.updateUI();
			}
		});
        
        //===============Иконка из файла================
        fileIcon = new WebRadioButton ( "Из файла" );
        fileIcon.addActionListener ( new ActionListener ()
        {
            private WebFileChooser fileChooser = new WebFileChooser();
            private File file = null;

            @Override
            public void actionPerformed ( ActionEvent e )
            {
            	fileChooser.setMultiSelectionEnabled ( false );
            	fileChooser.setAcceptAllFileFilterUsed ( false );
            	fileChooser.addChoosableFileFilter ( GlobalConstants.IMAGES_FILTER );
            	
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
                    pathImageText.setEditable(false);
                    pathImageText.setText(file.getPath());
    				iconLabelImage = new WebImage(pathImageText.getText());
    		        iconLabelImage.setDisplayType ( DisplayType.fitComponent );
    		        iconLabelImage.setPreferredSize ( new Dimension(64, 64) );
    		        iconLabelImagePanel.removeAll();
    		        iconLabelImagePanel.add(iconLabelImage,BorderLayout.CENTER);
    		        iconLabelImagePanel.updateUI();
                }
            }
        } );
        
        pathImageText.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyTyped(KeyEvent e)
			{
				iconLabelImage = new WebImage(pathImageText.getText());
		        iconLabelImage.setDisplayType ( DisplayType.fitComponent );
		        iconLabelImage.setPreferredSize ( new Dimension(64, 64) );
		        iconLabelImagePanel.removeAll();
		        iconLabelImagePanel.add(iconLabelImage,BorderLayout.CENTER);
		        iconLabelImagePanel.updateUI();
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
			}
		});

        
      //===============Ввести путь к иконки================
        WebRadioButton enterIcon = new WebRadioButton ( "Ввести путь вручную" );
        enterIcon.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pathImageText.setEditable(true);
			}
		});
        
        UnselectableButtonGroup.group(noneIcon,defaultIcon,fileIcon,enterIcon);
        
        WebLabel iconLabel = new WebLabel("Иконка");
        iconLabel.setForeground(Color.BLACK);
        iconLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        WebPanel iconLabelPanel = new WebPanel(); 
        iconLabelPanel.setLayout(new BorderLayout());
        iconLabelPanel.add(iconLabel,BorderLayout.NORTH);
        iconLabelPanel.add(iconLabelImagePanel,BorderLayout.CENTER);
        iconLabelPanel.add(new GroupPanel(4,false,noneIcon,defaultIcon,fileIcon,enterIcon),BorderLayout.EAST);
        iconLabelPanel.add(pathImageText,BorderLayout.SOUTH);
        
        //==================Apps Label=======================
        fileChooser = new WebFileChooserField ( getOwner() );
        fileChooser.setPreferredWidth ( 200 );
        fileChooser.setVisible(true);
        
        urlText = new WebTextField ("");
        urlText.setPreferredWidth ( 200 );
        urlText.setEnabled(false);
        
        appRadioBut = new WebRadioButton("Файл");
        appRadioBut.setSelected(true);
        appRadioBut.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileChooser.setVisible(true);
				urlText.setEnabled(false);
			}
		});
        
        GroupPanel chooseFile = new GroupPanel(0,false,appRadioBut,fileChooser);
        
        urlRadioBut = new WebRadioButton("Ссылка");
        urlRadioBut.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileChooser.setVisible(false);
				urlText.setEnabled(true);
			}
		});
        
        GroupPanel chooseURL = new GroupPanel(0,false,urlRadioBut,urlText);
        
        UnselectableButtonGroup.group(appRadioBut,urlRadioBut);
        
        WebLabel openAsLabel = new WebLabel("Открывать...");
        openAsLabel.setForeground(Color.BLACK);
        openAsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        openAsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        WebPanel appLabelPanel = new WebPanel(new BorderLayout());
        appLabelPanel.add(openAsLabel,BorderLayout.NORTH);
        appLabelPanel.add(new GroupPanel(10,false,chooseFile,chooseURL),BorderLayout.CENTER);
        
        //=====================Info================================
        infoTextArea = new WebTextArea ("");
        infoTextArea.setLineWrap ( true );
        infoTextArea.setWrapStyleWord ( true );
        
        WebScrollPane textInfoScroll = new WebScrollPane ( infoTextArea );
        textInfoScroll.setPreferredSize ( new Dimension ( 200, 150 ) );
        
        WebLabel infoLabel = new WebLabel("Описание");
        infoLabel.setForeground(Color.BLACK);
        infoLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        WebPanel infoPanel =new WebPanel(new BorderLayout());
        infoPanel.add(infoLabel,BorderLayout.NORTH);
        infoPanel.add(textInfoScroll,BorderLayout.CENTER);
        infoPanel.setVisible(true);
        
        //========================Button===========================
        WebButton createLabel = new WebButton(TEXT_MODE_LABEL);
        createLabel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean showErr = false;
				String messErr = "";
				
				String titleName = nameLabelText.getText();

				String app = null;
				
				if (urlRadioBut.isSelected())
				{
					app = urlText.getText();
					
					if(!Application.isURL(app))
					{
						showErr = true;
						messErr += "-Ссылка имеет неправильный формат. Возможно нет добовления: \"http://\".\n";
					}
					else
					{
						app = urlText.getText();
					}
				} else
				{
					if (fileChooser.getSelectedFiles().size() > 0)
						app = fileChooser.getSelectedFiles().get(0).getPath();
					else
					{
						showErr = true;
						messErr += "-Не выбран файл..\n";
					}
				}

				String image = pathImageText.getText();

				String info = infoTextArea.getText();
				
				if(showErr)
				{
					WebOptionPane.showMessageDialog ( getOwner(), messErr, "Ошибка", WebOptionPane.ERROR_MESSAGE );
					return;
				}
				
				if (!edit)
				{
					label = new DefaultDesktopLabel(FormCreateLabel.this.setting, titleName, app, image, info);
					
				} 
				else
				{
					label.setTitleName(titleName);
					label.setApp(app);
					label.setImage(image);
					label.setInfo(info);
					label.update();
				}
				
				label.setEditMode(true);
				
				setVisible(false);
				//dispose();
			}
		});
        
        WebButton cancel = new WebButton("Отмена");
        cancel.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				//dispose();
			}
		});
        
        WebPanel butPanel = new WebPanel(new FlowLayout());
        butPanel.add(createLabel);
        butPanel.add(cancel);
        
        
        mainPanel.add(nameLabelPanel,"0,0");  
        mainPanel.add(iconLabelPanel,"0,1");
        mainPanel.add(appLabelPanel, "0,2");
        mainPanel.add(infoPanel,     "0,3");
        mainPanel.add(butPanel,      "0,4");
        
        this.add(mainPanel,BorderLayout.CENTER);
        
        pack();
        this.setLocationRelativeTo(null);
	}
	
	public DesktopLabel getCreatedDesktopLabel()
	{
		return this.label;
	}

}
