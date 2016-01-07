package ru.kolaer.server.restful.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import ru.kolaer.server.dao.entities.DbKolaerUser;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class UsersManager {
	private final Map<String, DbKolaerUser> connectionUsers = new HashMap<>();
	private final ReentrantLock lock = new ReentrantLock();
	/**
	 * {@linkplain UsersManager}
	 */
	public UsersManager() {
		
	}

	public Collection<DbKolaerUser> getUsers() {
		return this.connectionUsers.values();
	}
	
	public DbKolaerUser getUserByName(final String user) {
		return this.getUserByName(user, false);
	}
	
	public DbKolaerUser getUserByName(final String user, final boolean create) {
		final DbKolaerUser kolaerUser = connectionUsers.get(user);
		
		if(create && kolaerUser == null) {
			final DbKolaerUser userData = new DbKolaerUser(user);
			//lock.lock();
			connectionUsers.put(user, userData);
			//lock.unlock();
			return userData;
		}
		
		return kolaerUser;
	}


	/**
	 * @param user
	 */
	public void disconnectUser(final String user) {
		final DbKolaerUser kolaerUser = connectionUsers.get(user);
		kolaerUser.disconect();
		this.connectionUsers.remove(user, kolaerUser);
	}
	
	public void disconnectAllUser() {
		this.connectionUsers.values().parallelStream().forEach(user -> user.disconect());
		this.connectionUsers.clear();
	}
}
