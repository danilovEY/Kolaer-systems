package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@ApiModel("(Парольница) Репозиторий паролей")
@JsonIgnoreProperties({"historyPasswords"})
@JsonDeserialize(as = RepositoryPasswordBase.class)
public interface RepositoryPassword extends Serializable {
    Integer getId();
    void setId(Integer id);
    
    @ApiModelProperty("Наименование репозитория")
    String getName();
    void setName(String name);

    @ApiModelProperty("Сотрудник репозитория")
    EmployeeEntity getEmployee();
    void setEmployee(EmployeeEntity employee);

    @ApiModelProperty("Ссылка на иконку")
    String getUrlImage();
    void setUrlImage(String urlImage);

    @ApiModelProperty("Актуальный пароль")
    RepositoryPasswordHistory getLastPassword();
    void setLastPassword(RepositoryPasswordHistory lastPassword);

    @ApiModelProperty("Первый пароль")
    RepositoryPasswordHistory getFirstPassword();
    void setFirstPassword(RepositoryPasswordHistory firstPassword);

    @ApiModelProperty("Предыдущий пароль")
    RepositoryPasswordHistory getPrevPassword();
    void setPrevPassword(RepositoryPasswordHistory prevPassword);

    @ApiModelProperty("История паролей")
    List<RepositoryPasswordHistory> getHistoryPasswords();
    void setHistoryPasswords(List<RepositoryPasswordHistory> historyPasswords);
}
