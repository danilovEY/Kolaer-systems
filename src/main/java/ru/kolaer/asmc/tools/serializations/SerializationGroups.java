package ru.kolaer.asmc.tools.serializations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * (Де)сериализация объектов.
 * @author Danilov
 * @version 0.1
 */
public class SerializationGroups {

	private final String pathDitSerializedObject = "data";
	private final String fileNameSerializeObjects = "objects.aer";
	private final File dir = new File(pathDitSerializedObject);
	private File fileSer = null;
	private List<MGroupLabels> cacheObjects;

	public SerializationGroups() {
		
		if(!this.checkFile()){
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Не удалость создать файл для объектов!");
				alert.showAndWait();
				System.exit(-9);
			});
		}
	}

	/**
	 * Проверяет файл {@link #fileNameSerializeObjects} на его наличие.
	 * Иначе создает.
	 * @return true - если файл создан.
	 */
	private boolean checkFile() {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final File[] files = dir.listFiles();
		long lastTimeMod = -1;
		this.fileSer = null;
		for(File file : files) {
			if(lastTimeMod < file.lastModified() && file.isFile() && file.getName().endsWith("aer")) {
				lastTimeMod = file.lastModified();
				this.fileSer = file;
			}
		}
		
		if(files.length >= 10) {
			for(File file : files) {
				if(file != this.fileSer)
					file.delete();
			}
		}
		
		if (this.fileSer == null) {
			this.fileSer = new File(pathDitSerializedObject + "/" + fileNameSerializeObjects);
			try (FileOutputStream fileOutSer = new FileOutputStream(this.fileSer);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {
			} catch (FileNotFoundException e) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Ошибка!");
					alert.setHeaderText("Не найден файл: " + this.fileSer.getAbsolutePath());
					alert.showAndWait();
				});
			} catch (IOException e2) {
				System.exit(-9);
			}
		}
		return true;
	}
	
	/**Получить сериализованные группы.*/
	public List<MGroupLabels> getSerializeGroups() {
		
		if(this.cacheObjects != null)
			return this.cacheObjects;
		
		if(!this.checkFile()) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Файл был удален: " + this.fileSer.getAbsolutePath());
				alert.showAndWait();
			});
		}

		try (FileInputStream fileInput = new FileInputStream(this.fileSer);
				ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
			try {
				if(this.cacheObjects == null) {
					 this.cacheObjects = new ArrayList<>();
					 if(objectInput.available() != -1) {
						 final List<MGroupLabels> groupList = (List<MGroupLabels>) objectInput.readObject();
						 this.cacheObjects.addAll(groupList);
					 }
				}				
				return this.cacheObjects;
			} catch (ClassNotFoundException e) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Ошибка!");
					alert.setHeaderText("Класс не найден!" + this.fileSer.getAbsolutePath());
					alert.setContentText(e.getMessage());
					alert.showAndWait();
				});
				return this.cacheObjects;
			}
		} catch (IOException e) {
			this.fileSer.delete();
			this.fileSer = null;
			return this.getSerializeGroups();
		}
	}

	/**Сериализовать список групп.*/
	public void setSerializeGroups(List<MGroupLabels> groupModels) {

		File newSerObj = new File(pathDitSerializedObject + "/" + new SimpleDateFormat("MM.dd.yyyy_HH.mm.ss").format(System.currentTimeMillis())
				+ "_" + fileNameSerializeObjects);
		try{
			newSerObj.createNewFile();
		}
		catch(IOException e2){
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Не удалось создать файл: " + newSerObj.getAbsolutePath());
				alert.setContentText(e2.getMessage());
				alert.showAndWait();
			});
		}
		
		try (FileOutputStream fileOutSer = new FileOutputStream(newSerObj);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutSer)) {

			objectOutputStream.writeObject(groupModels);

		} catch (FileNotFoundException e) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Не найден файл: " + newSerObj.getAbsolutePath());
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			});
		} catch (IOException e1) {
			System.exit(-9);
		}
	}
}
