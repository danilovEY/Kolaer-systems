package ru.kolaer.birthday.mvp.model;

import java.util.Date;

/**
 * Модель сотрудника для таблици.
 *
 * @author danilovey
 * @version 0.1
 */
public interface UserModel {
	/**Получить фамилию.*/
	public String getFirstName();
	/**Задать фамилию.*/
	public void setFirstName(String firstName);
	/**Получить имя.*/
	public String getSecondName();
	/**Задать имя.*/
	public void setSecondName(String secondName);
	/**Получить отчество.*/
	public String getThirdName();
	/**Задать отчество.*/
	public void setThirdName(String thirdName);
	/**Получить цех/отдел.*/
	public String getDepartament();
	/**Задать цех/отдел.*/
	public void setDepartament(String departament);
	/**Получить дату рождения.*/
	public Date getBirthday();
	/**Задать дату рождения.*/
	public void setBirthday(Date birthday);
	/**Получить путь к фотографии*/
	public String getIcon();
	/**Задать путь к фоторграфии.*/
	public void setIcon(String icon);
	/**Получить телефонный номер.*/
	public String getPhoneNumber();
	/**Задать телефонный номер.*/
	public void setPhoneNumber(String phoneNumber);
	/**Получить имя организации.*/
	public String getOrganization();
	/**Задать имя организации.*/
	public void setOrganization(String organization);
	/**Получить должность.*/
	public String getPost();
	/**Задать должность.*/
	public void setPost(String post);
	/**Получить инициалы.*/
	public String getInitials();
	/**Задать инициалы*/
	public void setInitials(String initials);
}
