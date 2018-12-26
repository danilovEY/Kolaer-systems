package ru.kolaer.common.constant;

public interface Routers {

    String EMPLOYEE_SERVICE = "/employees";
    String EMPLOYEE_ID = EMPLOYEE_SERVICE + "/{" + PathVariables.EMPLOYEE_ID + "}";
    String EMPLOYEE_ID_ACHIEVEMENT = EMPLOYEE_ID + "/achievements";


    String DEPARTMENT_SERVICE = "/departments";
    String DEPARTMENT_FIND = DEPARTMENT_SERVICE + "/find";
    String DEPARTMENT_BY_ID = DEPARTMENT_SERVICE + "/{" + PathVariables.DEPARTMENT_ID + "}";

}
