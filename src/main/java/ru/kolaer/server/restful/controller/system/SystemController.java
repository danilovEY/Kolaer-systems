package ru.kolaer.server.restful.controller.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	
	@RequestMapping(path = "/windows", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Set<String> > getWindows() {
		final Map<String, Set<String>> map = new HashMap<>();
		
		connectionUsers.values().parallelStream().forEach(user -> {map.put(user.getName(), user.getOpeningWindows());});
		
		return map;
	}
	
}
