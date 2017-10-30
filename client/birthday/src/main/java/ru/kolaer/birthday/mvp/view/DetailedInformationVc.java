package ru.kolaer.birthday.mvp.view;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.tools.BirthdayTools;

import java.net.URL;

public class DetailedInformationVc {

	public static void show(UserModel user) {
		Dialog dialog = new Dialog();

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().add(ButtonType.CLOSE);


		ImageView icon = new ImageView();
		icon.setPreserveRatio(true);
		icon.setFitHeight(400);
		icon.setFitWidth(300);

		String birthdayDate;

		if(user.getGender() == null || user.getGender() == EnumGender.MALE) {
			birthdayDate = BirthdayTools.dateToString(user.getBirthday());
		} else {
			birthdayDate = BirthdayTools.dateToStringWithOutYear(user.getBirthday());
		}

		ListView<String> titleListView = new ListView<>();
		titleListView.getItems().addAll("Ф.И.О.",
				"Организация",
				"Подразделение",
				"Должность",
				"E-Mail",
				"Телефон",
				"Дата рождения");

		ListView<String> valueListView = new ListView<>();
		valueListView.getItems().addAll(user.getInitials(),
				user.getOrganization(),
				user.getDepartment(),
				user.getPost(),
				user.getEmail(),
				user.getPhoneNumber(),
				birthdayDate
				);

		BorderPane mainPane = new BorderPane(new SplitPane(titleListView, valueListView));
		mainPane.setTop(icon);

		dialog.setGraphic(mainPane);

		if(user.getIcon() == null){
			URL url = DetailedInformationVc.class.getResource("/nonePicture.jpg");
			icon.setImage(new Image(url.toString(), false));
		} else {
			icon.setImage(new Image(user.getIcon(), false));
		}

		dialog.show();
	}

}