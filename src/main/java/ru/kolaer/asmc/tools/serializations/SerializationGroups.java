package ru.kolaer.asmc.tools.serializations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * (Де)сериализация объектов.
 * @author Danilov
 * @version 0.1
 */
public class SerializationGroups {
	private static final Logger LOG = LoggerFactory.getLogger(SerializationGroups.class);

	private final String pathDitSerializedObject = "data";
	private final String fileNameSerializeObjects = "objects.aer";
	private final File dir = new File(pathDitSerializedObject);
	private final File fileSer = new File(pathDitSerializedObject + "/" + fileNameSerializeObjects);
	private List<MGroupLabels> cacheObjects;

	public SerializationGroups() {
		
		if(!this.checkFile()){
			LOG.error("Не удалось создать файл: {}", fileSer.getAbsolutePath());
			System.exit(-9);
		}
	}

	/**
	 * Проверяет файл {@link #fileNameSerializeObjects} на его наличие.
	 * Иначе создает.
	 * @return true - если файл создан.
	 */
	private boolean checkFile() {
		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}

			if (!fileSer.exists()) {
				LOG.debug("Создание файла: {}", fileSer.getAbsolutePath());
				fileSer.createNewFile();
			}
		} catch (IOException e1) {
			LOG.error("Не удалось создать файл: {}", fileSer.getAbsolutePath());
			return false;
		}
		return true;
	}
	
	/**Получить сериализованные группы.*/
	public List<MGroupLabels> getSerializeGroups() {
		
		if(this.cacheObjects != null)
			return this.cacheObjects;
		
		if(!this.checkFile())
			LOG.error("Файл был удален: {}", fileSer.getAbsolutePath());
		
		try (FileInputStream fileInput = new FileInputStream(fileSer);
				ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
			try {
				if(this.cacheObjects == null) {
					 this.cacheObjects = new ArrayList<>();
					 final List<MGroupLabels> groupList = (List<MGroupLabels>) objectInput.readObject();
					 this.cacheObjects.addAll(groupList);
				}				
		
				return this.cacheObjects;
			} catch (ClassNotFoundException e) {
				LOG.error("Класс не найден!", e);
				return this.cacheObjects;
			}
		} catch (IOException e) {
			LOG.error("Не удалось открыть файл: " + fileSer.getAbsolutePath(), e);
			return this.cacheObjects;
		}
	}

	/**Сериализовать список групп.*/
	public void setSerializeGroups(List<MGroupLabels> groupModels) {
		
		if(!this.checkFile())
			LOG.error("Файл был удален: {}", fileSer.getAbsolutePath());
		
		try (FileOutputStream fileOutSer = new FileOutputStream(fileSer);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {

			objectOutputStream.writeObject(groupModels);

		} catch (FileNotFoundException e) {
			LOG.error("Не найден файл: " + fileSer.getAbsolutePath(), e);
		} catch (IOException e1) {
			System.exit(-9);
		}
	}
}
