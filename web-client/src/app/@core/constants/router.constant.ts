import {PathVariableConstant} from "./path-variable.constant";

export class RouterConstant {
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


    public static readonly PAGES_URL = `/${RouterConstant.PAGES_PART_URL}`;

    public static readonly APP_URL = RouterConstant.PAGES_URL + `/${RouterConstant.APP_PART_URL}`;



    // ------ORG_STRUCTURES_URL---------
    public static readonly ORG_STRUCTURES_URL = RouterConstant.APP_URL + `/${RouterConstant.ORG_STRUCTURES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_URL = RouterConstant.ORG_STRUCTURES_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_URL = RouterConstant.ORG_STRUCTURES_EMPLOYEES_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL = RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_URL = RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_URL = RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_URL = RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_URL = RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_PART_URL}`;

    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_URL =
        RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_URL +
        `/${RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_PART_URL}`;

    public static createUrlFromUrlTemplate(urlTemplate: string, urlParamName: string, urlParamValue: string) {
        return urlTemplate.replace(':' + urlParamName, urlParamValue);
    }
}
