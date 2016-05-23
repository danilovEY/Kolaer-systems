package ru.kolaer.api.mvp.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestDbBirthdayAllList {
	private List<DbBirthdayAll> birthdayList;
	
	public RequestDbBirthdayAllList(final List<DbBirthdayAll> list) {
		this.setBirthdayList(list);
	}

	public RequestDbBirthdayAllList() {
		this.setBirthdayList(null);
	}
	
	public List<DbBirthdayAll> getBirthdayList() {
		return birthdayList;
	}

	public void setBirthdayList(final List<DbBirthdayAll> list) {
		if(list != null) {
			this.birthdayList = list;
		} else {
			this.birthdayList = Collections.emptyList();
		}
	}
}
