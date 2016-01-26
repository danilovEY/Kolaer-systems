package ru.kolaer.birthday.mvp.model;

import java.net.URL;
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
	public Date getBithday();
	public void setBithday(Date bithday);
	public URL getIcon();
	public void setIcon(URL icon);
	public Integer getPersonNumber();
	public void setPersonNumber(Integer personNumber);
}
