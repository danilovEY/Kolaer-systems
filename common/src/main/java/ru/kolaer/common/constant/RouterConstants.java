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
    String EMPLOYEES_ID_COMBINATIONS = EMPLOYEES_ID + "/combinations";


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


    String VACATIONS = "/vacations";
    String VACATIONS_ID = VACATIONS + "/{" + PathVariableConstants.VACATION_ID + "}";
    String VACATIONS_PERIODS = VACATIONS + "/periods";
    String VACATIONS_BALANCE = VACATIONS + "/balance";
    String VACATIONS_CALCULATE = VACATIONS + "/calculate";
    String VACATIONS_CALCULATE_DAYS = VACATIONS_CALCULATE + "/days";
    String VACATIONS_CALCULATE_DATE = VACATIONS_CALCULATE + "/date";
    String VACATIONS_REPORT = VACATIONS + "/report";
    String VACATIONS_REPORT_CALENDAR = VACATIONS_REPORT + "/calendar";
    String VACATIONS_REPORT_CALENDAR_EXPORT = VACATIONS_REPORT_CALENDAR + "/export";
    String VACATIONS_REPORT_DISTRIBUTE = VACATIONS_REPORT + "/distribute";
    String VACATIONS_REPORT_TOTAL_COUNT = VACATIONS_REPORT + "/total_count";
    String VACATIONS_REPORT_EXPORT = VACATIONS_REPORT + "/export";


    String BANK = "bank";
    String BANK_ID = BANK + "/{" + PathVariableConstants.BANK_ACCOUNT_ID + "}";
    String BANK_EMPLOYEES = BANK + "/employees";

    String AUTHENTICATION = "/authentication";
    String AUTHENTICATION_LOGOUT = AUTHENTICATION + "/logout";
    String AUTHENTICATION_LOGIN = AUTHENTICATION + "/login";
    String AUTHENTICATION_GENERATE_PASS = AUTHENTICATION + "/genpass";
    String AUTHENTICATION_REFRESH_TOKEN = "/refresh";


    String ACCOUNTS = "/accounts";


    String USER = "/user";
    String USER_PASSWORD = USER + "/password";
    String USER_CONTACT = USER + "/contact";
    String USER_EMPLOYEE = USER + "/employee";


    String NON_SECURITY = "/non-security";
    String NON_SECURITY_TOOLS = NON_SECURITY + "/tools";
    String NON_SECURITY_TOOLS_CALCULATE = NON_SECURITY_TOOLS + "/calculate";
    String NON_SECURITY_TOOLS_CALCULATE_PERIOD = NON_SECURITY_TOOLS_CALCULATE + "/period";
    String NON_SECURITY_TOOLS_CALCULATE_PERIOD_DAYS = NON_SECURITY_TOOLS_CALCULATE_PERIOD + "/days";
    String NON_SECURITY_TOOLS_SERVER = NON_SECURITY_TOOLS + "/server";
    String NON_SECURITY_TOOLS_SERVER_TIME = NON_SECURITY_TOOLS_SERVER + "/time";


    String PLACEMENT = "/placement";


    String BUSINESS_TRIP = "/business_trip";
    String BUSINESS_TRIP_ID = BUSINESS_TRIP + "/{" + PathVariableConstants.BUSINESS_TRIP_ID + "}";
    String BUSINESS_TRIP_ID_EMPLOYEES = BUSINESS_TRIP_ID + "/employees";
    String BUSINESS_TRIP_ID_EMPLOYEE_ID = BUSINESS_TRIP_ID_EMPLOYEES + "/{" + PathVariableConstants.EMPLOYEE_ID + "}";
}
