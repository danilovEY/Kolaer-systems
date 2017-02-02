package ru.kolaer.server.webportal.mvc.model.dto;


import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;

import java.util.Collections;
import java.util.List;

public class RequestDbBirthdayAllList {
	private List<EmployeeOtherOrganization> birthdayList;
	
	public RequestDbBirthdayAllList(final List<EmployeeOtherOrganization> list) {
		this.setBirthdayList(list);
	}

	public RequestDbBirthdayAllList() {
		this.setBirthdayList(null);
	}
	
	public List<EmployeeOtherOrganization> getBirthdayList() {
		return birthdayList;
	}

	public void setBirthdayList(final List<EmployeeOtherOrganization> list) {
		if(list != null) {
			this.birthdayList = list;
		} else {
			this.birthdayList = Collections.emptyList();
		}
	}
}
