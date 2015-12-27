package ru.kolaer.server.restful.controller.system;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.DbKolaerUser;

@RestController
@RequestMapping(value="/system/user/{user}")
public class UserSystemController {
	@Resource
	@Qualifier("map")
	private Map<String, DbKolaerUser> connectionUsers;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DbKolaerUser getUser(@PathVariable String user) {
		if(connectionUsers.containsKey(user)) {
			return this.connectionUsers.get(user);
		} else {
			final DbKolaerUser userData = new DbKolaerUser(user);
			connectionUsers.put(user, userData);
			return userData;
		}
	}
	
	@RequestMapping(path = "/exit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void exitUser(@PathVariable String user) {
		if(connectionUsers.containsKey(user)) {
			final DbKolaerUser userData = this.connectionUsers.get(user);
			userData.disconect();
			this.connectionUsers.remove(user);
		}
	}
	
	@RequestMapping(path = "/ip/{ip}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean addUserIp(@PathVariable String user, @PathVariable String ip) {
		if(connectionUsers.containsKey(user)) {
			final DbKolaerUser userData = this.connectionUsers.get(user);
			userData.addIp(ip);
		} else {
			final DbKolaerUser userData = new DbKolaerUser(user);
			userData.addIp(ip);
			connectionUsers.put(user, userData);
		}
		return true;
	}
	
	@RequestMapping(path = "/key/{key}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addKey(@PathVariable String user, @PathVariable String key) {
		if(connectionUsers.containsKey(user)) {
			final DbKolaerUser userData = this.connectionUsers.get(user);
			userData.addKey(key);
		} else {
			final DbKolaerUser userData = new DbKolaerUser(user);
			connectionUsers.put(user, userData);
			userData.addKey(key);
		}
	}
	
	@RequestMapping(path = "/window/{window}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addKey(@PathVariable String user, @PathVariable String window, @RequestParam(name = "status") String status) {
		if(connectionUsers.containsKey(user)) {
			final DbKolaerUser userData = this.connectionUsers.get(user);
			
			if(status.equals("true"))
				userData.addOpeningWindow(window);
		} else {
			final DbKolaerUser userData = new DbKolaerUser(user);
			connectionUsers.put(user, userData);
			
			if(status.equals("true"))
				userData.addOpeningWindow(window);
		}
	}
	
}
