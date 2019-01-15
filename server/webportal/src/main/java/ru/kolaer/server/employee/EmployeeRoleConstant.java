package ru.kolaer.server.employee;

import ru.kolaer.server.core.security.BaseRoleConstant;

public interface EmployeeRoleConstant extends BaseRoleConstant {
    String OK = "OK";
    String OK_WITH_PREFIX = PREFIX + OK;

    String TYPE_WORK = "TYPE_WORK;";
    String TYPE_WORK_WITH_PREFIX = PREFIX + TYPE_WORK;
}
