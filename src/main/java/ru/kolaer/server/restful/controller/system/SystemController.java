package ru.kolaer.server.restful.controller.system;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.DbKolaerUser;

@RestController
@RequestMapping(value="/system")
public class SystemController {
	
	@Resource
	@Qualifier("map")
	private Map<String, DbKolaerUser> connectionUsers;

	@RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<DbKolaerUser> getUsers() {
		return connectionUsers.values();
	}
	
}
