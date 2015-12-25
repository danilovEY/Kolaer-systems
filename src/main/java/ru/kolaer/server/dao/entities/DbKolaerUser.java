package ru.kolaer.server.dao.entities;

import java.util.HashSet;
import java.util.Set;

import ru.kolaer.server.restful.tools.UserLog;

public class DbKolaerUser {
	private final String name;
	private final UserLog log;
	
	private Set<String> ipSet = new HashSet<>();
	private Set<String> openingWindows = new HashSet<>();
	
	public DbKolaerUser(final String user) {
		this.log = new UserLog(user);
		this.name = user;
		this.log.addSystemMessage("Добавлен пользователь: " + user);
	}
	public String getName() {
		return name;
	}
	
	public Set<String> getIpSet() {
		return ipSet;
	}

	public Set<String> getOpeningWindows() {
		return openingWindows;
	}

	public void addIp(final String ip) {
		this.ipSet.add(ip);
		this.log.addSystemMessage("Добавлен IP: " + ip);
	}
	
	public void addOpeningWindow(final String name) {
		this.openingWindows.add(name);
	}
	
	public void addKey(final String key) {
		this.log.getUserTelemetryLogger().info(key);
	}
	
	public UserLog getLog() {
		return log;
	}
	
	public void disconect() {
		log.shutdown();
		ipSet.clear();
		openingWindows.clear();
	}	
}
