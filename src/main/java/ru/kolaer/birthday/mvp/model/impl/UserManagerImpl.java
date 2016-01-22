package ru.kolaer.birthday.mvp.model.impl;

import java.net.URL;
import java.util.Date;

import ru.kolaer.birthday.mvp.model.UserManager;

public class UserManagerImpl implements UserManager {
	private String firstName;
	private String secondName;
	private String thirdName;
	private String departament;
	private Date bithday;
	private URL icon;
	
	public UserManagerImpl(String firstName, String secondName, String thirdName, String departament, Date bithday, URL icon){
		this.firstName = firstName;
		this.secondName = secondName;
		this.thirdName = thirdName;
		this.departament = departament;
		this.bithday = bithday;
		this.icon = icon;
	}
	
	public UserManagerImpl() {
		
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
	public Date getBithday() {
		return bithday;
	}
	public void setBithday(Date bithday) {
		this.bithday = bithday;
	}
	public URL getIcon() {
		return icon;
	}
	public void setIcon(URL icon) {
		this.icon = icon;
	}
	
	

}
