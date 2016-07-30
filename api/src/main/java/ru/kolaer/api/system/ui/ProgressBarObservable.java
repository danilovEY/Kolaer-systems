package ru.kolaer.api.system.ui;

public interface ProgressBarObservable {
	double getValue();
	void setValue(double value);
	void notifyObservers();
	void removeObserverProgressBar(ProgressBarObserver observer);
	void registerObserverProgressBar(ProgressBarObserver observer);	
}
