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
	
	@RequestMapping(path = "/screen", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean addUserScreen(final @PathVariable String user, final @RequestBody byte[] image) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addImage(image);
		return true;
	}
	
	@RequestMapping(path = "/hostname", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean addUserHostName(final @PathVariable String user, final @RequestBody String hostName) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addHostName(hostName);
		return true;
	}

	@RequestMapping(path = "/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean ping(final @PathVariable String user) {
		return Boolean.valueOf(this.getOrCreate(user).isPing());
	}
	
	@RequestMapping(path = "/ping", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void ping(final @PathVariable String user, final @RequestBody String ping) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.setPing(true);
	}
	
	@RequestMapping(path = "/key", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addKey(final @PathVariable String user, final @RequestBody String key) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		userData.addKey(key);
	}
	
	@RequestMapping(path = "/app/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String[] getStatusApplication(final @PathVariable String user, @PathVariable final String status) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		if(status.equals("close")) {
			final String[] closes = userData.getCloseApplications().toArray(new String[userData.getCloseApplications().size()]);
			userData.getCloseApplications().clear();
			return closes;
		} else {
			final String[] open = userData.getOpeningWindows().toArray(new String[userData.getOpeningWindows().size()]);
			userData.getCloseApplications().clear();
			return open;
		}
	}
	
	@RequestMapping(path = "/app/{app}/{status}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addApplication(final @PathVariable String user, final @PathVariable String app,
			@PathVariable String status) {
		final DbKolaerUser userData =  this.getOrCreate(user);
		if (status.equals("open")){
			userData.addOpeningApplication(app);
		} else if (status.equals("close")) {
			userData.removeOpeningApplication(app);
		}
	}
}
