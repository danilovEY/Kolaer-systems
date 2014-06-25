package ru.kolaer.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import org.w3c.dom.Element;

import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.extended.painter.BorderPainter;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.utils.SwingUtils;

/**
 * Стандартный вид ярлыка.
 * @author Danilov E.Y.
 *
 */
@SuppressWarnings("serial")
public class DefaultDesktopLabel extends DesktopLabel
{	
	/**Запуск задач.*/
	private ApplicationInt application;
	
	/**Название ярлыка.*/
	private WebLabel labelTitle;
	
	/**Иконка ярлыка.*/
	private WebImage iconLabel;
	
	/**Панель я информацией о ярлыке.*/
	private WebTextArea editPanel;
	
	/**Системная панель.*/
	private WebPanel sysPanel;
	
	protected DefaultDesktopLabel()
	{
		super();
		initialize();
	}
	
	/**
	 * 
	 * @param titleName - Название ярлыка.
	 * @param app - Путь для запуска.
	 * @param image - Путь для иконки.
	 * @param info - Информация.
	 */
	protected DefaultDesktopLabel(String titleName, String app,
			String image, String info)
	{
		super(titleName, app, image, info);
		initialize();
	}

	public DefaultDesktopLabel(Element element)
	{
		super(element);		
		initialize();
	}

	private void initialize()
	{	
		this.application = new Application(this.app);
		
		//==============System Panel==================
		sysPanel = new WebPanel();
		sysPanel.setLayout(new BorderLayout());
		sysPanel.setBackground(new Color(50,50,50));
		sysPanel.setVisible(false);
		
		//=============Button Edit Label===============
		WebButton editLabel = new WebButton("Редактировать ярлык");
		editLabel.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FormCreateLabel form = new FormCreateLabel(getRootPane(), getThis());
				form.pack();
				form.setVisible(true);
				
				update();
				updataXML();
			}
		});

		
		//=============Button Del Label===============
		WebButton removeLabel = new WebButton("Удалить ярлык");
		removeLabel.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(xmlLabelElement!=null)
				  xmlLabelElement.getParentNode().removeChild(xmlLabelElement);
				setVisible(false);
			}
		});
		
		
		//=============Sys Button group===============
		WebButtonGroup sysButGroup = new WebButtonGroup ( true, editLabel, removeLabel );
		SwingUtils.equalizeComponentsWidths(editLabel,removeLabel);

		//==============Desktop Panel=================
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setBackground(new Color(50,50,50));
		Dimension sizeDLabel = new Dimension(400, 130);
		this.setPreferredSize(sizeDLabel);
		this.setMinimumSize(sizeDLabel);
		
		//==============Border for Main panel=================
		@SuppressWarnings("rawtypes")
		BorderPainter borderPainterMainPanel = new BorderPainter ();
		borderPainterMainPanel.setColor(new Color ( 39, 95, 173 ));
		
		//==============Main panel=================
		WebPanel mainPanel = new WebPanel(new BorderLayout());
		mainPanel.setPainter( borderPainterMainPanel ).setMargin(5);
		mainPanel.setBackground(new Color(50,50,50));
		
		//==============Title Label=================
		this.labelTitle  = new WebLabel(this.titleName);
		this.labelTitle.setForeground(Color.YELLOW);
		this.labelTitle.setFont(new Font("Tahoma", Font.BOLD, 17));
		this.labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//==============Panel for Button and TextInfo=================
		WebPanel contPanel = new WebPanel();
		contPanel.setLayout(new BoxLayout(contPanel, BoxLayout.Y_AXIS));
		
		//==============Button=================
		WebButton runBut = new WebButton("Открыть");
		runBut.setBottomBgColor(new Color(20,20,20));
		runBut.setTopBgColor(new Color(100,100,100));
		runBut.setShadeColor(Color.YELLOW);
		runBut.setForeground(Color.WHITE);
		runBut.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		runBut.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				application.start();
			}
		});
		
		//==============TextInfo=================
		this.editPanel = new WebTextArea();
		this.editPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.editPanel.setEditable(false);
		this.editPanel.setText(this.info);
		this.editPanel.setLineWrap ( true );
		this.editPanel.setWrapStyleWord ( true );

        WebScrollPane textInfoScroll = new WebScrollPane ( this.editPanel );
        textInfoScroll.setPreferredSize ( new Dimension ( 200, 150 ) );
        
        //==============Image=================
        iconLabel = new WebImage(this.image);
        if(this.image.equals("default"))
        {
        	iconLabel = new WebImage(Resources.AER_ICON);
        }
        iconLabel.setDisplayType ( DisplayType.fitComponent );
        iconLabel.setPreferredSize ( new Dimension(100, 100) );
        if(this.image.equals(""))
        {
        	iconLabel.setVisible(false);
        }
		
        //==============Panel for icon=================
        WebPanel iconPanel = new WebPanel(new BorderLayout());
        iconPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        iconPanel.setBackground(new Color(50,50,50));
        iconPanel.add(iconLabel);
        
		//==============Panel for Button=================
		WebPanel runButPanel = new WebPanel(new BorderLayout());
		runButPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		runButPanel.setBackground(new Color(50,50,50));
		runButPanel.add(runBut);
		
		//==============Panel for TextInfo=================
		WebPanel panelEditPanel = new WebPanel(new BorderLayout());
		panelEditPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelEditPanel.setBackground(new Color(50,50,50));
		panelEditPanel.add(textInfoScroll);
		
		//==============System Panel=================
		WebPanel sysAndLabelPanel = new WebPanel(new BorderLayout());
		sysAndLabelPanel.setBackground(new Color(50,50,50));
		sysAndLabelPanel.add(sysPanel,BorderLayout.CENTER);
		sysAndLabelPanel.add(this.labelTitle,BorderLayout.NORTH);
		
		
		
		sysPanel.add(sysButGroup,BorderLayout.LINE_END);
		
		contPanel.add(runButPanel);
		contPanel.add(panelEditPanel);
		
		mainPanel.add(sysAndLabelPanel,BorderLayout.NORTH);
		mainPanel.add(contPanel,BorderLayout.CENTER);
		mainPanel.add(iconPanel,BorderLayout.WEST);
		
		this.add(mainPanel);
	}

	public void setEditMode(boolean mode)
	{
		this.sysPanel.setVisible(mode);
	}
	
	private DesktopLabel getThis()
	{
		return this;
	}

	@Override
	public void update()
	{
		this.labelTitle.setText(this.titleName);
		this.iconLabel.setImage(new WebImage(this.image).getImage());
		this.application = new Application(this.app);
		this.editPanel.setText(this.info);
	}

	@Override
	public boolean isEditMode()
	{
		return this.sysPanel.isVisible();
	}
}
