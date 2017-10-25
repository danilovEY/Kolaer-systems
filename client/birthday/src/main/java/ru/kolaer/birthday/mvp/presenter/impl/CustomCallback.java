package ru.kolaer.birthday.mvp.presenter.impl;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.network.UserDataBase;
import ru.kolaer.api.system.network.kolaerweb.EmployeeOtherOrganizationTable;
import ru.kolaer.birthday.tools.Tools;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private final UserDataBase<?>[] usersDB;
	/**Имя организации.*/
	private final String organization;
	private final ExecutorService threads = Executors.newCachedThreadPool();
	
	public CustomCallback(final UserDataBase<?> userDB) {
		this.usersDB = new UserDataBase<?>[]{userDB};
		this.organization = null;
	}
	
	public CustomCallback(final EmployeeOtherOrganizationTable userDB, final String organization) {
		this.usersDB = new UserDataBase<?>[]{userDB};
		this.organization = organization;
	}
	
	public CustomCallback(final UserDataBase<?>... userDB) {
		this.usersDB = userDB;
		this.organization = null;
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
						countUsersDataAll = ((EmployeeOtherOrganizationTable)usersDB[0]).getCountUsersBirthday(Tools.convertToDate(item), organization).getResponse();
					} else {
						for(UserDataBase<?> base : usersDB) {
							countUsersDataAll += base.getCountUsersBirthday(Tools.convertToDate(item)).getResponse();
						}
					}
					
					if (countUsersDataAll != 0) {
						final int count = 255 - countUsersDataAll * 15;
							//В зависимости от кол-ва людей меняется интенсивность закрашивания даты.
						Platform.runLater(() -> {							
							String color = "-fx-background-color: rgb(" + count + ", " + count + ", 255);";

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