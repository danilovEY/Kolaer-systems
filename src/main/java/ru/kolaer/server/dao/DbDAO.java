package ru.kolaer.server.dao;

import java.util.List;

public interface DbDAO<T> {
	List<T> getAll();
	List<T> getAllMaxCount(int count);
	int getRowCount();
}
