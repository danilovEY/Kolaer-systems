package ru.kolaer.api.mvp.model.restful;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PublicHolidays {
	private JSONDate date;
	private String note;
	private String localName;
	private String englishName;
	
	public PublicHolidays(final JSONDate date, final String localName, final String englishName, final String note) {
		this.date = date;
		this.localName = localName;
		this.englishName = englishName;
		this.note = note;
	}
	
	public PublicHolidays() {
	}
	
	public JSONDate getDate() {
		return date;
	}
	public void setDate(JSONDate date) {
		this.date = date;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
