package ru.kolaer.common.constant;

public interface RouterConstants {

    String EMPLOYEES = "/employees";
    String EMPLOYEE_ID = EMPLOYEES + "/{" + PathVariableConstants.EMPLOYEE_ID + "}";
    String EMPLOYEE_ID_ACHIEVEMENTS = EMPLOYEE_ID + "/achievements";
    String EMPLOYEE_ID_EDUCATIONS = EMPLOYEE_ID + "/educations";
    String EMPLOYEE_ID_EMPLOYMENT_HISTORIES = EMPLOYEE_ID + "/employment_histories";
    String EMPLOYEE_ID_MILITARY_REGISTRATIONS = EMPLOYEE_ID + "/military_registrations";
    String EMPLOYEE_ID_PERSONAL_DATA = EMPLOYEE_ID + "/personal_data";
    String EMPLOYEE_ID_PUNISHMENTS = EMPLOYEE_ID + "/punishments";
    String EMPLOYEE_ID_RELATIVES = EMPLOYEE_ID + "/relatives";


    String DEPARTMENTS = "/departments";
    String DEPARTMENT_FIND = DEPARTMENTS + "/find";
    String DEPARTMENT_BY_ID = DEPARTMENTS + "/{" + PathVariableConstants.DEPARTMENT_ID + "}";


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
