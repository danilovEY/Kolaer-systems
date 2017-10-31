package ru.kolaer.birthday.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
		dialog.setTitle(user.getInitials());

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

		final int ROW_HEIGHT = 24;
		titleListView.setPrefHeight(titleListView.getItems().size() * ROW_HEIGHT + 2);
		valueListView.setPrefHeight(valueListView.getItems().size() * ROW_HEIGHT + 2);

		BorderPane mainPane = new BorderPane(new SplitPane(titleListView, valueListView));
		mainPane.setTop(icon);

		BorderPane.setAlignment(icon, Pos.CENTER);
		BorderPane.setMargin(icon, new Insets(10,10,10,10));

		dialogPane.setContent(mainPane);

		if(user.getIcon() == null){
			URL url = DetailedInformationVc.class.getResource("/nonePicture.jpg");
			icon.setImage(new Image(url.toString(), false));
		} else {
			icon.setImage(new Image(user.getIcon(), false));
		}

		dialog.show();
	}

}