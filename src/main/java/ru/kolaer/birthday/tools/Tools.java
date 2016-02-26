package ru.kolaer.birthday.tools;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Tools {
	
	public static Date convertToDate(final LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public static String getNameOrganization(final String org) {
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
