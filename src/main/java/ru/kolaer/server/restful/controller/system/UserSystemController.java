package ru.kolaer.server.restful.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.DbKolaerUser;
import ru.kolaer.server.restful.tools.UsersManager;

@RestController
@RequestMapping(value = "/system/user/{user}")
public class UserSystemController {
	
	@Autowired
	private UsersManager usersManager;

	private DbKolaerUser getOrCreate(final String user) {
		return this.usersManager.getUserByName(user, true);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DbKolaerUser getUser(final @PathVariable String user) {
		return this.usersManager.getUserByName(user, false);
	}

	@RequestMapping(path = "/exit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void exitUser(final @PathVariable String user) {
		this.usersManager.disconnectUser(user);
	}

	@RequestMapping(path = "/ip", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean addUserIp(final @PathVariable String user, final @RequestBody String ip) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addIp(ip);

		return true;
	}

	@RequestMapping(path = "/key", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addKey(final @PathVariable String user, final @RequestBody String key) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addKey(key);
	}

	@RequestMapping(path = "/window", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addWindow(final @PathVariable String user, final @RequestBody String window,
			@RequestBody String status) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		if (status.equals("true"))
			userData.addOpeningWindow(window);
		else
			userData.removeOpeningWindow(window);
	}
}
