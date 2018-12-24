package ru.kolaer.common.constant;

public interface Routers {

    String DEPARTMENT_SERVICE = "/departments";

    String DEPARTMENT_FIND = DEPARTMENT_SERVICE + "/find";
    String DEPARTMENT_BY_ID = DEPARTMENT_SERVICE + "/{depId}";

}
