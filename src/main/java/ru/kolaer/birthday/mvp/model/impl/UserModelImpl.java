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
	private String post;
	private String initials;
	
	public UserModelImpl(final String organization, 
			final String firstName, 
			final String secondName, 
			final String thirdName, 
			final String departament, 
			final Date birthday, 
			final String icon, 
			final String phoneNumber, 
			final String post,
			final String initials){
		this.organization = organization;
		this.firstName = firstName;
		this.secondName = secondName;
		this.thirdName = thirdName;
		this.departament = departament;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.icon = icon;
		this.post = post;
		this.initials = initials;
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

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
}
