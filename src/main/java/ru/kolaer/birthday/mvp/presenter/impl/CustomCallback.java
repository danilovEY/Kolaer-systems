package ru.kolaer.birthday.mvp.presenter.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import ru.kolaer.client.javafx.system.OtherPublicAPI;
import ru.kolaer.client.javafx.system.UserBirthdayAllDataBase;
import ru.kolaer.client.javafx.system.UserDataBase;
import ru.kolaer.server.dao.entities.PublicHolidays;

/**
 * 
 * Реализация {@linkplain Callback} для получения от сервера сотрудников по определенной дате рождения.
 * @author danilovey
 * @version 0.1
 */
public class CustomCallback implements Callback<DatePicker, DateCell> {
	private final Logger LOG = LoggerFactory.getLogger(CustomCallback.class);
	/**Объект, который хранит начальную дату во view-календаря.*/
	private LocalDate startDate = LocalDate.now();
	/**Оттенки для каждой даты.*/
	private final String[] arrayColor = new String[42];
	/**Счеткик дат.*/
	private int index = 0;
	/**Индекс последней даты.*/
	private final int finish = 41;
	/**Флаг true, если нужно пересчитать пользователей и закрасить дату.*/
	private boolean update = true;
	/**БД с пользователями.*/
	private final UserDataBase<?> userDB;
	/**Имя организации.*/
	private final String organization;
	private final OtherPublicAPI otherPublicAPI;
	private final ExecutorService threads = Executors.newCachedThreadPool();
	
	public CustomCallback(final OtherPublicAPI otherPublicAPI, final UserDataBase<?> userDB) {
		this.userDB = userDB;
		this.organization = null;
		this.otherPublicAPI = otherPublicAPI;
	}
	
	public CustomCallback(final OtherPublicAPI otherPublicAPI, final UserBirthdayAllDataBase userDB, final String organization) {
		this.userDB = userDB;
		this.organization = organization;
		this.otherPublicAPI = otherPublicAPI;
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
							//В зависимости от кол-ва людей меняется интенсивность закрашивания даты.
						Platform.runLater(() -> {							
							String color = "-fx-background-color: #" + count + "" + count + "FF;";
							for(final PublicHolidays holiday : otherPublicAPI.getPublicHolidaysDateBase().getPublicHolidays(item.getMonthValue(), item.getYear())){
								if(holiday.getDate().getDay() == item.getDayOfMonth() ) {
									color += "-fx-text-fill: red;";
									break;
								}
							}
							arrayColor[ind] = color;
							node.setStyle(color);
						});
					} else {
						Platform.runLater(() -> {							
							node.setStyle("");
							arrayColor[ind] = "";
						});
					}
				}, threads).exceptionally(t -> {
					LOG.error("Ошибка!", t);
					return null;
				});
			}
		};
	}
}