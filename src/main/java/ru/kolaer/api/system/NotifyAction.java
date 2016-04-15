package ru.kolaer.api.system;

import java.util.function.Consumer;

import javafx.event.ActionEvent;

public class NotifyAction {
	private final String text;
	private final Consumer<ActionEvent> consumer;
	
	public NotifyAction(final String text, final Consumer<ActionEvent> consumer) {
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
