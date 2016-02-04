package ru.kolaer.client.javafx;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.kolaer.server.dao.entities.DbBirthdayAll;
import ru.kolaer.server.restful.controller.request.obj.RequestDbBirthdayAllList;

public class RestTest {

	public static void main(String[] args) throws InterruptedException {
		RestTemplate restTemplate = new RestTemplate();
		

		/*RequestDbBirthdayAllList CO = new RequestDbBirthdayAllList(getDataFromXML("ca_list.xml", "Центральный аппарат"));
		RequestDbBirthdayAllList bal = new RequestDbBirthdayAllList(getDataFromXML("balakovo.xml", "БалаковоАтомэнергоремонт"));
		RequestDbBirthdayAllList vol = new RequestDbBirthdayAllList(getDataFromXML("volgodonsk.xml", "ВолгодонскАтомэнергоремонт"));
		RequestDbBirthdayAllList udo = new RequestDbBirthdayAllList(getDataFromXML("udomlya.xml", "КалининАтомэнергоремонт"));
		RequestDbBirthdayAllList kur = new RequestDbBirthdayAllList(getDataFromXML("kurchatov.xml", "КурскАтомэнергоремонт"));
		RequestDbBirthdayAllList sos = new RequestDbBirthdayAllList(getDataFromXML("sosnoviyBor.xml", "ЛенАтомэнергоремонт"));
		RequestDbBirthdayAllList nov = new RequestDbBirthdayAllList(getDataFromXML("novovoroneg.xml", "НововоронежАтомэнергоремонт"));
		RequestDbBirthdayAllList des = new RequestDbBirthdayAllList(getDataFromXML("desnogorsk.xml", "СмоленскАтомэнергоремонт"));
		RequestDbBirthdayAllList zar = new RequestDbBirthdayAllList(getDataFromXML("zarechny.xml", "УралАтомэнергоремонт"));
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", CO, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("bal");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", bal, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("vol");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", vol, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("udo");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", udo, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("kur");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", kur, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("sos");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", sos, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("nov");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", nov, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("des");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", des, RequestDbBirthdayAllList.class);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("zar");
		restTemplate.postForObject("http://js:8080/kolaer/database/birthdayAll/set/users/list", zar, RequestDbBirthdayAllList.class);*/
	}
	
	public static List<DbBirthdayAll> getDataFromXML(String path, String organizaton) {
		List<DbBirthdayAll> list = new ArrayList<>();

		try{
			final File xmlFile = new File(path);
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document doc = db.parse(xmlFile);

			doc.getDocumentElement().normalize();

			final NodeList nodeList = doc.getElementsByTagName("z:row");
			for(int i = 0; i < nodeList.getLength(); i++){
				final Node node = nodeList.item(i);
				if(Node.ELEMENT_NODE == node.getNodeType()){
					final Element element = (Element) node;
					final String categ = element.getAttribute("ows__x041a__x0430__x0442__x0435__x04");
					if(categ.equals("Рабочий"))
						continue;
					final String initial = element.getAttribute("ows_LinkTitleNoMenu");
					final String post = element.getAttribute("ows_JobTitle");
					final String dep = element.getAttribute("ows_Department");
					final String phone = element.getAttribute("ows_PrimaryNumber");
					final String mobilePhone = element.getAttribute("ows_CellPhone");
					final String email = element.getAttribute("ows_EMail");
					final String birthday = element.getAttribute("ows_Birthday");
					final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					final DbBirthdayAll user = new DbBirthdayAll();
					user.setOrganization(organizaton);
					user.setBirthday(sdf.parse(birthday));
					user.setCategoryUnit(categ);
					user.setDepartament(dep);
					user.setEmail(email);
					user.setInitials(initial);
					user.setMobilePhone(mobilePhone);
					user.setPhone(phone);
					user.setPost(post);
					list.add(user);
				}
			}
		}catch(ParserConfigurationException | SAXException | IOException ex){
			ex.printStackTrace();
		}catch(ParseException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return list;
	}

}
