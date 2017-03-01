package ru.kolaer.asmc.mvp.view;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.mvp.model.MLabel;

import java.io.File;
import java.util.Optional;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class VAddingLabelDialog {
	private final Logger LOG = LoggerFactory.getLogger(VAddingLabelDialog.class);
	
	private MLabel result;
	private final BorderPane mainPane;
	private final Button buttonSetFile;
	private final TextField nameLabelText;
	private final TextField infoLabelText;
	private final ImageView image;
	private final RadioButton rbNoneIcon;
	private final RadioButton rbDefaultIcon;
	private final Button buttonSetPathIcon;
	private final TextField pathIconText;
	private final TextField pathAppText;
	private final TextField textPriority;
	private final TextField pathOpenAppWith;
	private final Button buttonSetAppWith;
    private final Dialog<MLabel> dialog;

	public VAddingLabelDialog() {
		this(null);
	}

	public VAddingLabelDialog(final MLabel model) {
        this.mainPane = new BorderPane();
        this.buttonSetFile = new Button("Обзор...");
        this.buttonSetAppWith = new Button("Обзор...");
        this.nameLabelText = new TextField();
        this.infoLabelText = new TextField();
        this.image = new ImageView();
        this.rbNoneIcon = new RadioButton("Без иконки");
        this.rbDefaultIcon = new RadioButton("Стандартная иконка");
        this.buttonSetPathIcon = new Button("Указать путь к иконке...");
        this.pathIconText = new TextField();
        this.pathAppText = new TextField();
        this.textPriority = new TextField();
        this.pathOpenAppWith = new TextField();
        this.dialog = new Dialog<>();

        this.result = model;

        this.init();
	}

	private void init() {
        this.mainPane.setPadding(new Insets(5,10,5,10));

        this.image.setFitHeight(100);
        this.image.setFitWidth(100);

        final VBox rbPane = new VBox(this.rbNoneIcon,
                this.rbDefaultIcon,
                this.buttonSetPathIcon);
        rbPane.setPadding(new Insets(10));
        rbPane.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(this.rbNoneIcon, new Insets(0, 0, 10, 0));
        VBox.setMargin(this.rbDefaultIcon, new Insets(0, 0, 10, 0));
        VBox.setMargin(this.buttonSetPathIcon, new Insets(0, 0, 10, 0));

        final BorderPane iconPane = new BorderPane(rbPane);
        iconPane.setLeft(this.image);
        iconPane.setBottom(this.pathIconText);

        final GridPane contentPane = new GridPane();
        contentPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        contentPane.setVgap(10); //vertical gap in pixels
        contentPane.setPadding(new Insets(10));
        contentPane.setAlignment(Pos.TOP_LEFT);

        contentPane.add(new Label("Имя ярлыка:"), 0, 0);
        contentPane.add(this.nameLabelText, 1, 0);

        contentPane.add(new Label("Описание:"), 0, 1);
        contentPane.add(this.infoLabelText, 1, 1);

        contentPane.add(new Label("Иконка:"), 0, 2);
        contentPane.add(iconPane, 1, 2);

        contentPane.add(new Label("Путь к файлу/URL:"), 0, 3);
        contentPane.add(new FlowPane(this.pathAppText, this.buttonSetFile), 1, 3);

        contentPane.add(new Label("Открывать с помощью:"), 0, 4);
        contentPane.add(new FlowPane(this.pathOpenAppWith, this.buttonSetAppWith), 1, 4);

        contentPane.add(new Label("Приоритет:"), 0, 5);
        contentPane.add(this.textPriority, 1, 5);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setFillWidth(true);

        contentPane.getColumnConstraints().add(columnConstraints);

        columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);

        contentPane.getColumnConstraints().add(columnConstraints);

        this.mainPane.setCenter(contentPane);

        final String title = this.result == null ? "Добавить ярлык" : "Удалить ярлык";
        final ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        this.dialog.getDialogPane().getButtonTypes().add(new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE));
        this.dialog.getDialogPane().getButtonTypes().add(ok);
        this.dialog.getDialogPane().setContent(this.mainPane);
        this.dialog.setResultConverter(param -> {
            if(param == ok) {
                if(!this.textPriority.getText().matches("[0-9]*")) {
                    final Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Внимание!");
                    alert.setHeaderText("Приоритет может быть только числом!");
                    alert.show();
                    return null;
                }

                int prior = this.textPriority.getText().isEmpty() ? 0 : Integer.valueOf(textPriority.getText());

                if (this.result == null) {
                    this.result = new MLabel();
                }

                this.result.setName(this.nameLabelText.getText());
                this.result.setInfo(this.infoLabelText.getText());
                this.result.setPathImage(this.pathIconText.getText());
                this.result.setPathApplication(this.pathAppText.getText());
                this.result.setPriority(prior);
                this.result.setPathOpenAppWith(this.pathOpenAppWith.getText());

                return this.result;
            }

            return null;
        });

        this.dialog.setTitle(title);
        this.dialog.setResizable(true);
        ((Stage)this.dialog.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image(Resources.AER_ICON.toString()));

		this.buttonSetAppWith.setOnAction(e -> {
            final Optional<File> file = Optional
                    .ofNullable(this.initFileChooser(this.pathOpenAppWith.getText()).showOpenDialog(null));

			if (file.isPresent() && file.get().exists()) {
				this.pathOpenAppWith.setText(file.get().getAbsolutePath());
			}
		});
		
		this.buttonSetFile.setOnAction(e -> {
			final Optional<File> file = Optional
                    .ofNullable(this.initFileChooser(this.pathAppText.getText()).showOpenDialog(null));

			if (file.isPresent() && file.get().exists()) {
				this.pathAppText.setText(file.get().getAbsolutePath());
			}
		});

		this.pathIconText.setOnKeyPressed(key -> {
			if (key.getCode() != KeyCode.ENTER)
				return;

			Optional.of(new File(this.pathIconText.getText()))
                    .filter(File::exists)
                    .ifPresent(this::updateIcon);
		});

        this.buttonSetPathIcon.setOnAction(this::actionButtonSetPathIcon);
        this.rbNoneIcon.setOnAction(this::actionRBNoneIcon);
        this.rbDefaultIcon.setOnAction(this::actionRBDefaultIcon);
	}

    private FileChooser initFileChooser(String path) {
        final FileChooser fileC = new FileChooser();
        fileC.setTitle("Выбор файла");
        fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));

        final String pathToApp = path == null ? System.getProperty("user.home") : path;

        final File startDir = new File(pathToApp);
        final String startPath = startDir.isFile()
                ? startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length())
                : startDir.getAbsolutePath();

        fileC.setInitialDirectory(new File(startPath));

        return fileC;
    }

    private void actionRBNoneIcon(final ActionEvent event) {
		this.image.setImage(null);
		this.pathIconText.setText("");
	}

    private void actionRBDefaultIcon(final ActionEvent event) {
		this.image.setImage(new Image(Resources.AER_ICON.toString() ));
		this.pathIconText.setText(Resources.AER_ICON.toString());
	}

    private void actionButtonSetPathIcon(final ActionEvent event) {

        final FileChooser fileChooser = this.initFileChooser(this.pathIconText.getText());
        fileChooser.setTitle("Выбор иконки ярлыка");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));

		Optional.ofNullable(fileChooser.showOpenDialog(null))
                .filter(File::exists)
                .ifPresent(this::updateIcon);
	}

	private void updateIcon(File file) {
        this.rbDefaultIcon.setSelected(false);
        this.rbNoneIcon.setSelected(false);
        this.pathIconText.setText(file.getAbsolutePath());
        try {
            this.image.setImage(new Image(file.toURI().toURL().toString()));
        } catch (final Exception e) {
            LOG.error("Невозможно переконвертировать в URL файл: {}", file.getAbsolutePath());
        }
    }

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
            } else if (this.result.getPathImage().contains("bundle")) {
                this.image.setImage(new Image(Resources.AER_ICON.toString()));
                this.pathIconText.setText(Resources.AER_ICON.toString());
                this.rbDefaultIcon.setSelected(true);
                this.rbNoneIcon.setSelected(false);
            } else {
                Optional.of(new File(this.result.getPathImage()))
                        .filter(File::exists)
                        .ifPresent(this::updateIcon);
            }
        }

        return this.dialog.showAndWait();
    }
}
