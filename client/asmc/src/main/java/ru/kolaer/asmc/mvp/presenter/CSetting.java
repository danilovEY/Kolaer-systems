package ru.kolaer.asmc.mvp.presenter;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CSetting extends BaseController {

	private final Stage dialog = new Stage();
	
	@FXML
	private RadioButton rbDefaultWB;
	@FXML
	private RadioButton rbDefaultUserWB;
	@FXML
	private RadioButton rbSetWB;
	@FXML
	private TextField textPathWB;
	@FXML
	private Button buttonSetPathWB;
	@FXML
	private CheckBox cbAllLabels;
	
	@FXML
	private TextField textPathBanner;
	@FXML
	private Button buttonSetPathBanner;
	
	@FXML
	private TextField textPathBannerLeft;
	@FXML
	private Button buttonSetPathBannerLeft;
	
	@FXML
	private TextField textPathBannerRigth;
	@FXML
	private Button buttonSetPathBannerRigth;
	
	@FXML
	private PasswordField textPassRoot;
	@FXML
	private Button changePassButton;
	
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	
	
	public CSetting() {
		super(Resources.V_SETTING);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		this.cancelButton.setOnAction(e -> {
			this.dialog.close();
		});
		
		this.textPathWB.setText(SettingSingleton.getInstance().getPathWebBrowser());
		this.textPathBanner.setText(SettingSingleton.getInstance().getPathBanner());
		this.textPathBannerLeft.setText(SettingSingleton.getInstance().getPathBannerLeft());	
		this.textPathBannerRigth.setText(SettingSingleton.getInstance().getPathBannerRigth());	
		
		if(SettingSingleton.getInstance().isDefaultWebBrowser()) {
			this.rbDefaultUserWB.setSelected(false);
			this.rbDefaultWB.setSelected(true);
			this.rbSetWB.setSelected(false);
			this.textPathWB.setDisable(true);
			this.buttonSetPathWB.setDisable(true);
		} else if(SettingSingleton.getInstance().isDefaultUserWebBrowser()) {
			this.rbDefaultWB.setSelected(false);
			this.rbSetWB.setSelected(false);
			this.rbDefaultUserWB.setSelected(true);
			this.textPathWB.setDisable(true);
			this.buttonSetPathWB.setDisable(true);
		} else {
			this.rbDefaultUserWB.setSelected(false);
			this.rbDefaultWB.setSelected(false);
			this.rbSetWB.setSelected(true);
			this.textPathWB.setDisable(false);
			this.buttonSetPathWB.setDisable(false);
		}
		
		if(SettingSingleton.getInstance().isAllLabels()) {
			this.cbAllLabels.setSelected(true);
		} else {
			this.cbAllLabels.setSelected(false);
		}
		
		this.rbDefaultUserWB.setOnAction(e -> {
			this.rbDefaultWB.setSelected(false);
			this.rbSetWB.setSelected(false);
			this.textPathWB.setDisable(true);
			this.buttonSetPathWB.setDisable(true);
		});
		
		this.rbDefaultWB.setOnAction(e -> {
			this.rbDefaultUserWB.setSelected(false);
			this.rbSetWB.setSelected(false);
			this.textPathWB.setDisable(true);
			this.buttonSetPathWB.setDisable(true);
		});
		
		this.rbSetWB.setOnAction(e -> {
			this.rbDefaultUserWB.setSelected(false);
			this.rbDefaultWB.setSelected(false);
			this.textPathWB.setDisable(false);
			this.buttonSetPathWB.setDisable(false);
		});
		
		this.buttonSetPathBanner.setOnAction(e -> {
			final FileChooser fileC = new FileChooser();
			fileC.setTitle("Выбор файла");
			fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));
			
			final File startDir = new File(this.textPathBanner.getText());
			String startPath = System.getProperty("user.home");
			if(startDir.isFile()) {
				startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
			} else {
				startPath = startDir.getAbsolutePath();
			}
			fileC.setInitialDirectory(new File(startPath));

			final Optional<File> file = Optional.ofNullable(fileC.showOpenDialog(dialog));

			if (file.isPresent() && file.get().exists()) {
				this.textPathBanner.setText(file.get().getAbsolutePath());
			}
		});
		
		this.buttonSetPathBannerLeft.setOnAction(e -> {
			final FileChooser fileC = new FileChooser();
			fileC.setTitle("Выбор файла");
			fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));
			
			final File startDir = new File(this.textPathBannerLeft.getText());
			String startPath = System.getProperty("user.home");
			if(startDir.isFile()) {
				startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
			} else {
				startPath = startDir.getAbsolutePath();
			}
			fileC.setInitialDirectory(new File(startPath));

			final Optional<File> file = Optional.ofNullable(fileC.showOpenDialog(dialog));

			if (file.isPresent() && file.get().exists()) {
				this.textPathBannerLeft.setText(file.get().getAbsolutePath());
			}
		});
		
		this.buttonSetPathBannerRigth.setOnAction(e -> {
			final FileChooser fileC = new FileChooser();
			fileC.setTitle("Выбор файла");
			fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));
			
			final File startDir = new File(this.textPathBannerRigth.getText());
			String startPath = System.getProperty("user.home");
			if(startDir.isFile()) {
				startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
			} else {
				startPath = startDir.getAbsolutePath();
			}
			fileC.setInitialDirectory(new File(startPath));

			final Optional<File> file = Optional.ofNullable(fileC.showOpenDialog(dialog));

			if (file.isPresent() && file.get().exists()) {
				this.textPathBannerRigth.setText(file.get().getAbsolutePath());
			}
		});
		
		this.buttonSetPathWB.setOnAction(e -> {
			final FileChooser fileC = new FileChooser();
			fileC.setTitle("Выбор файла");
			fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));
			
			final File startDir = new File(this.textPathWB.getText());
			String startPath = System.getProperty("user.home");
			if(startDir.isFile()) {
				startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
			} else {
				startPath = startDir.getAbsolutePath();
			}
			fileC.setInitialDirectory(new File(startPath));

			final Optional<File> file = Optional.ofNullable(fileC.showOpenDialog(dialog));

			if (file.isPresent() && file.get().exists()) {
				this.textPathWB.setText(file.get().getAbsolutePath());
			}
		});
		
		this.changePassButton.setOnAction(e -> {
			SettingSingleton.getInstance().setRootPass(this.textPassRoot.getText());
			final Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Пароль сохранен!");
			alert.setHeaderText("Пароль успешно изменен!");
			alert.showAndWait();
			SettingSingleton.getInstance().saveSetting();
		});
		
		this.okButton.setOnAction(e -> {
			final SettingSingleton set = SettingSingleton.getInstance();
			set.setAllLabels(this.cbAllLabels.isSelected());
			set.setDefaultWebBrowser(this.rbDefaultWB.isSelected());
			set.setPathBanner(this.textPathBanner.getText());
			set.setPathBannerLeft(this.textPathBannerLeft.getText());
			set.setPathBannerRigth(this.textPathBannerRigth.getText());
			set.setPathWebBrowser(this.textPathWB.getText());
			set.setDefaultUserWebBrowser(this.rbDefaultUserWB.isSelected());
			SettingSingleton.getInstance().saveSetting();
			this.dialog.close();
		});
	}


	public Optional<?> showAndWait() {
		final String title = Resources.SETTING_LABEL_FRAME_TITLE;
		this.dialog.setTitle(title);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();

		try {
			this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		} catch (final IllegalArgumentException e) {
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \"" + Resources.AER_LOGO + "\"");
			alert.showAndWait();
		}

		this.dialog.showAndWait();

		return Optional.empty();
	}
}
