package ru.kolaer.birthday.tools;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BirthdayTools {
	/**Формат месяцев.*/
	private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){
		@Override
		public String[] getMonths() {
			return new String[]{"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
					"Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
		}
	};

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateFormat dateFormatWithOutYear = new SimpleDateFormat("dd MMMMM", myDateFormatSymbols);
	private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", myDateFormatSymbols);

	public static String dateToString(Date date) {
		return dateFormat.format(date);
	}

	public static String dateToStringWithOutYear(Date date) {
		return dateFormatWithOutYear.format(date);
	}

	public static Date convertToDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public static String getNameOrganization(String org) {
		switch(org) {		
			case "БалаковоАтомэнергоремонт": return "БалАЭР";
			case "ВолгодонскАтомэнергоремонт": return "ВДАЭР";
			case "КалининАтомэнергоремонт": return "КАЭР";
			case "КурскАтомэнергоремонт": return "КурскАЭР";
			case "ЛенАтомэнергоремонт": return "ЛенАЭР";
			case "НововоронежАтомэнергоремонт": return "НВАЭР";
			case "СмоленскАтомэнергоремонт": return "САЭР";
			case "УралАтомэнергоремонт": return "УралАЭР";
			case "Центральный аппарат": return "ЦА";
			case "КолАтомэнергоремонт": return "КолАЭР";
			default: return org;
		}
	}
}
