package ru.kolaer.birthday.mvp.model.impl;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.PostEntity;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.mvp.model.other.DbDataAll;
import ru.kolaer.birthday.mvp.model.UserModel;

import java.util.Date;

/**
 * Реализация {@linkplain UserModel}.
 *
 * @author danilovey
 * @version 0.1
 */
public class UserModelImpl implements UserModel {
	/**Фамилия.*/
	private String firstName;
	/**Имя.*/
	private String secondName;
	/**Отчество.*/
	private String thirdName;
	/**Отдел.*/
	private String department;
	/**Организация.*/
	private String organization;
	/**Дата рождения.*/
	private Date birthday;
	/**Путь к фото.*/
	private String icon;
	/**Телефонный номер.*/
	private String phoneNumber;
	/**Должность.*/
	private String post;
	/**Инициалы.*/
	private String initials;
	private String email;
	
	public UserModelImpl(final String organization, 
			final String firstName, 
			final String secondName, 
			final String thirdName, 
			final String department,
			final Date birthday, 
			final String icon, 
			final String phoneNumber, 
			final String post,
			final String initials){
		this.organization = organization;
		this.firstName = firstName;
		this.secondName = secondName;
		this.thirdName = thirdName;
		this.department = department;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.icon = icon;
		this.post = post;
		this.initials = initials;
	}
	
	public UserModelImpl() {
		
	}
	
	public UserModelImpl(final DbDataAll user) {
		this.setOrganization("КолАтомэнергоремонт");
		this.setInitials(user.getInitials());
		this.setBirthday(user.getBirthday());
		this.setDepartment(user.getDepartament());
		this.setPost(user.getPost());
		this.setPhoneNumber(user.getPhone());
		this.setEmail(user.getEmail());
		this.setFirstName(user.getName());
		this.setSecondName(user.getSurname());
		this.setThirdName(user.getPatronymic());
		this.setIcon(user.getVCard());
	}

	public UserModelImpl(final EmployeeEntity user) {
		this.setOrganization("КолАтомэнергоремонт");
		this.setInitials(user.getInitials());
		this.setBirthday(user.getBirthday());
		this.setDepartment(user.getDepartment().getAbbreviatedName());

		PostEntity postEntity = user.getPostEntity();
		String postName = postEntity.getName();
		if(postEntity.getTypeRang() != null && !postEntity.getTypeRang().trim().isEmpty())
			postName += " " + postEntity.getRang() + " " + postEntity.getTypeRang();

		this.setPost(postName);
		this.setPhoneNumber(user.getWorkPhoneNumber());
		this.setEmail("");
		this.setIcon(user.getInitials() + ".jpg");
	}

	public UserModelImpl(final EmployeeOtherOrganization user) {
		this.setOrganization(user.getOrganization());
		this.setInitials(user.getInitials());
		this.setBirthday(user.getBirthday());
		this.setDepartment(user.getDepartment());
		this.setPost(user.getPost());
		this.setPhoneNumber(user.getPhone());
		this.setEmail(user.getEmail());
		this.setIcon(user.getPhoto());
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(final String departament) {
		this.department = departament;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
