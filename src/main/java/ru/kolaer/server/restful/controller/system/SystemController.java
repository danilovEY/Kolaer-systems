package ru.kolaer.server.restful.controller.system;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.DbKolaerUser;
import ru.kolaer.server.restful.tools.UsersManager;

@RestController
@RequestMapping(value="/system")
public class SystemController {
	private static String status = "available";
	@Autowired
	private UsersManager usersManager;
	
	@RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<DbKolaerUser> getUsers() {
		return usersManager.getUsers();
	}
	
	@RequestMapping(path = "/server/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getStatus() {
		return status;
	}
	
	@RequestMapping(path = "/server/status/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String setStatus(final @PathVariable String status) {
		SystemController.status = status;
		return status;
	}
	
	@RequestMapping(path = "/users/ping", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void ping(@RequestParam String ping) {
		if(ping.equals("start")) {
			this.usersManager.startPing();
		} else {
			this.usersManager.stopPing();
		}
	}
	
	@RequestMapping(path = "/users/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean ping() {
		return this.usersManager.isStartPing();
	}
	
	@RequestMapping(path = "/users/exit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void exitUsers() {
		this.usersManager.disconnectAllUser();
	}
	
	@RequestMapping(path = "/users/app/{window}/{status}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void closeWindow(@PathVariable final String window, @PathVariable final String status) {
		if(status.equals("close")) {
			this.usersManager.getUsers().parallelStream().forEach(kolaerUser -> kolaerUser.addCloseApplication(window));
		} else {
			
		}
	}
	
	@RequestMapping(path = "/users/{user}/app/{window}/{status}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void closeWindow(@PathVariable final String user, @PathVariable final String window, @PathVariable final String status) {
		if(status.equals("close")) {
			this.usersManager.getUsers().parallelStream().forEach(kolaerUser -> {
				if(kolaerUser.getName().equals(user)) {
					kolaerUser.addCloseApplication(window);
					return;
				}
			});
		} else {
			
		}
	}
	
}
