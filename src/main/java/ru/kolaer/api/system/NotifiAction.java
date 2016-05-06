package ru.kolaer.api.system;

import java.util.function.Consumer;

import javafx.event.ActionEvent;

public class NotifiAction {
	private final String text;
	private final Consumer<ActionEvent> consumer;
	
	public NotifiAction(final String text, final Consumer<ActionEvent> consumer) {
		this.text = text;
		this.consumer = consumer;
	}

	public String getText() {
		return text;
	}

	public Consumer<ActionEvent> getConsumer() {
		return consumer;
	}
}
