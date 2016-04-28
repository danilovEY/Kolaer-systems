package ru.kolaer.server.dao;

import java.util.List;

public interface DbInsertDataDAO<T> {
	void insertData(T data);
	void insertDataList(List<T> dataList);
}
