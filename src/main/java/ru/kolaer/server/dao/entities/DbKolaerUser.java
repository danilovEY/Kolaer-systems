package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.kolaer.server.restful.tools.UserLog;

@JsonIgnoreProperties("log")
public class DbKolaerUser implements Serializable {
	private static final long serialVersionUID = -4533583948766410710L;
	
	private final String name;
	private final transient UserLog log;
	private boolean ping = true;
	
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
		if(this.openingWindows.add(name)){
			this.log.getUserSystemLogger().info("Окно \"{}\" открыто!", name);
		}
	}
	
	public void removeOpeningWindow(final String name) {
		if(this.openingWindows.remove(name)){
			this.log.getUserSystemLogger().info("Окно \"{}\" закрыто!", name);
		}
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
	/**
	 * @return the {@linkplain #ping}
	 */
	public boolean isPing() {
		return ping;
	}
	/**
	 * @param ping the {@linkplain #ping} to set
	 */
	public void setPing(boolean ping) {
		this.ping = ping;
	}	
}
