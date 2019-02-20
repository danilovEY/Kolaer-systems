package ru.kolaer.asmc.mvp.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Danilov
 * @version 0.1
 */
@Slf4j
public class AddingLabelDialogVc {

	private static Dialog<MLabel> initialization(final MLabel mLabel) {
        BorderPane mainPane = new BorderPane();
        Button buttonSetFile = new Button("Обзор...");
        Button buttonSetAppWith = new Button("Обзор...");
        TextField nameLabelText = new TextField();
        TextField infoLabelText = new TextField();
        ImageView image = new ImageView();
        RadioButton rbNoneIcon = new RadioButton("Без иконки");
        RadioButton rbDefaultIcon = new RadioButton("Стандартная иконка");
        ButtonBase buttonSetPathIcon = new Button("Указать путь к иконке...");
        TextField pathIconText = new TextField();
        TextField pathAppText = new TextField();
        TextField textPriority = new TextField();
        TextField pathOpenAppWith = new TextField();
        CheckBox autoRun = new CheckBox("Автозапуск");
        Dialog<MLabel> dialog = new Dialog<>();

        mainPane.setPadding(new Insets(5,10,5,10));

        image.setFitHeight(100);
        image.setFitWidth(100);

        ToggleGroup group = new ToggleGroup();
        rbNoneIcon.setToggleGroup(group);
        rbDefaultIcon.setToggleGroup(group);

        VBox rbPane = new VBox(rbNoneIcon,
                rbDefaultIcon,
                buttonSetPathIcon);
        rbPane.setPadding(new Insets(10));
        rbPane.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(rbNoneIcon, new Insets(0, 0, 10, 0));
        VBox.setMargin(rbDefaultIcon, new Insets(0, 0, 10, 0));
        VBox.setMargin(buttonSetPathIcon, new Insets(0, 0, 10, 0));

        BorderPane iconPane = new BorderPane(rbPane);
        iconPane.setLeft(image);
        iconPane.setBottom(pathIconText);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setFillWidth(true);

        GridPane contentPane = new GridPane();
        contentPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        contentPane.setVgap(10); //vertical gap in pixels
        contentPane.setPadding(new Insets(10));
        contentPane.setAlignment(Pos.TOP_LEFT);

        contentPane.add(new Label("Имя ярлыка:"), 0, 0);
        contentPane.add(nameLabelText, 1, 0);

        contentPane.add(new Label("Описание:"), 0, 1);
        contentPane.add(infoLabelText, 1, 1);

        contentPane.add(new Label("Иконка:"), 0, 2);
        contentPane.add(iconPane, 1, 2);

        contentPane.add(new Label("Путь к файлу/URL:"), 0, 3);
        contentPane.add(new FlowPane(pathAppText, buttonSetFile), 1, 3);

        contentPane.add(new Label("Открывать с помощью:"), 0, 4);
        contentPane.add(new FlowPane(pathOpenAppWith, buttonSetAppWith), 1, 4);

        contentPane.add(new Label("Приоритет:"), 0, 5);
        contentPane.add(textPriority, 1, 5);

        contentPane.add(new Label("Автозапуск:"), 0, 6);
        contentPane.add(autoRun, 1, 6);

        contentPane.getColumnConstraints().add(columnConstraints);

        mainPane.setCenter(contentPane);

        String title = mLabel == null ? "Добавить ярлык" : "Удалить ярлык";
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().setContent(mainPane);
        dialog.setResultConverter(param -> {
            if(param == ok) {
                String priorityInText = textPriority.getText();

                if(!priorityInText.matches("[0-9]*")) {
                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getDialog()
                            .createWarningDialog("Внимание!", "Приоритет может быть только числом!");
                    return null;
                }

                int priority = 0;

                try {
                    priority = Integer.valueOf(priorityInText);
                } catch (NumberFormatException ignore) {}

                MLabel result = mLabel;

                if (result == null) {
                    result = new MLabel();
                }

                result.setName(nameLabelText.getText());
                result.setInfo(infoLabelText.getText());
                result.setPathImage(pathIconText.getText());
                result.setPathApplication(pathAppText.getText());
                result.setPriority(priority);
                result.setPathOpenAppWith(pathOpenAppWith.getText());
                result.setAutoRun(autoRun.isSelected());

                return result;
            }

            return null;
        });

        Consumer<File> updateIcon = file -> {
            rbDefaultIcon.setSelected(false);
            rbNoneIcon.setSelected(false);
            pathIconText.setText(file.getAbsolutePath());
            try {
                image.setImage(new Image(file.toURI().toURL().toString()));
            } catch (Exception ex) {
                log.error("Невозможно переконвертировать в URL файл: {}", file.getAbsolutePath());
            }
        };

        if (mLabel != null) {
            textPriority.setText(String.valueOf(mLabel.getPriority()));
            nameLabelText.setText(mLabel.getName());
            infoLabelText.setText(mLabel.getInfo());
            pathAppText.setText(mLabel.getPathApplication());
            pathOpenAppWith.setText(mLabel.getPathOpenAppWith());
            autoRun.setSelected(mLabel.isAutoRun());
            if (mLabel.getPathImage() == null || mLabel.getPathImage().isEmpty()) {
                rbNoneIcon.setSelected(true);
                image.setImage(null);
                pathIconText.setText("");
            } else if (mLabel.getPathImage().contains("bundle")) {
                image.setImage(new Image(Resources.AER_ICON.toString()));
                pathIconText.setText(Resources.AER_ICON.toString());
                rbDefaultIcon.setSelected(true);
                rbNoneIcon.setSelected(false);
            } else {
                pathIconText.setText(mLabel.getPathImage());
                Optional.of(new File(mLabel.getPathImage()))
                        .filter(File::exists)
                        .ifPresent(updateIcon);
            }
        }

        dialog.setTitle(title);
        dialog.setResizable(true);
        ((Stage)dialog.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image(Resources.AER_ICON.toString()));

		buttonSetAppWith.setOnAction(e -> {
            Optional.ofNullable(initFileChooser(pathOpenAppWith.getText()).showOpenDialog(null))
                    .map(File::getAbsolutePath)
                    .ifPresent(pathOpenAppWith::setText);
		});
		
		buttonSetFile.setOnAction(e -> {
            Optional.ofNullable(initFileChooser(pathAppText.getText()).showOpenDialog(null))
                    .map(File::getAbsolutePath)
                    .ifPresent(pathAppText::setText);
		});

		pathIconText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.isEmpty()){
                rbNoneIcon.setSelected(true);
                rbNoneIcon.getOnAction().handle(null);
            } else {
                rbNoneIcon.setSelected(false);
                Optional.of(new File(pathIconText.getText()))
                        .filter(File::exists)
                        .ifPresent(updateIcon);
            }
        });

        buttonSetPathIcon.setOnAction(e -> {
            Optional.ofNullable(actionButtonSetPathIcon(pathIconText.getText()))
                    .filter(File::exists)
                    .ifPresent(updateIcon);
        });

        rbNoneIcon.setOnAction(e -> {
            image.setImage(null);
            pathIconText.setText("");
        });

        rbDefaultIcon.setOnAction(e -> {
            image.setImage(new Image(Resources.AER_ICON.toString() ));
            pathIconText.setText(Resources.AER_ICON.toString());
        });

        return dialog;
	}

    private static FileChooser initFileChooser(String path) {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Выбор файла");
        fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.*", "*.*"));

        String pathToApp = path == null || path.contains("bundle")
                ? System.getProperty("user.home")
                : path;

        File startDir = new File(pathToApp);
        String startPath = startDir.isFile()
                ? startDir.getAbsolutePath().substring(0, startDir.getAbsolutePath().length() - startDir.getName().length())
                : startDir.getAbsolutePath();

        fileC.setInitialDirectory(new File(startPath));

        return fileC;
    }

    private static File actionButtonSetPathIcon(String path) {
        FileChooser fileChooser = initFileChooser(path);
        fileChooser.setTitle("Выбор иконки ярлыка");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));

		return fileChooser.showOpenDialog(null);
	}

    public static Optional<MLabel> showAndWait(MLabel model) {
        return initialization(model).showAndWait();
    }

    public static Optional<MLabel> showAndWait() {
	    return showAndWait(null);
    }
}
