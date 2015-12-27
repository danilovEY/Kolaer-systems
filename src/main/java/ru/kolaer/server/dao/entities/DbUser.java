package ru.kolaer.server.dao.entities;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class DbUser {
	private String name;
	private String ip;
	/**
	 * {@linkplain DbUser.java}
	 */
	public DbUser() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(final String ip) {
		this.ip = ip;
	}
}
