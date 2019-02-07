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
    public static readonly ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL = 'employment-histories';


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

    public static createUrlFromUrlTemplate(urlTemplate: string, urlParamName: string, urlParamValue: string) {
        return urlTemplate.replace(':' + urlParamName, urlParamValue);
    }
}
