package ru.kolaer.server.restful.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.restful.tools.Resources;

@RestController
@RequestMapping(value=Resources.ABSOLUTE_APPLICATIONS)
public class Applications {
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String[] getApplications() {
		return new String[]{};
	}
	
}
