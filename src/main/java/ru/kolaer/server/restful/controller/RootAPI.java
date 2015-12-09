package ru.kolaer.server.restful.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import ru.kolaer.server.restful.tools.Resources;

@RestController
@RequestMapping(value=Resources.ABSOLUTE_ROOT_API)
public class RootAPI {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public View getVersion(@RequestParam(name="version", defaultValue=Resources.VERSION) String version) {
		System.out.println("Version=" + version);
		return new RedirectView(Resources.ROOT_API+"/"+version+Resources.APPLICATIONS, true);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String[] getListVersions() {
		return new String[]{Resources.VERSION_0_0_1};
	}
	
}
