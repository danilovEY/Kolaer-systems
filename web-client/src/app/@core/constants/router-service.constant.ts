import {PathVariableConstant} from "./path-variable.constant";
import {environment} from "../../../environments/environment";

export class RouterServiceConstant {
    public static readonly ROOT_URL = environment.publicServerUrl;

    public static readonly EMPLOYEES_URL: string = RouterServiceConstant.ROOT_URL + '/employees';
    public static readonly EMPLOYEES_ID_URL: string = RouterServiceConstant.EMPLOYEES_URL + `/:${PathVariableConstant.EMPLOYEE_ID}`;

    public static readonly EMPLOYEES_ID_TYPE_WORK_URL: string =
        RouterServiceConstant.EMPLOYEES_ID_URL + `/type-work`;

    public static readonly EMPLOYEES_ALL_URL: string = `${RouterServiceConstant.EMPLOYEES_URL}`;
    public static readonly EMPLOYEES_SYNC: string = `${RouterServiceConstant.EMPLOYEES_URL}/sync`;
    public static readonly EMPLOYEES_OLD_REPORT: string = `${RouterServiceConstant.EMPLOYEES_URL}/old/report`;
    public static readonly EMPLOYEES_ALL_BIRTHDAY: string = `${RouterServiceConstant.EMPLOYEES_URL}/get/birthday/today`;
    public static readonly OTHER_EMPLOYEES_ALL_BIRTHDAY: string =
        RouterServiceConstant.ROOT_URL + `/organizations/employees/get/users/birthday/today`;

    public static readonly EMPLOYEES_ID_EDUCATION_URL: string =
        RouterServiceConstant.EMPLOYEES_ID_URL + `/educations`;

    public static readonly USER_URL: string = RouterServiceConstant.ROOT_URL + `/user`;
    public static readonly USER_EMPLOYEE_URL: string = RouterServiceConstant.USER_URL + `/employee`;
    public static readonly USER_CONTACT_URL: string = RouterServiceConstant.USER_URL + `/contact`;


    public static readonly DEPARTMENTS_URL: string = RouterServiceConstant.ROOT_URL + `/departments`;
    public static readonly DEPARTMENTS_ID_URL: string = RouterServiceConstant.DEPARTMENTS_URL +
        `/:${PathVariableConstant.DEPARTMENT_ID}`;


    public static readonly CONTACTS_URL: string = RouterServiceConstant.ROOT_URL + `/contacts`;
    public static readonly CONTACTS_EMPLOYEES_URL: string = RouterServiceConstant.CONTACTS_URL + `/employees`;
    public static readonly CONTACTS_EMPLOYEES_ID_URL: string = RouterServiceConstant.CONTACTS_EMPLOYEES_URL +
        `/:${PathVariableConstant.EMPLOYEE_ID}`;
    public static readonly CONTACTS_DEPARTMENTS_URL: string = RouterServiceConstant.CONTACTS_URL + `/departments`;
    public static readonly CONTACTS_DEPARTMENTS_ID_URL: string = RouterServiceConstant.CONTACTS_DEPARTMENTS_URL +
        `/:${PathVariableConstant.DEPARTMENT_ID}`;
    public static readonly CONTACTS_DEPARTMENTS_ID_TYPE_URL: string = RouterServiceConstant.CONTACTS_DEPARTMENTS_ID_URL +
        `/:${PathVariableConstant.CONTACT_TYPE}`;
}
