package ru.kolaer.server.restful.tools;

import java.io.File;
import java.time.LocalDate;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public class UserLog {
	private static final String PATH_TO_USER_LOGS = "userslogs";
	private static final String PATH_TO_LOG_SYS = "system.log";
	private static final String PATH_TO_LOG_TELEMETRY_KEY = "telemetry_key.log";
	
	private final String userName;
	private final Logger userSystemLogger;
	private final Logger userTelemetryLogger;
	
	public UserLog(final String userName) {
		this.userName = userName;
		this.userSystemLogger = this.createLogger(userName+"_sys", PATH_TO_LOG_SYS);
		this.userTelemetryLogger = this.createLogger(userName+"_telemetry", PATH_TO_LOG_TELEMETRY_KEY);
	}

	private Logger createLogger(final String userName, final String fileName) {
		final File userLogDir = new File(PATH_TO_USER_LOGS + "/" + this.userName);
		if(!userLogDir.exists())
			userLogDir.mkdirs();
		
		final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		final PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date | %msg%n");
        ple.setContext(lc);
        ple.start();
        
        LocalDate localDate = LocalDate.now();
    	final FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setFile(userLogDir.getAbsolutePath() + "/" + localDate + "/" + fileName);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();
        
        final Logger logger = (Logger) LoggerFactory.getLogger(userName);
        logger.detachAndStopAllAppenders(); 
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);
        return logger;
	}

	public void addSystemMessage(final String message){
		this.userSystemLogger.info(message);
	}
	
	public String getUserName() {
		return userName;
	}

	public Logger getUserSystemLogger() {
		return userSystemLogger;
	}
	
	public void shutdown() {
		this.userSystemLogger.iteratorForAppenders().forEachRemaining(a -> a.stop());
		this.userTelemetryLogger.iteratorForAppenders().forEachRemaining(a -> a.stop());
	}

	public Logger getUserTelemetryLogger() {
		return userTelemetryLogger;
	}
}
