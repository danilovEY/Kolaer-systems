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
	private Set<String> closingWindows = new HashSet<>();
	
	public DbKolaerUser(final String user) {
		this.log = new UserLog(user);
		this.name = user;
		this.log.addSystemMessage("Пользователь \"" + this.name + "\" добавлен!");
	}
	public String getName() {
		return this.name;
	}
	
	public Set<String> getIpSet() {
		return this.ipSet;
	}

	public Set<String> getOpeningWindows() {
		return this.openingWindows;
	}

	public void addIp(final String ip) {
		if(this.ipSet.add(ip))
			this.log.addSystemMessage("Добавлен IP: " + ip);
	}
	
	public void addIps(final String... ipArray) {
		this.log.addSystemMessage("Новые IP:");
		this.ipSet.clear();
		for(final String ip : ipArray) {
			this.addIp(ip);
		}
	}
	
	public void addOpeningApplication(final String name) {
		if(this.openingWindows.add(name)){
			this.log.addSystemMessage("Окно \"" + name  + "\" открыто!");
		}
	}
	
	public void addCloseApplication(final String name) {
		this.closingWindows.add(name);
	}
	
	public Set<String> getCloseApplications() {
		return this.closingWindows;
	}
	
	public void removeOpeningApplication(final String name) {
		if(this.openingWindows.remove(name)){
			this.log.addSystemMessage("Окно \"" + name  + "\" закрыто!");
		}
	}
	
	public void addKey(final String key) {
		this.log.addTelemetryMessage(key);
	}
	
	public UserLog getLog() {
		return this.log;
	}
	
	public void disconect() {
		this.log.addSystemMessage("Пользователь \"" + this.name + "\" удален!");
		this.log.shutdown();
		ipSet.clear();
		openingWindows.clear();
	}
	/**
	 * @return the {@linkplain #ping}
	 */
	public boolean isPing() {
		return this.ping;
	}
	/**
	 * @param ping the {@linkplain #ping} to set
	 */
	public void setPing(boolean ping) {
		this.ping = ping;
	}	
}
