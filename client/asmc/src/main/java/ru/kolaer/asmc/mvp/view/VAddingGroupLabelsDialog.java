package ru.kolaer.asmc.mvp.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.tools.Resources;

import java.util.Optional;

/**
 * Окно для добавления группы.
 *
 * @author Danilov
 * @version 0.2
 */
public class VAddingGroupLabelsDialog {

	private static Dialog<MGroup> initialize(final MGroup groupModel) {
        BorderPane mainPane = new BorderPane();
        Dialog<MGroup> dialog = new Dialog<>();
        TextField groupNameText = new TextField();
        TextField textPriority = new TextField();

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setFillWidth(true);

        GridPane contentPane = new GridPane();
        contentPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        contentPane.setVgap(10); //vertical gap in pixels
        contentPane.setPadding(new Insets(10));
        contentPane.setAlignment(Pos.TOP_LEFT);

        contentPane.add(new Label("Имя группы:"), 0, 0);
        contentPane.add(groupNameText, 1, 0);

        contentPane.add(new Label("Приоритет:"), 0, 1);
        contentPane.add(textPriority, 1, 1);

        contentPane.getColumnConstraints().add(columnConstraints);

        mainPane.setCenter(contentPane);

        if(groupModel != null){
            groupNameText.setText(groupModel.getNameGroup());
            textPriority.setText(String.valueOf(groupModel.getPriority()));
        }

        String title = groupModel == null ? "Добавить группу" : "Редактировать группу";
        dialog.setTitle(title);
        dialog.setResizable(true);
        ((Stage)dialog.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image(Resources.AER_ICON.toString()));

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.setResultConverter(callBack -> {
            if(callBack == ok) {
                String priorityText = textPriority.getText();
                if(!priorityText.matches("[0-9]*")) {
                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getDialog()
                            .createWarningDialog("Внимание!", "Приоритет может быть только числом!")
                            .show();
                    return null;
                }

                if(groupModel == null) {
                    int priority = 0;

                    try {
                        priority = Integer.valueOf(priorityText);
                    } catch (NumberFormatException ignore) {}

                    return new MGroup(groupNameText.getText(), priority);
                } else {
                    groupModel.setNameGroup(groupNameText.getText());
                    groupModel.setPriority(Integer.valueOf(priorityText));
                }
                return groupModel;
            }

            return null;
        });

        dialog.getDialogPane().setContent(mainPane);

        return dialog;
	}

	public static Optional<MGroup> showAndWait(MGroup groupModel){
		return initialize(groupModel).showAndWait();
	}

    public static Optional<MGroup> showAndWait(){
        return showAndWait(null);
    }
}
