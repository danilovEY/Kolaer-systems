package ru.kolaer.server.employee.model.request;

import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;

import java.util.Collections;
import java.util.List;

public class RequestDbBirthdayAllList {
	private List<EmployeeOtherOrganizationDto> birthdayList;
	
	public RequestDbBirthdayAllList(final List<EmployeeOtherOrganizationDto> list) {
		this.setBirthdayList(list);
	}

	public RequestDbBirthdayAllList() {
		this.setBirthdayList(null);
	}
	
	public List<EmployeeOtherOrganizationDto> getBirthdayList() {
		return birthdayList;
	}

	public void setBirthdayList(final List<EmployeeOtherOrganizationDto> list) {
		if(list != null) {
			this.birthdayList = list;
		} else {
			this.birthdayList = Collections.emptyList();
		}
	}
}
