package ru.kolaer.asmc.mvp.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.Optional;

/**
 * Окно для добавления группы.
 *
 * @author Danilov
 * @version 0.2
 */
public class VAddingGroupLabelsDialog {
	private final Logger LOG = LoggerFactory.getLogger(VAddingGroupLabelsDialog.class);

    private final Dialog<MGroup> dialog;
    private final TextField groupNameText;
    private final TextField textPriority;
    private final BorderPane mainPane;

    private MGroup result;
	
	public VAddingGroupLabelsDialog() {
		this(null);
	}

	/**
	 * {@linkplain VAddingGroupLabelsDialog}
	 * @param groupModel - Редактировать группу.
	 */
	public VAddingGroupLabelsDialog(final MGroup groupModel) {
		this.mainPane = new BorderPane();
        this.dialog = new Dialog<>();
		this.groupNameText = new TextField();
		this.textPriority = new TextField();

		this.result = groupModel;

        this.initialize();
	}

	public void initialize() {
        final GridPane contentPane = new GridPane();
        contentPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        contentPane.setVgap(10); //vertical gap in pixels
        contentPane.setPadding(new Insets(10));
        contentPane.setAlignment(Pos.TOP_LEFT);

        contentPane.add(new Label("Имя группы:"), 0, 0);
        contentPane.add(this.groupNameText, 1, 0);

        contentPane.add(new Label("Приоритет:"), 0, 1);
        contentPane.add(this.textPriority, 1, 1);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setFillWidth(true);

        contentPane.getColumnConstraints().add(columnConstraints);

        columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);

        contentPane.getColumnConstraints().add(columnConstraints);

        this.mainPane.setCenter(contentPane);

        this.textPriority.setText("0");

        if(this.result != null){
            this.groupNameText.setText(this.result.getNameGroup());
            this.textPriority.setText(String.valueOf(this.result.getPriority()));
        }

        final String title = this.result == null ? "Добавить группу" : "Редактировать группу";
        this.dialog.setTitle(title);
        this.dialog.setResizable(true);
        ((Stage)this.dialog.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image(Resources.AER_ICON.toString()));

        final ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        this.dialog.getDialogPane().getButtonTypes().add(ok);
        this.dialog.getDialogPane().getButtonTypes().add(new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE));
        this.dialog.setResultConverter(callBack -> {
            if(callBack == ok) {
                if(!this.textPriority.getText().matches("[0-9]*")) {
                    final Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Внимание!");
                    alert.setHeaderText("Приоритет может быть только числом!");
                    alert.show();
                    return null;
                }
                if(this.result == null) {
                    this.result = new MGroup(this.groupNameText.getText(), Integer.valueOf(this.textPriority.getText()));
                }
                else {
                    this.result.setNameGroup(this.groupNameText.getText());
                    this.result.setPriority(Integer.valueOf(this.textPriority.getText()));
                }
                return this.result;
            }

            return null;
        });

        this.dialog.getDialogPane().setContent(this.mainPane);
	}

	public Optional<MGroup> showAndWait(){
		return this.dialog.showAndWait();
	}
}
