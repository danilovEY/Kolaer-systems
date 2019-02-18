package ru.kolaer.common.constant.assess;

public interface VacationAccessConstant extends CommonAccessConstant {
    /**
     * Получить все отпуска
     */
    String VACATIONS_GET = PREFIX + "VACATIONS_GET";

    /**
     * Получить отпуска из своего подразделения
     */
    String VACATIONS_GET_DEPARTMENT = PREFIX + "VACATIONS_GET_DEPARTMENT";

    /**
     * Добавить отпуск сотруднику
     */
    String VACATIONS_ADD = PREFIX + "VACATIONS_ADD";

    /**
     * Добавить отпуск сотруднику из своего подразделения
     */
    String VACATIONS_ADD_DEPARTMENT = PREFIX + "VACATIONS_ADD_DEPARTMENT";

    /**
     * Редактировать отпуск
     */
    String VACATIONS_EDIT = PREFIX + "VACATIONS_EDIT";

    /**
     * Редактировать отпуск из своего подразделения
     */
    String VACATIONS_EDIT_DEPARTMENT = PREFIX + "VACATIONS_EDIT_DEPARTMENT";

    /**
     * Удалить отпуск
     */
    String VACATIONS_DELETE = PREFIX + "VACATIONS_DELETE";

    /**
     * Удалить отпуск из своего подразделения
     */
    String VACATIONS_DELETE_DEPARTMENT = PREFIX + "VACATIONS_DELETE_DEPARTMENT";

    /**
     * Получить баланс дней у сотрудников
     */
    String VACATIONS_BALANCE_GET = PREFIX + "VACATIONS_DELETE";

    /**
     * Получить баланс дней у сотрудников из своего подразделения
     */
    String VACATIONS_BALANCE_GET_DEPARTMENT = PREFIX + "VACATIONS_DELETE_DEPARTMENT";

    /**
     * Изменить баланс дней у сотрудников
     */
    String VACATIONS_BALANCE_EDIT = PREFIX + "VACATIONS_DELETE_DEPARTMENT";

    /**
     * Изменить баланс дней у сотрудников
     */
    String VACATIONS_REPORT_GENERATE = PREFIX + "VACATIONS_DELETE_DEPARTMENT";
}
