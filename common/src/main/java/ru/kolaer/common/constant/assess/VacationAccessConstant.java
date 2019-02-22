package ru.kolaer.common.constant.assess;

public interface VacationAccessConstant extends CommonAccessConstant {
    /**
     * Получить все отпуска
     */
    String VACATIONS_READ = PREFIX + "VACATIONS_READ";

    /**
     * Получить отпуска из своего подразделения
     */
    String VACATIONS_READ_DEPARTMENT = PREFIX + "VACATIONS_READ_DEPARTMENT";

    /**
     * Добавить отпуск сотруднику
     */
    String VACATIONS_WRITE = PREFIX + "VACATIONS_ADD";

    /**
     * Добавить отпуск сотруднику из своего подразделения
     */
    String VACATIONS_WRITE_DEPARTMENT = PREFIX + "VACATIONS_ADD_DEPARTMENT";

    /**
     * Получить баланс дней у сотрудников
     */
    String VACATIONS_BALANCE_READ = PREFIX + "VACATIONS_DELETE";

    /**
     * Получить баланс дней у сотрудников из своего подразделения
     */
    String VACATIONS_BALANCE_READ_DEPARTMENT = PREFIX + "VACATIONS_DELETE_DEPARTMENT";

    /**
     * Изменить баланс дней у сотрудников
     */
    String VACATIONS_BALANCE_WRITE = PREFIX + "VACATIONS_DELETE_DEPARTMENT";

    String VACATIONS_REPORT_GENERATE = PREFIX + "VACATIONS_DELETE_DEPARTMENT";
}
