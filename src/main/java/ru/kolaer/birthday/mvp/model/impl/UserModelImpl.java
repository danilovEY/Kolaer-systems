package ru.kolaer.birthday.mvp.model.impl;

import java.util.Date;

import ru.kolaer.birthday.mvp.model.UserModel;

public class UserModelImpl implements UserModel {
	private String firstName;
	private String secondName;
	private String thirdName;
	private String departament;
	private Integer personNumber;
	private Date birthday;
	private String icon;
	private String phoneNumber;
	
	public UserModelImpl(Integer personNumber, String firstName, String secondName, String thirdName, String departament, Date birthday, String icon, String phoneNumber){
		this.personNumber = personNumber;
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
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getThirdName() {
		return thirdName;
	}
	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}
	public String getDepartament() {
		return departament;
	}
	public void setDepartament(String departament) {
		this.departament = departament;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date bithday) {
		this.birthday = bithday;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(Integer personNumber) {
		this.personNumber = personNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
