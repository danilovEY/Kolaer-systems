package ru.kolaer.server.restful.tools;

import java.io.File;
import java.time.LocalDate;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

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
		
		/*final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		final PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date | %msg%n");
        ple.setContext(lc);
        ple.start();
        
        SizeBasedTriggeringPolicy<ILoggingEvent> size = new SizeBasedTriggeringPolicy<>("5MB");
        
        LocalDate localDate = LocalDate.now();
    	final FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setFile(userLogDir.getAbsolutePath() + "/" + localDate + "/" + fileName);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();*/
		
		final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        
		final LocalDate localDate = LocalDate.now();
		final RollingFileAppender<ILoggingEvent> rfAppender = new RollingFileAppender<>();
	    rfAppender.setContext(lc);
	    rfAppender.setFile(userLogDir.getAbsolutePath() + "/" + localDate + "/" + fileName);
	    
	    final FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
	    rollingPolicy.setContext(lc);
	    rollingPolicy.setParent(rfAppender);
	    rollingPolicy.setFileNamePattern("%i.+" + fileName + ".zip");
	    rollingPolicy.start();

	    final SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<>();
	    triggeringPolicy.setMaxFileSize("5MB");
	    triggeringPolicy.start();

		final PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date | %msg%n");
        ple.setContext(lc);
        ple.start();
	    
	    rfAppender.setEncoder(ple);
	    rfAppender.setRollingPolicy(rollingPolicy);
	    rfAppender.setTriggeringPolicy(triggeringPolicy);

	    rfAppender.start();
        
        final Logger logger = (Logger) LoggerFactory.getLogger(userName);
        logger.detachAndStopAllAppenders(); 
        logger.addAppender(rfAppender);
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
