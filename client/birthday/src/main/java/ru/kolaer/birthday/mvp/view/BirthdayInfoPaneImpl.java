package ru.kolaer.birthday.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.tools.BirthdayTools;
import ru.kolaer.client.core.system.ui.StaticView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by danilovey on 31.10.2017.
 */
public class BirthdayInfoPaneImpl implements BirthdayInfoPane {
    private final Map<String, VBox> labelsMap = new HashMap<>();
    private Node mainPane;
    private VBox content;

    @Override
    public String getTitle() {
        return "Дни рождения";
    }

    @Override
    public void initView(Consumer<StaticView> viewVisit) {
        content = new VBox();
        content.setMaxHeight(Region.USE_PREF_SIZE);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10));

        BackgroundImage notifyBackground= new BackgroundImage(new Image(getClass().getResource("/static-background.png").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        content.setBackground(new Background(notifyBackground));

        setContent(content);

        viewVisit.accept(this);
    }

    @Override
    public void setContent(Node content) {
        mainPane = content;
    }

    @Override
    public Node getContent() {
        return mainPane;
    }

    @Override
    public void put(String title, List<UserModel> users) {
        if(labelsMap.containsKey(title)) {
            content.getChildren().remove(labelsMap.get(title));
        }

        VBox content = new VBox();
        content.setBackground(Background.EMPTY);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));
        titleLabel.setPadding(new Insets(10,0,10,0));

        content.getChildren().add(titleLabel);

        List<Button> buttons = new ArrayList<>(users.size());

        for (UserModel userModel : users) {
            Button button = new Button("(" + BirthdayTools.getNameOrganization(userModel.getOrganization()) + ") " + userModel.getInitials());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(e -> DetailedInformationVc.show(userModel));
            buttons.add(button);
        }

        content.getChildren().addAll(buttons);

        this.content.getChildren().add(content);

        labelsMap.put(title, content);
    }
}
