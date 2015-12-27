package ru.kolaer.client.javafx.services;

public class UserLocaleService implements LocaleService {
	private boolean isRunning = false;
	
	@Override
	public void setRunningStatus(boolean isRun) {
		this.isRunning = isRun;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
	public void run() throws Exception {
		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
