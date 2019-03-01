export class RoleConstant {
    public static readonly PREFIX: string = 'ROLE_';

    public static readonly BANK_ACCOUNTS_READ: string = RoleConstant.PREFIX + 'BANK_ACCOUNTS_READ';
    public static readonly TICKET_REGISTER_REPORT: string = RoleConstant.PREFIX + 'TICKET_REGISTER_REPORT';
    public static readonly TICKET_REGISTER_WRITE: string = RoleConstant.PREFIX + 'TICKET_REGISTER_WRITE';


    public static readonly VACATIONS_READ: string = RoleConstant.PREFIX + 'VACATIONS_READ';
    public static readonly VACATIONS_READ_DEPARTMENT: string = RoleConstant.PREFIX + 'VACATIONS_READ_DEPARTMENT';
    public static readonly VACATIONS_WRITE: string = RoleConstant.PREFIX + 'VACATIONS_WRITE';
    public static readonly VACATIONS_WRITE_DEPARTMENT: string = RoleConstant.PREFIX + 'VACATIONS_WRITE_DEPARTMENT';


    public static readonly EMPLOYEES_READ: string = RoleConstant.PREFIX + 'EMPLOYEES_READ';
    public static readonly EMPLOYEES_WRITE: string = RoleConstant.PREFIX + 'EMPLOYEES_WRITE';
    public static readonly EMPLOYEES_SYNC: string = RoleConstant.PREFIX + 'EMPLOYEES_SYNC';
    public static readonly EMPLOYEES_REPORT_OLD: string = RoleConstant.PREFIX + 'EMPLOYEES_REPORT_OLD';


    public static readonly DEPARTMENTS_WRITE: string = RoleConstant.PREFIX + 'DEPARTMENTS_WRITE';


    public static readonly POSTS_WRITE: string = RoleConstant.PREFIX + 'POSTS_WRITE';


    public static readonly TYPE_WORKS_READ: string = RoleConstant.PREFIX + 'TYPE_WORKS_READ';


    public static readonly CONTACTS_WRITE: string = RoleConstant.PREFIX + 'CONTACTS_WRITE';
    public static readonly CONTACTS_DEPARTMENT_WRITE: string = RoleConstant.PREFIX + 'CONTACTS_DEPARTMENT_WRITE';
}
