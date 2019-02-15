package ru.kolaer.common.constant.assess;

public interface AccountAccessConstant extends CommonAccessConstant {
    /**
     * Генерация пароля
     */
    String GENERATE_PASSWORDS = PREFIX + "GENERATE_PASSWORDS";

    /**
     * Получить всех пользователей
     */
    String ACCOUNTS_GET = PREFIX + "ACCOUNTS_GET";

    /**
     * Отредактировать пользователей
     */
    String ACCOUNTS_EDIT = PREFIX + "ACCOUNTS_EDIT";

}
