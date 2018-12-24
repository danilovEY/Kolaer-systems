package ru.kolaer.client.weather.service;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.other.WeatherKaesDataDto;
import ru.kolaer.common.dto.other.WeatherKaesDto;
import ru.kolaer.common.plugins.services.Service;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.system.network.KaesTable;
import ru.kolaer.common.system.ui.StaticView;
import ru.kolaer.common.tools.Tools;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by danilovey on 15.02.2018.
 */
@Slf4j
public class WeatherService implements Service, StaticView {
    private int lastDayForChart;
    private boolean run;
    private Node mainNode;
    private Label lastUpdateDate;
    private Label powerLabel;
    private Label radiationLabel;
    private Label temperatureLabel;
    private Label windLabel;
    private ImageView imageView;

    @Override
    public void initView(Consumer<StaticView> viewVisit) {
        BorderPane content = new BorderPane();
        content.setStyle("-fx-background-color: rgba(74,255,68,0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");

        Font labelFont = Font.font(null, FontWeight.BOLD, 15);

        lastUpdateDate = new Label();
        lastUpdateDate.setTextAlignment(TextAlignment.CENTER);
        lastUpdateDate.setAlignment(Pos.CENTER);
        lastUpdateDate.setWrapText(true);
        lastUpdateDate.setMinHeight(Region.USE_PREF_SIZE);
        lastUpdateDate.setFont(labelFont);

        powerLabel = new Label();
        powerLabel.setTextAlignment(TextAlignment.LEFT);
        powerLabel.setAlignment(Pos.CENTER);
        powerLabel.setWrapText(true);
        powerLabel.setMinHeight(Region.USE_PREF_SIZE);
        powerLabel.setFont(labelFont);

        radiationLabel = new Label();
        radiationLabel.setTextAlignment(TextAlignment.LEFT);
        radiationLabel.setAlignment(Pos.CENTER);
        radiationLabel.setWrapText(true);
        radiationLabel.setMinHeight(Region.USE_PREF_SIZE);
        radiationLabel.setFont(labelFont);

        temperatureLabel = new Label();
        temperatureLabel.setTextAlignment(TextAlignment.LEFT);
        temperatureLabel.setAlignment(Pos.CENTER);
        temperatureLabel.setWrapText(true);
        temperatureLabel.setMinHeight(Region.USE_PREF_SIZE);
        temperatureLabel.setFont(labelFont);

        windLabel = new Label();
        windLabel.setTextAlignment(TextAlignment.LEFT);
        windLabel.setAlignment(Pos.CENTER);
        windLabel.setWrapText(true);
        windLabel.setMinHeight(Region.USE_PREF_SIZE);
        windLabel.setFont(labelFont);

        imageView = new ImageView();
        imageView.setFitWidth(448);
        imageView.setFitHeight(229);

        VBox weatherData = new VBox();
        weatherData.setPadding(new Insets(10));
        weatherData.setAlignment(Pos.TOP_LEFT);
        weatherData.setSpacing(10);
        weatherData.getChildren().addAll(powerLabel,
//                new Separator(Orientation.VERTICAL),
                radiationLabel,
//                new Separator(Orientation.VERTICAL),
                temperatureLabel,
//                new Separator(Orientation.VERTICAL),
                windLabel);

        content.setLeft(weatherData);
        content.setCenter(imageView);
        content.setBottom(lastUpdateDate);

        BorderPane.setAlignment(lastUpdateDate, Pos.CENTER);

        setContent(content);

        viewVisit.accept(this);
    }

    private void updateData(WeatherKaesDto weatherKaesDto) {
        if(!isViewInit()) {
            return;
        }

        if(weatherKaesDto != null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(weatherKaesDto.getUpdateTime());
//            calendar.roll(Calendar.HOUR, -3);

            lastUpdateDate.setText("Время последнего обновления: " +
                    Tools.dateTimeToString(weatherKaesDto.getUpdateTime()));

            WeatherKaesDataDto data = weatherKaesDto.getData();
            if (data != null) {
                powerLabel.setText("Мощность: " + data.getPower());
                radiationLabel.setText("Излучение: " + data.getRadiation());
                temperatureLabel.setText("Температура: " + data.getTemperature());
                windLabel.setText("Ветер: " + data.getWind());
            }

            return;
        }

        log.warn("Не удалось обновить данные");

        powerLabel.setText("");
        lastUpdateDate.setText("");
        temperatureLabel.setText("");
        radiationLabel.setText("");
        windLabel.setText("");
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public Node getContent() {
        return mainNode;
    }

    @Override
    public void setContent(Node content) {
        mainNode = content;
    }

    @Override
    public boolean isRunning() {
        return run;
    }

    @Override
    public String getName() {
        return "Погода";
    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        run = true;

        if(!isViewInit()) {
            Tools.runOnWithOutThreadFX(() ->
                    initView(UniformSystemEditorKitSingleton
                            .getInstance()
                            .getUISystemUS()
                            .getStatic()::addStaticView));
        }

        while (run) {

            KaesTable kaesTable = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getOtherPublicAPI()
                    .getKaesTable();

            ServerResponse<WeatherKaesDto> weather = kaesTable.getWeather();
            if(weather.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(weather.getExceptionMessage());
            } else {
                Tools.runOnWithOutThreadFX(() -> {
                    updateData(weather.getResponse());

                    int dayOfYear = LocalDate.now().getDayOfYear();
                    if(lastDayForChart != dayOfYear) {
                        lastDayForChart = dayOfYear;

                        if(isViewInit()) {
                            imageView.setImage(new Image(kaesTable.getWeatherChartUrl(), true));
                        }
                    }
                });
            }

            try {
                TimeUnit.MINUTES.sleep(5);
            } catch (InterruptedException e) {
                run = false;
                return;
            }
        }
    }
}
