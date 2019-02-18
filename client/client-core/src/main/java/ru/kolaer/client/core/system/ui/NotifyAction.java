package ru.kolaer.client.core.system.ui;

import javafx.event.ActionEvent;

import java.util.function.Consumer;

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
