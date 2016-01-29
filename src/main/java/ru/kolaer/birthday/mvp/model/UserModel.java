package ru.kolaer.birthday.mvp.model;

import java.util.Date;

public interface UserModel {
	public String getFirstName();
	public void setFirstName(String firstName);
	public String getSecondName();
	public void setSecondName(String secondName);
	public String getThirdName();
	public void setThirdName(String thirdName);
	public String getDepartament();
	public void setDepartament(String departament);
	public Date getBirthday();
	public void setBirthday(Date birthday);
	public String getIcon();
	public void setIcon(String icon);
	public String getPhoneNumber();
	public void setPhoneNumber(String phoneNumber);
}
