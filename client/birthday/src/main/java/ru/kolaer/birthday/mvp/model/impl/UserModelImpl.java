package ru.kolaer.birthday.mvp.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.employee.EnumGender;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.dto.post.PostDto;

import java.util.Date;

/**
 * Реализация {@linkplain UserModel}.
 *
 * @author danilovey
 * @version 0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	private EnumGender gender;

	public UserModelImpl(EmployeeDto user) {
		setOrganization("КолАтомэнергоремонт");
		setInitials(user.getInitials());
		setBirthday(user.getBirthday());
		setDepartment(user.getDepartment().getAbbreviatedName());

		PostDto postEntity = user.getPost();
		String postName = postEntity.getName();
		if(postEntity.getType() != null) {
			postName += " " + postEntity.getRang() + " " + postEntity.getType().getName();
		}

        String photoUrl = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getUrl() + user.getPhoto();

		setPost(postName);
		setIcon(photoUrl);
		setGender(user.getGender());
	}

	public UserModelImpl(EmployeeOtherOrganizationDto user) {
		setOrganization(user.getOrganization());
		setInitials(user.getInitials());
		setBirthday(user.getBirthday());
		setDepartment(user.getDepartment());
		setPost(user.getPost());
		setPhoneNumber(user.getWorkPhoneNumber());
		setEmail(user.getEmail());
		setIcon(user.getPhoto());
	}
}
