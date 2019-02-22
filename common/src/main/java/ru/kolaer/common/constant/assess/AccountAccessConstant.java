package ru.kolaer.common.constant.assess;

public interface AccountAccessConstant extends CommonAccessConstant {
    /**
     * Генерация пароля
     */
    String GENERATE_PASSWORDS = PREFIX + "GENERATE_PASSWORDS";

    /**
     * Получить всех пользователей
     */
    String ACCOUNTS_READ = PREFIX + "ACCOUNTS_READ";

    /**
     * Отредактировать пользователей
     */
    String ACCOUNTS_WRITE = PREFIX + "ACCOUNTS_WRITE";

}
