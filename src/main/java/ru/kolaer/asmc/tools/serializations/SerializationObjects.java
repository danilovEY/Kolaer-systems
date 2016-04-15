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

import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * (Де)сериализация объектов.
 * @author Danilov
 * @version 0.1
 */
public class SerializationObjects {
	private final Logger LOG = LoggerFactory.getLogger(SerializationObjects.class);
	
	private final String pathDitSerializedObject = "data";
	private final String fileNameSerializeObjects = "objects.aer";
	private final File settingFile = new File("setting.aer");
	private File fileSer = new File(pathDitSerializedObject + "/" + fileNameSerializeObjects);
	private List<MGroupLabels> cacheObjects;

	public SerializationObjects() {

	}
	
	public void setSerializeSetting(final SettingSingleton setting) {
		if(!this.settingFile.exists()) {
			try {
				this.settingFile.createNewFile();
			} catch (final IOException e2) {
				LOG.error("Невозможно создать файл: {}", this.settingFile.getAbsolutePath(), e2);
			}
		}
		
		try (final FileOutputStream fileOutSer = new FileOutputStream(this.settingFile);
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {
			objectOutputStream.writeObject(setting);
		} catch (final FileNotFoundException e) {
			LOG.error("Не найден файл: {}", this.settingFile.getAbsolutePath(), e);
		} catch (final IOException e1) {
			LOG.error("Ошибка: {}", this.settingFile.getAbsolutePath(), e1);
			System.exit(-9);
		}
	}
	
	public SettingSingleton getSerializeSetting() {	
		if(!this.settingFile.exists())
			return SettingSingleton.getInstance();
		
		if(SettingSingleton.getInstance() != null) {
			return SettingSingleton.getInstance();
		}
		
		try (final FileInputStream fileInput = new FileInputStream(this.settingFile);
				final ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
			return (SettingSingleton) objectInput.readObject();			
		} catch (final ClassNotFoundException e) {
			LOG.error("Класс не найден!", e);
		} catch (final IOException e1) {
			LOG.error("Ошибка!", e1);
		}
		return SettingSingleton.getInstance();
	}
	
	/**Получить сериализованные группы.*/
	@SuppressWarnings("unchecked")
	public synchronized List<MGroupLabels> getSerializeGroups() {
		if(this.cacheObjects != null)
			return this.cacheObjects;
		if(!this.fileSer.exists()) {
			return Collections.emptyList();
		}
		try(FileInputStream fileInput = new FileInputStream(this.fileSer); ObjectInputStream objectInput = new ObjectInputStream(fileInput)){
			if(objectInput.available() != -1){
				final List<MGroupLabels> groupList = (List<MGroupLabels>) objectInput.readObject();
				this.cacheObjects = new ArrayList<>(groupList);
			}
			return this.cacheObjects;
		}catch(final ClassNotFoundException e){
			LOG.error("Класс не найден!", e);
			return Collections.emptyList();
		}
		catch(final Exception e){
			LOG.error("Ошибка!", e);
			return Collections.emptyList();
		}
	}

	/**Сериализовать список групп.*/
	public void setSerializeGroups(final List<MGroupLabels> groupModels) {
		final File newSerObj = new File(pathDitSerializedObject + "/" + fileNameSerializeObjects);
		if(newSerObj.exists()) {
			newSerObj.delete();
		}
			
		try{
			newSerObj.createNewFile();
		} catch(final IOException e2){
			LOG.error("Не удалось создать файл: {}", newSerObj.getAbsolutePath(), e2);
		}
		
		try (final FileOutputStream fileOutSer = new FileOutputStream(newSerObj);
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {
			objectOutputStream.writeObject(groupModels);
		} catch (final FileNotFoundException e) {
			LOG.error("Не найден файл: {}", newSerObj.getAbsolutePath());
		} catch (IOException e1) {
			LOG.error("Ошибка! Создается локальная база!", e1);
			try (final FileOutputStream fileOutSer = new FileOutputStream(new File("C:\\Temp\\" + fileNameSerializeObjects));
					final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {
				objectOutputStream.writeObject(groupModels);
			} catch (final Exception ex) {
				LOG.error("Ошибка!", ex);
				System.exit(-9);
			}
		}
	}
}
