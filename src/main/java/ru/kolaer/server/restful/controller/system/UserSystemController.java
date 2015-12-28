package ru.kolaer.server.restful.controller.system;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.DbKolaerUser;

@RestController
@RequestMapping(value = "/system/user/{user}")
public class UserSystemController {
	@Resource
	@Qualifier("map")
	private Map<String, DbKolaerUser> connectionUsers;

	private DbKolaerUser getOrCreate(final String user) {
		if (connectionUsers.containsKey(user)) {
			return this.connectionUsers.get(user);
		} else {
			final DbKolaerUser userData = new DbKolaerUser(user);
			connectionUsers.put(user, userData);
			return userData;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DbKolaerUser getUser(final @PathVariable String user) {
		return this.getOrCreate(user);
	}

	@RequestMapping(path = "/exit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void exitUser(final @PathVariable String user) {
		if (connectionUsers.containsKey(user)) {
			final DbKolaerUser userData = this.connectionUsers.get(user);
			userData.disconect();
			this.connectionUsers.remove(user);
		}
	}

	@RequestMapping(path = "/ip/{ip}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean addUserIp(final @PathVariable String user, final @PathVariable String ip) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addIp(ip);

		return true;
	}

	@RequestMapping(path = "/key/{key}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addKey(final @PathVariable String user, final @PathVariable String key) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addKey(key);
	}

	@RequestMapping(path = "/window/{window}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addWindow(final @PathVariable String user, final @PathVariable String window,
			@RequestBody String status) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		if (status.equals("true"))
			userData.addOpeningWindow(window);
		else
			userData.removeOpeningWindow(window);
	}
}
