package ru.kolaer.client.javafx.system;

import ru.kolaer.server.dao.entities.DbUsers1c;

public interface User1cDataBase {
	DbUsers1c[] getAllUser();
	DbUsers1c[] getUsersMax(int maxCount);
}
