package ru.kolaer.asmc.ui.javafx.controller;

public interface Observable {
	void notifyObserverClick();
	void registerOberver(Observer observer);
	void removeObserver(Observer observer);
}
