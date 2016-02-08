package ru.kolaer.client.javafx.system;

import java.util.LinkedList;
import java.util.List;

/**
 * Реализация стандартного статус-бара.
 * 
 * @author danilovey
 * @version 0.1
 */
public class DefaultProgressBar implements ProgressBarObservable {
	private double value = 0;
	private final List<ProgressBarObserver> observers = new LinkedList<>();
	
	@Override
	public double getValue() {
		return this.value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
		this.notifyObservers();
	}

	@Override
	public void notifyObservers() {
		this.observers.parallelStream().forEach(obs -> obs.updateValue(this.value));
	}

	@Override
	public void removeObserverProgressBar(final ProgressBarObserver observer) {
		this.observers.remove(observer);
	}

	@Override
	public void registerObserverProgressBar(final ProgressBarObserver observer) {
		this.observers.add(observer);
	}

}
