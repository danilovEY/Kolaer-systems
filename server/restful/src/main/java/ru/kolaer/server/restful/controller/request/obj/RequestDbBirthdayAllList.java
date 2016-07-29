package ru.kolaer.server.restful.controller.request.obj;

import ru.kolaer.server.dao.entities.DbBirthdayAll;

import java.util.Collections;
import java.util.List;

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
