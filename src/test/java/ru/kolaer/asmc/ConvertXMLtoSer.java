package ru.kolaer.asmc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.serializations.SerializationGroups;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

public class ConvertXMLtoSer {

	public static final String ROOT_DESKTOP_LABELS = "DesktopLables";
	public static final String LABEL_GROUP = "GroupLabel";
	public static final String LABEL = "DesktopLable";


	/** Атрибут в xml для названия ярлыка. */
	private static final String NAME_ATR = "titleName";
	/** Атрибут в xml для пути к иконки. */
	private static final String IMAGE_PATH_ATR = "imagePaph";
	/** Атрибут в xml для пути к задаче. */
	private static final String APP_PATH_ATR = "appPath";
	/** Атрибут в xml для описания ярлыка. */
	private static final String INFO_ATR = "info";

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
		Document document = db.parse(new File("src/test/resources/Data.xml"));

		NodeList desktopLabelList = document.getElementsByTagName(LABEL_GROUP);

		SerializationGroups ser = new SerializationGroups();
		List<MGroupLabels> listGroups = new ArrayList<>();
		
		// List of GroupLabel
		for (int je = 0; je < desktopLabelList.getLength(); je++) {
			Node nodeGroup = desktopLabelList.item(je);

			if (nodeGroup.getNodeType() == Node.ELEMENT_NODE) {
				Element groupEl = (Element) nodeGroup;
				String name = groupEl.getAttribute("name");
				MGroupLabels group = new MGroupLabels(name, je);
				
				System.out.println("Group name: " + name);
				// List of Label
				NodeList labelList = groupEl.getElementsByTagName(LABEL);
				for (int i = 0; i < labelList.getLength(); i++) {
					Element labelElement = (Element) labelList.item(i);
					String titleName = labelElement.getAttribute(NAME_ATR);
					String app = labelElement.getAttribute(APP_PATH_ATR);
					String image = labelElement.getAttribute(IMAGE_PATH_ATR);
					image = image.substring("\\\\kolaer.local\\asup$\\ico\\".length());
					image = "\\\\kolaer.local\\asup$\\ASMC-v1.0-bin\\resources\\" + image.replaceFirst(".png", ".gif").replaceAll(".PNG", ".gif");
					
					//image = Resources.AER_ICON;
					String info = labelElement.getAttribute(INFO_ATR);
					System.out.printf("Label: %s %s %s %s ", titleName, info, image, app);
					group.addLabel(new MLabel(titleName, info, image, app, i));
				}
				listGroups.add(group);
			}
		}
		
		ser.setSerializeGroups(listGroups);

	}

}
