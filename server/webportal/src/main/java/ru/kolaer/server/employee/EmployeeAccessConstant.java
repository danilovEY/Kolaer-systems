package ru.kolaer.server.employee;

import ru.kolaer.server.core.security.CommonAccessConstant;

public interface EmployeeAccessConstant extends CommonAccessConstant {
    String TYPE_WORKS_ADD = PREFIX + "TYPE_WORKS_ADD";
    String TYPE_WORKS_GET = PREFIX + "TYPE_WORKS_GET";
    String TYPE_WORKS_EDIT = PREFIX + "TYPE_WORKS_EDIT";
    String TYPE_WORKS_DELETE = PREFIX + "TYPE_WORKS_DELETE";

    String DEPARTMENTS_ADD = PREFIX + "DEPARTMENTS_ADD";
    String DEPARTMENTS_GET = PREFIX + "DEPARTMENTS_GET";
    String DEPARTMENTS_EDIT = PREFIX + "DEPARTMENTS_EDIT";
    String DEPARTMENTS_DELETE = PREFIX + "DEPARTMENTS_DELETE";

    String EMPLOYEES_GET = PREFIX + "EMPLOYEES_GET";
    String EMPLOYEES_ADD = PREFIX + "EMPLOYEES_ADD";
    String EMPLOYEES_SYNC = PREFIX + "EMPLOYEES_SYNC";
    String EMPLOYEES_REPORT_OLD = PREFIX + "EMPLOYEES_REPORT_OLD";

    String EMPLOYEE_GET = PREFIX + "EMPLOYEE_GET";
    String EMPLOYEE_EDIT = PREFIX + "EMPLOYEE_EDIT";
    String EMPLOYEE_DELETE = PREFIX + "EMPLOYEE_DELETE";

    String EMPLOYEE_GET_CURRENT = PREFIX + "EMPLOYEE_GET_CURRENT";
    String EMPLOYEE_TYPE_WORK_EDIT_DEPARTMENT = PREFIX + "EMPLOYEE_TYPE_WORK_EDIT_DEPARTMENT";
    String EMPLOYEE_EDUCATIONS_GET = PREFIX + "EMPLOYEE_EDUCATIONS_GET";
    String EMPLOYEE_ACHIEVEMENTS_GET = PREFIX + "EMPLOYEE_ACHIEVEMENTS_GET";
    String EMPLOYEE_EMPLOYMENT_HISTORIES_GET = PREFIX + "EMPLOYEE_EMPLOYMENT_HISTORIES_GET";
    String EMPLOYEE_MILITARY_REGISTRATIONS_GET = PREFIX + "EMPLOYEE_MILITARY_REGISTRATIONS_GET";
    String EMPLOYEE_PERSONAL_DATA_GET = PREFIX + "EMPLOYEE_PERSONAL_DATA_GET";
    String EMPLOYEE_PUNISHMENTS_GET = PREFIX + "EMPLOYEE_PUNISHMENTS_GET";
    String EMPLOYEE_RELATIVES_GET = PREFIX + "EMPLOYEE_RELATIVES_GET";
    String EMPLOYEE_STAFF_MOVEMENTS_GET = PREFIX + "EMPLOYEE_STAFF_MOVEMENTS_GET";
    String EMPLOYEE_PERSONAL_DOCUMENT_GET_GET = PREFIX + "EMPLOYEE_PERSONAL_DOCUMENT_GET_GET";
}
