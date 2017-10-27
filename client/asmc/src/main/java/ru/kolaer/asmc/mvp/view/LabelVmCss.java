package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.tools.Application;
import ru.kolaer.asmc.tools.Resources;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Контроллер ярлыка.
 * 
 * @author Danilov
 * @version 0.1
 */
@Slf4j
public class LabelVmCss implements LabelVm {

    private MenuItem editLabel;
    private MenuItem deleteLabel;
    private MenuItem copyLabel;

    private ContextMenu labelContextMenu;

    /** Список слушателей. */
	private BorderPane mainPane;
	/** Кнопка запускающие приложение. */
	private Button button;
	/** Иконка. */
	private ImageView image;
	/** Имя ярлыка. */
	private Label nameLabel;
	private Tooltip toolTip;
    private ScrollPane scrollBackPanel;

    private final MLabel data;

	public LabelVmCss(@NonNull MLabel label) {
	    this.data = label;
	}

    @Override
    public void setAccess(boolean access) {
        this.scrollBackPanel.setContextMenu(access ? this.labelContextMenu : null);
    }

    @Override
    public boolean isAccess() {
        return this.scrollBackPanel.getContextMenu() != null;
    }

	@Override
	public BorderPane getContent() {
		return this.mainPane;
	}

    private void initialize() {
        mainPane = new BorderPane();
        scrollBackPanel = new ScrollPane();
        button = new Button();
        nameLabel = new Label();
        image = new ImageView();
        toolTip = new Tooltip();
        editLabel = new MenuItem("Редактировать");
        deleteLabel = new MenuItem("Удалить");
        copyLabel = new MenuItem("Копировать");
        labelContextMenu = new ContextMenu(this.editLabel, this.deleteLabel,
                new SeparatorMenuItem(), this.copyLabel);
    }

    @Override
    public void initView(Consumer<LabelVm> viewVisit) {
        initialize();

        BorderPane borderPane = new BorderPane(this.button);
        borderPane.setTop(this.nameLabel);
        borderPane.getStyleClass().add("lableSecontPanel");

        this.button.setMaxWidth(Double.MAX_VALUE);
        this.button.setPrefHeight(60);
        this.button.getStyleClass().add("record-sales");
        this.button.setWrapText(true);

        this.nameLabel.setTextAlignment(TextAlignment.CENTER);
        this.nameLabel.setAlignment(Pos.CENTER);
        this.nameLabel.setMaxWidth(Double.MAX_VALUE);
        this.nameLabel.setWrapText(true);

        this.image.setSmooth(true);
        this.image.setFitHeight(100);
        this.image.setFitWidth(100);

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

        ToolTipManager.sharedInstance().setDismissDelay(0);

        this.button.setTooltip(this.toolTip);

        this.image.setImage(null);

        this.button.setOnAction(e ->
                new Application(data.getPathApplication(), data.getPathOpenAppWith()).start()
        );

        updateView(data);

        viewVisit.accept(this);

    }

    private void updateView(MLabel mLabel) {
        this.nameLabel.setFont(Font.font(18 - mLabel.getName().length() * 0.15));
        this.button.setFont(Font.font(18 - mLabel.getName().length() * 0.15));
        this.nameLabel.setText(mLabel.getName());
        this.button.setText(mLabel.getInfo());
        this.toolTip.setText(mLabel.getName());

        Optional.ofNullable(mLabel.getPathImage()).ifPresent(image -> {
            if(image.equals(Resources.AER_ICON.toString())) {
                this.image.setImage(new Image(Resources.AER_ICON.toString()));
            } else {
                File imageFile = new File(image);

                if(imageFile.exists()) {
                    try {
                        this.image.setImage(new Image(imageFile.toURI().toURL().toString()));
                    } catch (MalformedURLException e) {
                        log.error("Невозможно преобразовать \"{}\" в URL!", image);
                    }
                }
            }
        });
    }

    @Override
    public void setOnCopy(Consumer<LabelVm> consumer) {
        copyLabel.setOnAction(e ->
                consumer.accept(this)
        );
    }

    @Override
    public void setOnDelete(Consumer<LabelVm> consumer) {
        deleteLabel.setOnAction(e ->
                consumer.accept(this)
        );
    }

    @Override
    public void setOnEdit(Consumer<LabelVm> consumer) {
        editLabel.setOnAction(e -> VAddingLabelDialog
                .showAndWait(data).ifPresent(label -> {
                    consumer.accept(this);
                    this.updateView(label);
                })
        );
    }

    @Override
    public MLabel getMode() {
	    return this.data;
    }
}
