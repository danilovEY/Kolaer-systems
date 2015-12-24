package ru.kolaer.server.restful.controller.system;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.restful.tools.Resources;

@RestController
@RequestMapping(value="/system/window")
public class WindowsSystemController {
	private String close = "false";
	
	@RequestMapping(path = "/close", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getApplications() {
		return close;
	}
	
	@RequestMapping(path = "/set", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getApp(@RequestParam(name="set") String set) {
		this.close = set;
		return close;
	}
}
