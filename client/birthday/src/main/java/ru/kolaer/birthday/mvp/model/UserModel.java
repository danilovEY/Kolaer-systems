package ru.kolaer.birthday.mvp.model;

import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;

import java.util.Date;

/**
 * Модель сотрудника для таблици.
 *
 * @author danilovey
 * @version 0.1
 */
 public interface UserModel {
	/**Получить фамилию.*/
	 String getFirstName();
	/**Задать фамилию.*/
	 void setFirstName(String firstName);
	/**Получить имя.*/
	 String getSecondName();
	/**Задать имя.*/
	 void setSecondName(String secondName);
	/**Получить отчество.*/
	 String getThirdName();
	/**Задать отчество.*/
	 void setThirdName(String thirdName);
	/**Получить цех/отдел.*/
	 String getDepartment();
	/**Задать цех/отдел.*/
	 void setDepartment(String departament);
	/**Получить дату рождения.*/
	 Date getBirthday();
	/**Задать дату рождения.*/
	 void setBirthday(Date birthday);
	/**Получить путь к фотографии*/
	 String getIcon();
	/**Задать путь к фоторграфии.*/
	 void setIcon(String icon);
	/**Получить телефонный номер.*/
	 String getPhoneNumber();
	/**Задать телефонный номер.*/
	 void setPhoneNumber(String phoneNumber);
	/**Получить имя организации.*/
	 String getOrganization();
	/**Задать имя организации.*/
	 void setOrganization(String organization);
	/**Получить должность.*/
	 String getPost();
	/**Задать должность.*/
	 void setPost(String post);
	/**Получить инициалы.*/
	 String getInitials();
	/**Задать инициалы*/
	 void setInitials(String initials);
	String getEmail();
	void setEmail(String email);

	EnumGender getGender();

	void setGender(EnumGender gender);
}
