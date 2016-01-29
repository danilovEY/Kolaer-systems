package ru.kolaer.birthday.mvp.model.impl;

import java.util.Date;

import ru.kolaer.birthday.mvp.model.UserModel;

public class UserModelImpl implements UserModel {
	private String firstName;
	private String secondName;
	private String thirdName;
	private String departament;
	private String organization;
	private Date birthday;
	private String icon;
	private String phoneNumber;
	
	public UserModelImpl(String organization, String firstName, String secondName, String thirdName, String departament, Date birthday, String icon, String phoneNumber){
		this.organization = organization;
		this.firstName = firstName;
		this.secondName = secondName;
		this.thirdName = thirdName;
		this.departament = departament;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.icon = icon;
	}
	
	public UserModelImpl() {
		
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(final String secondName) {
		this.secondName = secondName;
	}
	public String getThirdName() {
		return thirdName;
	}
	public void setThirdName(final String thirdName) {
		this.thirdName = thirdName;
	}
	public String getDepartament() {
		return departament;
	}
	public void setDepartament(final String departament) {
		this.departament = departament;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(final Date bithday) {
		this.birthday = bithday;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String getOrganization() {
		return this.organization;
	}

	@Override
	public void setOrganization(final String organization) {
		this.organization = organization;
	}
}
