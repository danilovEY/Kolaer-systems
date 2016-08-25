package ru.kolaer.admin.service;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.system.ui.StaticView;

/**
 * Created by danilovey on 25.08.2016.
 */
public class StaticViewPPR implements StaticView, PPR {
    private final Counter counter;
    private BorderPane mainPane;
    private Label title;
    private Label month;
    private Label days;
    private Label hours;
    private Label minute;
    private Label secund;
    private Label foot;
    private Label description;


    public StaticViewPPR(final Counter counter) {
        this.counter = counter;
        this.mainPane = new BorderPane();

        final String style = "    -fx-background-color:\n" +
                "                        #0f0d0f, #579bd6,\n" +
                "            linear-gradient(#5299eb, #1e4dc5);\n" +
                "    -fx-background-radius: 5,4,3,5;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Arial\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;\n" +
                "    -fx-padding: 5 10 5 10;";


        this.title = new Label();
        this.title.setFont(Font.font(null, FontWeight.BOLD, 15));

        this.description = new Label();
        this.description.setFont(Font.font(null, FontWeight.BOLD, 13));

        this.month = new Label();
        this.month.setStyle(style);

        this.days = new Label();
        this.days.setStyle(style);

        this.hours = new Label();
        this.hours.setStyle(style);

        this.minute = new Label();
        this.minute.setStyle(style);

        this.secund = new Label();
        this.secund.setStyle(style);

        final Font labelFont = Font.font(null, FontWeight.BOLD, 10);

        final Label labelMonth = new Label("Месяцы");
        labelMonth.setFont(labelFont);
        final VBox monthPane = new VBox(this.month, labelMonth);
        monthPane.setAlignment(Pos.CENTER);

        final Label labelDay = new Label("Дни");
        labelDay.setFont(labelFont);
        final VBox dayPane = new VBox(this.days, labelDay);
        dayPane.setAlignment(Pos.CENTER);

        final Label labelHour = new Label("Часы");
        labelHour.setFont(labelFont);
        final VBox hourPane = new VBox(this.hours, labelHour);
        hourPane.setAlignment(Pos.CENTER);

        final Label labelMin = new Label("Минуты");
        labelMin.setFont(labelFont);
        final VBox minutePane = new VBox(this.minute, labelMin);
        minutePane.setAlignment(Pos.CENTER);

        final Label labelSec = new Label("Секунды");
        labelSec.setFont(labelFont);
        final VBox secundPane = new VBox(this.secund, labelSec);
        secundPane.setAlignment(Pos.CENTER);


        final HBox flowPane = new HBox();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setSpacing(10);
        flowPane.getChildren().addAll(monthPane, dayPane, hourPane, minutePane, secundPane);

        final VBox titleDisPane = new VBox(this.title, this.description);
        titleDisPane.setAlignment(Pos.CENTER);

        this.foot = new Label();
        foot.setFont(labelFont);

        this.mainPane.setTop(titleDisPane);
        this.mainPane.setCenter(flowPane);
        this.mainPane.setBottom(this.foot);
        BorderPane.setAlignment(this.title, Pos.CENTER);
        BorderPane.setAlignment(this.foot, Pos.CENTER);
        BorderPane.setAlignment(flowPane, Pos.CENTER);


    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setDescription(String des) {
        this.description.setText(des);
    }

    @Override
    public void setFoot(String foot) {
        this.foot.setText(foot);
    }

    @Override
    public void setTime(int month, int day, int hours, int min, int sec) {
        this.month.setText(String.format("%02d", month));
        this.days.setText(String.format("%02d",day));
        this.hours.setText(String.format("%02d",hours));
        this.minute.setText(String.format("%02d",min));
        this.secund.setText(String.format("%02d",sec));
    }

    @Override
    public Counter getCounter() {
        return this.counter;
    }
}
