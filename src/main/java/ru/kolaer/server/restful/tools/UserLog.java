package ru.kolaer.server.restful.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UserLog {
	private static final String PATH_TO_USER_LOGS = "userslogs";
	private static final String PATH_TO_LOG_SYS = "system.log";
	private static final String PATH_TO_LOG_TELEMETRY_KEY = "telemetry_key.log";
	private final String userName;
	private String userLogDir;
	
	public UserLog(final String userName) {
		this.userName = userName;
		
		final LocalDate localDate = LocalDate.now();
		final File userLogDir = new File(PATH_TO_USER_LOGS + "/" + this.userName + "/" + localDate);
		if(!userLogDir.exists())
			userLogDir.mkdirs();
		
		final File userLogDirScreen = new File(PATH_TO_USER_LOGS + "/" + this.userName + "/" + localDate + "/screen");
		if(!userLogDirScreen.exists())
			userLogDirScreen.mkdirs();
		
		this.userLogDir = userLogDir.getAbsolutePath();
	}

	private FileWriter getOrCreateSystemLogger() throws IOException {
		final LocalDate localDate = LocalDate.now();
		if(!this.userLogDir.equals(PATH_TO_USER_LOGS + "/" + this.userName + "/" + localDate)) {
			final File userLogDir = new File(PATH_TO_USER_LOGS + "/" + this.userName + "/" + localDate);
			if(!userLogDir.exists())
				userLogDir.mkdirs();
			
			this.userLogDir = userLogDir.getAbsolutePath();
		}

		return new FileWriter(this.userLogDir + "/" + PATH_TO_LOG_SYS, true);
    }

	private FileWriter getOrCreateTelemetryLogger() throws IOException {	
		final LocalDate localDate = LocalDate.now();
		if(!this.userLogDir.equals(PATH_TO_USER_LOGS + "/" + this.userName + "/" + localDate)) {
			final File userLogDir = new File(PATH_TO_USER_LOGS + "/" + this.userName + "/" + localDate);
			if(!userLogDir.exists())
				userLogDir.mkdirs();
			
			this.userLogDir = userLogDir.getAbsolutePath();
		}
		
		return new FileWriter(this.userLogDir + "/" + PATH_TO_LOG_TELEMETRY_KEY, true);
    }
	
	public void addSystemMessage(final String message){
		try(final FileWriter sysLog = this.getOrCreateSystemLogger()) {
			sysLog.write(LocalTime.now()  + " | " + message);
			sysLog.write(System.lineSeparator());
			sysLog.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addTelemetryMessage(final String message) {
		try(final FileWriter telemetryLog = this.getOrCreateTelemetryLogger()) {
			telemetryLog.write(LocalTime.now()  + " | " + message);
			telemetryLog.write(System.lineSeparator());
			telemetryLog.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
	
	}

	public void addImage(final byte[] image) {
		if(image == null)
			return;
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H_m_s");
		final String text = LocalTime.now().format(formatter);
		final File file = new File(this.userLogDir + "/screen/" + text + "_screen.jpg");
		try{
			file.createNewFile();
		}catch(final IOException e1){
			e1.printStackTrace();
		}
		try(final FileOutputStream fos = new FileOutputStream(file)){
			fos.write(image);
			fos.flush();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}
}
