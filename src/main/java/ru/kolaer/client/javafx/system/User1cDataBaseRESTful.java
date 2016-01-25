package ru.kolaer.client.javafx.system;

import org.springframework.web.client.RestTemplate;

import ru.kolaer.server.dao.entities.DbUsers1c;

public class User1cDataBaseRESTful implements User1cDataBase {
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public DbUsers1c[] getAllUser() {
		final DbUsers1c[] users = restTemplate.getForObject("http://localhost:8080/kolaer/database/user1c/get/users/max/5", DbUsers1c[].class);
		return users;
	}
	
}
