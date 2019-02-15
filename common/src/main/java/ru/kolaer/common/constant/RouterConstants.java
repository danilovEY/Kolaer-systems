package ru.kolaer.common.constant;

public interface RouterConstants {

    String EMPLOYEES = "/employees";
    String EMPLOYEES_DAYS_OF_BIRTHS = EMPLOYEES + "/days_of_births";
    String EMPLOYEES_SYNC = EMPLOYEES + "/sync";
    String EMPLOYEES_REPORT = EMPLOYEES + "/report";
    String EMPLOYEES_REPORT_OLD = EMPLOYEES_REPORT + "/old";
    String EMPLOYEES_ID = EMPLOYEES + "/{" + PathVariableConstants.EMPLOYEE_ID + "}";
    String EMPLOYEES_ID_ACHIEVEMENTS = EMPLOYEES_ID + "/achievements";
    String EMPLOYEES_ID_EDUCATIONS = EMPLOYEES_ID + "/educations";
    String EMPLOYEES_ID_EMPLOYMENT_HISTORIES = EMPLOYEES_ID + "/employment_histories";
    String EMPLOYEES_ID_MILITARY_REGISTRATIONS = EMPLOYEES_ID + "/military_registrations";
    String EMPLOYEES_ID_PERSONAL_DATA = EMPLOYEES_ID + "/personal_data";
    String EMPLOYEES_ID_PUNISHMENTS = EMPLOYEES_ID + "/punishments";
    String EMPLOYEES_ID_RELATIVES = EMPLOYEES_ID + "/relatives";
    String EMPLOYEES_ID_TYPE_WORK = EMPLOYEES_ID + "/type_work";
    String EMPLOYEES_ID_STAFF_MOVEMENTS = EMPLOYEES_ID + "/staff_movements";
    String EMPLOYEES_ID_PERSONAL_DOCUMENTS = EMPLOYEES_ID + "/personal_documents";


    String TYPE_WORKS = "/type-works";
    String TYPE_WORKS_ID = TYPE_WORKS + "/{" + PathVariableConstants.TYPE_WORK_ID + "}";


    String DEPARTMENTS = "/departments";
    String DEPARTMENTS_FIND = DEPARTMENTS + "/find";
    String DEPARTMENTS_ID = DEPARTMENTS + "/{" + PathVariableConstants.DEPARTMENT_ID + "}";


    String CONTACTS = "/contacts";
    String CONTACTS_DEPARTMENTS = CONTACTS + "/departments";
    String CONTACTS_DEPARTMENTS_ID = CONTACTS_DEPARTMENTS + "/{" + PathVariableConstants.DEPARTMENT_ID + "}";
    String CONTACTS_DEPARTMENTS_ID_TYPE = CONTACTS_DEPARTMENTS_ID + "/{" + PathVariableConstants.CONTACT_TYPE + "}";
    String CONTACTS_EMPLOYEES = CONTACTS + "/employees";
    String CONTACTS_EMPLOYEES_ID = CONTACTS_EMPLOYEES + "/{" + PathVariableConstants.EMPLOYEE_ID + "}";


    String AUTHENTICATION = "/authentication";
    String AUTHENTICATION_LOGOUT = AUTHENTICATION + "/logout";
    String AUTHENTICATION_LOGIN = AUTHENTICATION + "/login";
    String AUTHENTICATION_GENERATE_PASS = AUTHENTICATION + "/genpass";
    String AUTHENTICATION_REFRESH_TOKEN = "/refresh";


    String ACCOUNTS = "/accounts";


    String USER = "/user";
    String USER_WITH_EMPLOYEE = USER + "/employee";
    String USER_PASSWORD = USER + "/password";
    String USER_CONTACT = USER + "/contact";


    String NON_SECURITY = "/non-security";
    String NON_SECURITY_TOOLS = NON_SECURITY + "/tools";
    String NON_SECURITY_TOOLS_CALCULATE = NON_SECURITY_TOOLS + "/calculate";
    String NON_SECURITY_TOOLS_CALCULATE_PERIOD = NON_SECURITY_TOOLS_CALCULATE + "/period";
    String NON_SECURITY_TOOLS_CALCULATE_PERIOD_DAYS = NON_SECURITY_TOOLS_CALCULATE_PERIOD + "/days";
    String NON_SECURITY_TOOLS_SERVER = NON_SECURITY_TOOLS + "/server";
    String NON_SECURITY_TOOLS_SERVER_TIME = NON_SECURITY_TOOLS_SERVER + "/time";


}
