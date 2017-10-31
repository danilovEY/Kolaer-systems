package ru.kolaer.birthday.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.birthday.mvp.model.UserModel;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 31.10.2017.
 */
public class BirthdayInfoPane implements StaticView {
    private final String title;
    private final List<UserModel> userModelList;
    private VBox mainPane;

    public BirthdayInfoPane(String title, List<UserModel> userModelList) {
        this.title = title;
        this.userModelList = userModelList;
    }

    @Override
    public void initView(Consumer<StaticView> viewVisit) {
        mainPane = new VBox();
        mainPane.setFillWidth(true);
        mainPane.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));
        titleLabel.setPadding(new Insets(10,0,10,0));

        mainPane.getChildren().add(titleLabel);

        for (UserModel userModel : userModelList) {
            ButtonBase button = new Button(userModel.getInitials());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(e -> DetailedInformationVc.show(userModel));
            mainPane.getChildren().add(button);
        }

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
