package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.presenter.CAddingLabelDialog;
import ru.kolaer.asmc.mvp.view.VLabel;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.mvp.model.MLabel;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

/**
 * Контроллер ярлыка.
 * 
 * @author Danilov
 * @version 0.1
 */
public class VLabelCss implements VLabel {
    private final static Logger log = LoggerFactory.getLogger(VLabelCss.class);
    private final MenuItem editLabel;
    private final MenuItem deleteLabel;
    private final ContextMenu labelContextMenu;

    /** Список слушателей. */
	private final BorderPane mainPane;
	/** Кнопка запускающие приложение. */
	private final Button button;
	/** Иконка. */
	private final ImageView image;
	/** Имя ярлыка. */
	private final Label nameLabel;
	private final Tooltip toolTip;
    private MLabel data;


	public VLabelCss() {
        this.mainPane = new BorderPane();
        this.button = new Button();
        this.nameLabel = new Label();
        this.image = new ImageView();
        this.toolTip = new Tooltip();
        this.editLabel = new MenuItem(Resources.MENU_ITEM_EDIT_LABEL);
        this.deleteLabel = new MenuItem(Resources.MENU_ITEM_DELETE_LABEL);
        this.labelContextMenu = new ContextMenu();

		this.initialize();
	}

	private void initialize() {
        final BorderPane borderPane = new BorderPane(this.button);
        borderPane.setTop(this.nameLabel);
        borderPane.getStyleClass().add("lableSecontPanel");

        this.button.setMaxWidth(Double.MAX_VALUE);
        this.button.setPrefHeight(60);
        this.button.getStyleClass().add("record-sales-spring");
        this.button.setWrapText(true);

        this.nameLabel.setTextAlignment(TextAlignment.CENTER);
        this.nameLabel.setAlignment(Pos.CENTER);
        this.nameLabel.setMaxWidth(Double.MAX_VALUE);
        this.nameLabel.setWrapText(true);

        this.image.setSmooth(true);
        this.image.setFitHeight(100);
        this.image.setFitWidth(100);

        final ScrollPane scrollBackPanel = new ScrollPane();
        scrollBackPanel.setFitToWidth(true);
        scrollBackPanel.setFitToHeight(true);
        scrollBackPanel.setContextMenu(labelContextMenu);
        scrollBackPanel.setContent(borderPane);
        scrollBackPanel.getStyleClass().add("lableTherdPanel");
        scrollBackPanel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        final BorderPane icon  = new BorderPane(this.image);

        this.mainPane.setPrefWidth(450);
        this.mainPane.getStyleClass().add("lableMainPane");
        this.mainPane.setCenter(scrollBackPanel);
        this.mainPane.setLeft(icon);
        BorderPane.setMargin(this.image, new Insets(5,5,5,5));
	}

	public void updateView(final MLabel label) {
        this.data = label;

        this.nameLabel.setFont(Font.font(18 - label.getName().length() * 0.15));
        this.nameLabel.setText(label.getName());

        this.button.setFont(Font.font(18 - label.getName().length() * 0.15));
        this.button.setText(label.getInfo());

        ToolTipManager.sharedInstance().setDismissDelay(0);
        this.toolTip.setText(label.getName());
        this.button.setTooltip(this.toolTip);

        this.image.setImage(null);

        Optional.ofNullable(label.getPathImage()).ifPresent(image -> {
            if(image.equals(Resources.AER_ICON.toString())) {
                this.image.setImage(new Image(Resources.AER_ICON.toString()));
            } else {
                final File imageFile = new File(image);

                if(imageFile.exists())
                    try {
                        this.image.setImage(new Image(imageFile.toURI().toURL().toString()));
                    } catch (MalformedURLException e) {
                        log.error("Невозможно преобразовать \"{}\" в URL!", image);
                    }
            }
        });
	}

    @Override
    public void setOnEdit(Function<MLabel, Void> function) {
        this.editLabel.setOnAction(e -> new CAddingLabelDialog(this.data)
                .showAndWait().ifPresent(function::apply)
        );
    }

    @Override
    public void setOnDelete(Function<MLabel, Void> function) {
        this.deleteLabel.setOnAction(e ->
            function.apply(this.data)
        );
    }

    @Override
    public void setOnAction(Function<MLabel, Void> function) {
        this.button.setOnAction(e ->
            function.apply(this.data)
        );
    }

    @Override
    public void setAccess(boolean access) {
        if(access)
            this.labelContextMenu.getItems().addAll(this.editLabel, this.deleteLabel);
        else
            this.labelContextMenu.getItems().clear();
    }

    @Override
	public void setContent(BorderPane content) {
		this.mainPane.setCenter(content);
	}

	@Override
	public BorderPane getContent() {
		return this.mainPane;
	}
}
