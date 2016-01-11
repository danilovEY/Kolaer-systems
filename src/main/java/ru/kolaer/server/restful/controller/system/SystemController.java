package ru.kolaer.server.restful.controller.system;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.DbKolaerUser;
import ru.kolaer.server.restful.tools.UsersManager;

@RestController
@RequestMapping(value="/system")
public class SystemController {

	@Autowired
	private UsersManager usersManager;
	
	@RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<DbKolaerUser> getUsers() {
		return usersManager.getUsers();
	}
	
	@RequestMapping(path = "/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void ping(@RequestParam String ping) {
		if(ping.equals("start")) {
			this.usersManager.startPing();
		} else {
			this.usersManager.stopPing();
		}
	}
	
	@RequestMapping(path = "/users/exit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void exitUsers() {
		this.usersManager.disconnectAllUser();
	}
	
}
