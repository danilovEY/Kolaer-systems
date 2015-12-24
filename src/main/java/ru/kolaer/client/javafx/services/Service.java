package ru.kolaer.client.javafx.services;

public interface Service {
	void setRunningStatus(boolean isRun);
	boolean isRunning();
	
	void run() throws Exception;
	void stop()throws Exception;
}
