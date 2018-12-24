package ru.kolaer.birthday.mvp.view;

import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.birthday.tools.BirthdayTools;
import ru.kolaer.common.tools.Tools;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * 
 * Реализация {@linkplain Callback} для получения от сервера сотрудников по определенной дате рождения.
 * @author danilovey
 * @version 0.1
 */
public class CustomCallback implements Callback<DatePicker, DateCell> {
	private Logger LOG = LoggerFactory.getLogger(CustomCallback.class);
	/**Объект, который хранит начальную дату во view-календаря.*/
	private LocalDate startDate = LocalDate.now();
	/**Оттенки для каждой даты.*/
	private String[] arrayColor = new String[42];
	/**Счеткик дат.*/
	private int index = 0;
	/**Индекс последней даты.*/
	private int finish = 41;
	/**Флаг true, если нужно пересчитать пользователей и закрасить дату.*/
	private boolean update = true;

	private final Function<Date, Integer> gettingCount;

	public CustomCallback(Function<Date, Integer> function) {
		gettingCount = function;
	}

	@Override
	public DateCell call(DatePicker param) {
		return new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
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

				int ind = index;
				Node node = this;
				
				if(!update) {
					Tools.runOnWithOutThreadFX(() -> node.setStyle(arrayColor[ind]));
					return;
				}
				
				CompletableFuture.runAsync(() -> {
					int countUsersDataAll = gettingCount.apply(BirthdayTools.convertToDate(item));
					
					if (countUsersDataAll != 0) {
						int count = 255 - countUsersDataAll * 15;
							//В зависимости от кол-ва людей меняется интенсивность закрашивания даты.
						Tools.runOnWithOutThreadFX(() -> {
							String color = "-fx-background-color: rgb(" + count + ", " + count + ", 255);";

							arrayColor[ind] = color;
							node.setStyle(color);
						});
					} else {
						Tools.runOnWithOutThreadFX(() -> {
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