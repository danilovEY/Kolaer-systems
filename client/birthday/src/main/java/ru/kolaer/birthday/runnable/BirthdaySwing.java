package ru.kolaer.birthday.runnable;

import ru.kolaer.client.core.plugins.services.Service;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BirthdaySwing implements Service {

	public static void main(String[] args) {
		new BirthdaySwing().run();
	}

	private static String getNameOrganization(final String org) {
		switch(org) {
			case "БалаковоАтомэнергоремонт":
				return "БалАЭР";
			case "ВолгодонскАтомэнергоремонт":
				return "ВДАЭР";
			case "КалининАтомэнергоремонт":
				return "КАЭР";
			case "КурскАтомэнергоремонт":
				return "КурскАЭР";
			case "ЛенАтомэнергоремонт":
				return "ЛенАЭР";
			case "НововоронежАтомэнергоремонт":
				return "НВАЭР";
			case "СмоленскАтомэнергоремонт":
				return "САЭР";
			case "УралАтомэнергоремонт":
				return "УралАЭР";
			case "Центральный аппарат":
				return "ЦА";
			case "КолАтомэнергоремонт":
				return "КолАЭР";
			default:
				return org;
		}
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(() -> {
			List<String> list = new ArrayList<>();
			try{
				URL oracle = new URL("http://aerkl-s-app02:8080/employees/get/birthday/today");

		        BufferedReader in = new BufferedReader(
		        new InputStreamReader(oracle.openStream(), "UTF-8"));
		        String text = "";
		        String inputLine;
		        while ((inputLine = in.readLine()) != null)
		        	text += inputLine;	        
		        in.close();
		        String user = "";
		        
		        for(String date : text.split(",")){
		        	if(date.startsWith("\"initials\"")) {
		        		list.add(date.split(":")[1].replaceAll("\"", "") + " (КолАЭР)");
		        		System.out.println(date.split(":")[1].replaceAll("\"", "") + " (КолАЭР)");
		        	}  	
		        }
		        
		        oracle = new URL("http://aerkl-s-app02:8080/organizations/employees/get/users/birthday/today");
				
		        in = new BufferedReader(
		        new InputStreamReader(oracle.openStream(), "UTF-8"));
		        text = "";
				while ((inputLine = in.readLine()) != null)
		        	text += inputLine;	        
		        in.close();
		        System.out.println(text);
		        String org = "";
		        for(String date : text.split(",")){
		        	if(date.startsWith("\"organization\"")) {
						org = " (" + getNameOrganization(date.split(":")[1].replaceAll("\"", "")) + ")";
		        	}

		        	if(date.startsWith("\"initials\"")) {
						user = date.split(":")[1].replaceAll("\"", "");
						list.add(user + org);
					}
		        }
			} catch(Exception e1){
				e1.printStackTrace();
			}
			
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			final StringBuilder title = new StringBuilder("Сегодня \"").append(LocalDate.now().format(formatter)).append("\". Поздравляем с днем рождения!\n");

			JPanel listPane = new JPanel();
			listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
			
			JLabel label = new JLabel(title.toString());
			label.setFont(new Font("Serif", Font.PLAIN, 18));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			listPane.add(label);
			listPane.add(new JLabel("\n"));

			for(String name : list) {
				try{
					JLabel lab = new JLabel(new String(name.getBytes()));
					lab.setAlignmentX(Component.CENTER_ALIGNMENT);
					listPane.add(lab);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			JPanel listButtonPane = new JPanel();
			listButtonPane.setLayout(new BoxLayout(listButtonPane, BoxLayout.X_AXIS));
			
			JButton buttonOpenAsup = new JButton("Открыть АСУП");
			buttonOpenAsup.addActionListener(a -> {
				final Runtime r = Runtime.getRuntime();
				try{
					r.exec("cmd /C \"\\\\kolaer.local\\asup$\\ASUP-plugins\\АСУП-КолАЭР.lnk\"");
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			
			JButton buttonClose = new JButton("Закрыть");
			buttonClose.addActionListener(a -> {
				System.exit(0);
			});		
			
			listButtonPane.add(buttonOpenAsup);
			listButtonPane.add(buttonClose);
			
			listPane.add(listButtonPane);
			JFrame frame = new JFrame("День рождения!");
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/aerIcon.png")));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
			frame.setResizable(false);
			//frame.setUndecorated(true);
			frame.setContentPane(listPane);
			frame.pack();
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(dim.width-frame.getSize().width - 50, dim.height-frame.getSize().height - 50);
			frame.setVisible(true);

		});
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public String getName() {
		return "Глобальная служба день рождения";
	}

	@Override
	public void stop() {
		
	}
}
