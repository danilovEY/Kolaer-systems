import {PathVariableConstant} from "./path-variable.constant";

export class RouterClientConstant {
    public static readonly APP_PART_URL = 'app';
    public static readonly PAGES_PART_URL = 'pages';
    public static readonly ORG_STRUCTURES_PART_URL = 'org-structures';
    public static readonly ORG_STRUCTURES_EMPLOYEES_PART_URL = 'employees';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_PART_URL = ':' + PathVariableConstant.EMPLOYEE_ID;
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL = 'details';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL = 'commons';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_PART_URL = 'educations';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_PART_URL = 'achievements';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_PART_URL = 'punishments';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL = 'employment_histories';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_PART_URL = 'staff_movements';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_PART_URL = 'combinations';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_PART_URL = 'vacations';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_PART_URL = 'personal_data';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_PART_URL = 'relatives';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_PART_URL = 'personal_documents';
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_PART_URL = 'military_registration';

    public static readonly BUSINESS_TRIP_PART_URL = 'business_trip';
    public static readonly BUSINESS_TRIP_LIST_PART_URL = 'list';
    public static readonly BUSINESS_TRIP_ID_PART_URL = ':' + PathVariableConstant.BUSINESS_TRIP_ID + '/details';
    public static readonly BUSINESS_TRIP_CREATE_PART_URL = 'create';

    public static readonly PAGES_URL = `/${RouterClientConstant.PAGES_PART_URL}`;


    public static readonly APP_URL = RouterClientConstant.PAGES_URL + `/${RouterClientConstant.APP_PART_URL}`;

    // ------BUSINESS_TRIP_URL---------
    public static readonly BUSINESS_TRIP_URL = RouterClientConstant.APP_URL + `/${RouterClientConstant.BUSINESS_TRIP_PART_URL}`;

    public static readonly BUSINESS_TRIP_LIST_URL = RouterClientConstant.BUSINESS_TRIP_URL +
        `/${RouterClientConstant.BUSINESS_TRIP_LIST_PART_URL}`;

    public static readonly BUSINESS_TRIP_CREATE_URL = RouterClientConstant.BUSINESS_TRIP_URL +
        `/${RouterClientConstant.BUSINESS_TRIP_CREATE_PART_URL}`;

    public static readonly BUSINESS_TRIP_ID_URL = RouterClientConstant.BUSINESS_TRIP_URL +
        `/${RouterClientConstant.BUSINESS_TRIP_ID_PART_URL}`;


    // ------ORG_STRUCTURES_URL---------
    public static readonly ORG_STRUCTURES_URL = RouterClientConstant.APP_URL + `/${RouterClientConstant.ORG_STRUCTURES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_URL = RouterClientConstant.ORG_STRUCTURES_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_URL = RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL = RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_URL = RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_URL =
        RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_PART_URL}`;


}
