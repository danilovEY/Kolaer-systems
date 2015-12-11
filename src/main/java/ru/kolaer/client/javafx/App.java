package ru.kolaer.client.javafx;

import org.springframework.web.client.RestTemplate;

public class App {

	public static void main(String args[]) {
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://localhost:8080/kolaer/api/";

		String beans = restTemplate.getForObject(url, String.class);
		System.out.println("The object received from REST call : " + beans);

	}

}
