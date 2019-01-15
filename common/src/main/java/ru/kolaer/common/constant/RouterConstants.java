package ru.kolaer.common.constant;

public interface RouterConstants {

    String EMPLOYEES = "/employees";
    String EMPLOYEE_ID = EMPLOYEES + "/{" + PathVariableConstants.EMPLOYEE_ID + "}";
    String EMPLOYEE_ID_ACHIEVEMENT = EMPLOYEE_ID + "/achievements";
    String EMPLOYEE_ID_EDUCATION = EMPLOYEE_ID + "/educations";


    String DEPARTMENTS = "/departments";
    String DEPARTMENT_FIND = DEPARTMENTS + "/find";
    String DEPARTMENT_BY_ID = DEPARTMENTS + "/{" + PathVariableConstants.DEPARTMENT_ID + "}";


    String AUTHENTICATION = "/authentication";
    String AUTHENTICATION_LOGUOT = AUTHENTICATION + "/logout";
    String AUTHENTICATION_LOGIN = AUTHENTICATION + "/login";
    String AUTHENTICATION_GENERATE_PASS = AUTHENTICATION + "/genpass";
    String AUTHENTICATION_REFRESH_TOKEN = "/refresh";


    String ACCOUNTS = "/accounts";


    String NON_SECURITY = "/non-security";
    String NON_SECURITY_TOOLS = NON_SECURITY + "/tools";
    String NON_SECURITY_TOOLS_CALCULATE = NON_SECURITY_TOOLS + "/calculate";
    String NON_SECURITY_TOOLS_CALCULATE_PERIOD = NON_SECURITY_TOOLS_CALCULATE + "/period";
    String NON_SECURITY_TOOLS_CALCULATE_PERIOD_DAYS = NON_SECURITY_TOOLS_CALCULATE_PERIOD + "/days";
    String NON_SECURITY_TOOLS_SERVER = NON_SECURITY_TOOLS + "/server";
    String NON_SECURITY_TOOLS_SERVER_TIME = NON_SECURITY_TOOLS_SERVER + "/time";
}
