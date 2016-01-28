package ru.kolaer.client.javafx.services;

public interface Service extends Runnable {
	void setRunningStatus(boolean isRun);
	boolean isRunning();
	
	String getName();
	
	void stop();
}
