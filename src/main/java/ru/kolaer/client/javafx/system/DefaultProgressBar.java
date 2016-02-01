package ru.kolaer.client.javafx.system;

public class DefaultProgressBar implements ProgressBar {
	private int value = 0;
	
	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public void setValue(int value) {
		this.value = value;
	}

}
