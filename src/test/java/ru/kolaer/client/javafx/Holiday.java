package ru.kolaer.client.javafx;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.kolaer.server.dao.entities.PublicHolidays;

public class Holiday {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		PublicHolidays[] user = mapper.readValue(new URL("http://kayaposoft.com/enrico/json/v1.0/?action=getPublicHolidaysForMonth&month=2&year=2016&country=rus"), PublicHolidays[].class);
		System.out.println(user[0].getDate().getDay());

	}

}
