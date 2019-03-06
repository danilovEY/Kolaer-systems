package ru.kolaer.server.employee.model.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by danilovey on 12.09.2016.
 */
@StaticMetamodel(DepartmentEntity.class)
public class DepartmentEntity_ {

    public static volatile SingularAttribute<DepartmentEntity, Long> id;
    public static volatile SingularAttribute<DepartmentEntity, String> name;
    public static volatile SingularAttribute<DepartmentEntity, String> abbreviatedName;
    public static volatile SingularAttribute<DepartmentEntity, Long> chiefEmployeeId;
    public static volatile SingularAttribute<DepartmentEntity, Boolean> deleted;
    public static volatile SingularAttribute<DepartmentEntity, String> externalId;
    public static volatile SingularAttribute<DepartmentEntity, Integer> code;

}
