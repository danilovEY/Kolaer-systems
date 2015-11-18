package ru.kolaer.asmc.tools.serializations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.asmc.ui.javafx.model.GroupLabelsModel;

public class SerializationGroups {
	private static final Logger LOG = LoggerFactory.getLogger(SerializationGroups.class);

	private final String pathDitSerializedObject = "data";
	private final String fileNameSerializeObjects = "objects.aer";
	private final File dir = new File(pathDitSerializedObject);
	private final File fileSer = new File(pathDitSerializedObject + "/" + fileNameSerializeObjects);

	public SerializationGroups() {

		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}

			if (!fileSer.exists()) {
				LOG.debug("Создание файла: {}", fileSer.getAbsolutePath());
				fileSer.createNewFile();
			}
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

	public List<GroupLabelsModel> getSerializeGroups() {

		try (FileInputStream fileInput = new FileInputStream(fileSer);
				ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
			try {
				List<GroupLabelsModel> groupList = (List<GroupLabelsModel>) objectInput.readObject();
				return groupList;
			} catch (ClassNotFoundException e) {
				LOG.error("Класс не найден!", e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return Collections.emptyList();
	}

	public void setSerializeGroups(List<GroupLabelsModel> groupModels) {

		try (FileOutputStream fileOutSer = new FileOutputStream(fileSer);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {

			objectOutputStream.writeObject(groupModels);

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

	}

}
