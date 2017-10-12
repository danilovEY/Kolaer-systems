package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 26.01.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("Результат обновления сотрудников")
public class ResultUpdateEmployeesDto implements Serializable {
    @ApiModelProperty("Кол-во удаленных должностей")
    private List<PostEntity> deletePost = new ArrayList<>();
    @ApiModelProperty("Кол-во добавленных должностей")
    private List<PostEntity> addPost = new ArrayList<>();

    @ApiModelProperty("Кол-во удаленных подразделений")
    private List<DepartmentEntity> deleteDep = new ArrayList<>();
    @ApiModelProperty("Кол-во добавленных подразделений")
    private List<DepartmentEntity> addDep = new ArrayList<>();

    @ApiModelProperty("Кол-во уволенных сотрудников")
    private List<EmployeeEntity> deleteEmployee = new ArrayList<>();
    @ApiModelProperty("Кол-во добавленных сотрудников")
    private List<EmployeeEntity> addEmployee = new ArrayList<>();

}
