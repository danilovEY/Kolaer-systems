package ru.kolaer.asmc.ui.javafx.controller;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class CAddingLabelDialog extends BaseController implements Dialog {

	private MLabel result;
	@FXML
	private Button buttonSetFile;
	@FXML
	private TextField nameLabelText;
	@FXML
	private TextField infoLabelText;
	@FXML
	private ImageView image;
	@FXML
	private RadioButton rbNoneIcon;
	@FXML
	private RadioButton rbDefaultIcon;
	@FXML
	private Button buttonSetPathIcon;
	@FXML
	private TextField pathIconText;
	@FXML
	private TextField pathAppText;
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	@FXML
	private TextField textPriority;
	@FXML
	private TextField pathOpenAppWith;
	@FXML
	private Button buttonSetAppWith;
	
	private final FileChooser fileChooser = new FileChooser();
	private final Stage dialog = new Stage();

	/**
	 * {@linkplain CAddingLabelDialog}
	 */
	public CAddingLabelDialog() {
		super(Resources.V_ADDING_LABEL);
		this.init();
	}

	/**
	 * {@linkplain CAddingLabelDialog}
	 * 
	 * @param model
	 */
	public CAddingLabelDialog(final MLabel model) {
		super(Resources.V_ADDING_LABEL);
		this.init();
		this.result = model;
	}

	private void init() {
		this.fileChooser.setTitle("Выбор иконки ярлыка");

		this.fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"));
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
		this.buttonSetAppWith.setOnAction(e -> {
			final FileChooser fileC = new FileChooser();
			fileC.setTitle("Выбор файла");
			fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));
			
			final String path = this.pathOpenAppWith.getText() == null ? System.getProperty("user.home") : this.pathOpenAppWith.getText();
			
			final File startDir = new File(path);
			String startPath = System.getProperty("user.home");
			if(startDir.isFile()) {
				startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
			} else {
				startPath = startDir.getAbsolutePath();
			}
			fileC.setInitialDirectory(new File(startPath));

			final Optional<File> file = Optional.ofNullable(fileC.showOpenDialog(dialog));

			if (file.isPresent() && file.get().exists()) {
				this.pathOpenAppWith.setText(file.get().getAbsolutePath());
			}
		});
		
		this.buttonSetFile.setOnAction(e -> {
			final FileChooser fileC = new FileChooser();
			fileC.setTitle("Выбор файла");
			fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));
			
			final String path = this.pathOpenAppWith.getText() == null ? System.getProperty("user.home") : this.pathOpenAppWith.getText();
			
			final File startDir = new File(path);
			String startPath = System.getProperty("user.home");
			if(startDir.isFile()) {
				startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
			} else {
				startPath = startDir.getAbsolutePath();
			}
			fileC.setInitialDirectory(new File(startPath));

			final Optional<File> file = Optional.ofNullable(fileC.showOpenDialog(dialog));

			if (file.isPresent() && file.get().exists()) {
				this.pathAppText.setText(file.get().getAbsolutePath());
			}
		});

		this.pathIconText.setOnKeyPressed(key -> {

			if (key.getCode() != KeyCode.ENTER)
				return;

			final Optional<File> file = Optional.of(new File(this.pathIconText.getText()));

			if (file.isPresent() && (file.get().exists() && file.get().isFile())) {
				this.rbDefaultIcon.setSelected(false);
				this.rbNoneIcon.setSelected(false);
				this.pathIconText.setText(new String(file.get().getAbsolutePath()));
				try {
					this.image.setImage(new Image(file.get().toURI().toURL().toString()));
				} catch (final Exception e) {
					final Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Ошибка!");
					alert.setHeaderText("Невозможно переконвертировать в URL файл:" + file.get().getAbsolutePath());
					alert.setContentText(e.toString());
					alert.showAndWait();
				}
				fileChooser.setInitialDirectory(file.get());
			}
		});
	}

	@FXML
	public void actionButtonOK(final ActionEvent event) {
		if(!this.textPriority.getText().matches("[0-9]*")) {
			final Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание!");
			alert.setHeaderText("Приоритет может быть только числом!");
			alert.show();
			return;
		}
		
		int prior = this.textPriority.getText().isEmpty() ? 0 : Integer.valueOf(textPriority.getText());
		
		if (this.result == null) {
			this.result = new MLabel(this.nameLabelText.getText(), this.infoLabelText.getText(),
					this.pathIconText.getText(), this.pathAppText.getText(), this.pathOpenAppWith.getText(), prior);
		} else {
			this.result.setName(this.nameLabelText.getText());
			this.result.setInfo(this.infoLabelText.getText());
			this.result.setPathImage(this.pathIconText.getText());
			this.result.setPathApplication(this.pathAppText.getText());
			this.result.setPriority(prior);
			this.result.setPathOpenAppWith(this.pathOpenAppWith.getText());
		}

		this.dialog.close();
	}

	@FXML
	public void actionButtonCancel(final ActionEvent event) {
		this.dialog.close();
	}

	@FXML
	public void actionRBNoneIcon(final ActionEvent event) {
		this.image.setImage(null);
		this.pathIconText.setText("");
	}

	@FXML
	public void actionRBDefaultIcon(final ActionEvent event) {
		this.image.setImage(new Image(Resources.AER_ICON.toString() ));
		this.pathIconText.setText(Resources.AER_ICON.toString());
	}

	@FXML
	public void actionButtonSetPathIcon(final ActionEvent event) {
		final File startDir = new File(this.pathIconText.getText());
		String startPath = System.getProperty("user.home");
		if(startDir.isFile()) {
			startPath = startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length());
		} else {
			startPath = startDir.getAbsolutePath();
		}
		this.fileChooser.setInitialDirectory(new File(startPath));

		final Optional<File> file = Optional.ofNullable(this.fileChooser.showOpenDialog(dialog));

		if (file.isPresent() && (file.get().exists() && file.get().isFile())) {
			this.rbDefaultIcon.setSelected(false);
			this.rbNoneIcon.setSelected(false);
			this.pathIconText.setText(new String(file.get().getAbsolutePath()));
			try {
				this.image.setImage(new Image(file.get().toURI().toURL().toString()));
			} catch (final Exception e) {
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Невозможно переконвертировать в URL файл:" + file.get().getAbsolutePath());
				alert.setContentText(e.toString());
				alert.showAndWait();
			}
			fileChooser.setInitialDirectory(file.get());
		}
	}

	@Override
	public Optional<MLabel> showAndWait() {
		if (this.result != null) {
			this.textPriority.setText(String.valueOf(this.result.getPriority()));
			this.nameLabelText.setText(this.result.getName());
			this.infoLabelText.setText(this.result.getInfo());
			this.pathAppText.setText(this.result.getPathApplication());
			this.pathOpenAppWith.setText(this.result.getPathOpenAppWith());
			if (this.result.getPathImage() == null || this.result.getPathImage().isEmpty()) {
				this.rbNoneIcon.setSelected(true);
				this.image.setImage(null);
				this.pathIconText.setText("");
			} else if (this.result.getPathImage().equals(Resources.AER_ICON)) {
				this.image.setImage(new Image(Resources.AER_ICON.toString()));
				this.pathIconText.setText(Resources.AER_ICON.toString());
				this.rbDefaultIcon.setSelected(true);
				this.rbNoneIcon.setSelected(false);
			} else {
				final File file = new File(this.result.getPathImage());

				if (file != null && file.exists() && file.isFile()) {
					this.rbDefaultIcon.setSelected(false);
					this.rbNoneIcon.setSelected(false);
					this.pathIconText.setText(new String(file.getAbsolutePath()));
					try {
						this.image.setImage(new Image(file.toURI().toURL().toString()));
					} catch (final Exception e) {
						final Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Ошибка!");
						alert.setHeaderText("Невозможно переконвертировать в URL файл:" + file.getAbsolutePath());
						alert.setContentText(e.toString());
						alert.showAndWait();
					}
				}
			}
		}

		final String title = this.result == null ? Resources.ADDING_LABEL_FRAME_TITLE : Resources.EDDING_LABEL_FRAME_TITLE;
		this.dialog.setTitle(title);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();

		try {
			this.dialog.getIcons().add(new Image("file:" + Resources.AER_LOGO.toString()));
		} catch (final IllegalArgumentException e) {
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \"" + Resources.AER_LOGO + "\"");
			alert.showAndWait();
		}

		this.dialog.showAndWait();

		return Optional.ofNullable(this.result);
	}
}
