package ru.kolaer.birthday.mvp.presenter.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import ru.kolaer.client.javafx.system.UserBirthdayAllDataBase;
import ru.kolaer.client.javafx.system.UserDataBase;

public class CustomCallback implements Callback<DatePicker, DateCell> {
	private final Logger LOG = LoggerFactory.getLogger(CustomCallback.class);
	
	private LocalDate startDate = LocalDate.now();
	private final String[] arrayColor = new String[42];
	private int index = 0;
	private final int finish = 41;
	private boolean update = true;
	private final UserDataBase<?> userDB;
	private final String organization;
	
	public CustomCallback(final UserDataBase<?> userDB) {
		this.userDB = userDB;
		this.organization = null;
	}
	
	public CustomCallback(final UserBirthdayAllDataBase userDB, final String organization) {
		this.userDB = userDB;
		this.organization = organization;
	}

	@Override
	public DateCell call(final DatePicker param) {
		return new DateCell() {
			@Override
			public void updateItem(final LocalDate item, final boolean empty) {
				if(index == finish) {
					index = 0;
				} else {
					if(index == 0) {
						if(item.equals(startDate)) {
							update = false;
						} else {
							update = true;
							startDate = item;
						}
					}		
					index++;
				}

				final int ind = index;
				final Node node = this;
				
				if(!update) {
					Platform.runLater(() -> {								
						node.setStyle(arrayColor[ind]);
					});
					return;
				}

				CompletableFuture.runAsync(() -> {
					int countUsersDataAll = 0;
					if(organization != null) {
						countUsersDataAll = ((UserBirthdayAllDataBase)userDB).getCountUsersBirthday(Date.from(item.atStartOfDay(ZoneId.systemDefault()).toInstant()), organization);
					} else {
						countUsersDataAll = userDB.getCountUsersBirthday(Date.from(item.atStartOfDay(ZoneId.systemDefault()).toInstant()));
					}
					
					if (countUsersDataAll != 0) {
						final int count = 99 - countUsersDataAll * 15;
						Platform.runLater(() -> {							
							final String color = "-fx-background-color: #" + count + "" + count + "FF;";					
							arrayColor[ind] = color;
							node.setStyle(color);
						});
					} else {
						Platform.runLater(() -> {							
							node.setStyle("");
							arrayColor[ind] = "";
						});
					}
				}).exceptionally(t -> {
					LOG.error("Ошибка!", t);
					return null;
				});
			}
		};
	}
}