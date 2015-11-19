package ru.kolaer.asmc;

import java.util.ArrayList;
import java.util.List;

import ru.kolaer.asmc.tools.serializations.SerializationGroups;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

public class TestSer {
	public static void main(String[] argv) {

		SerializationGroups ser = new SerializationGroups();
		List<MGroupLabels> list = new ArrayList<>();
		MGroupLabels group = new MGroupLabels();
		group.setNameGroup("AAA");
		group.addLabel(new MLabel("123", "456", "789", "000"));

		
		list.add(group);
		
		ser.setSerializeGroups(list);

		
		ser = new SerializationGroups();
		list = ser.getSerializeGroups();

		list.forEach((g) -> {
			System.out.println(g.getNameGroup());
			g.getLabelList().forEach((l) -> {
				System.out.println(l.getName());
			});
		});
	}
}
