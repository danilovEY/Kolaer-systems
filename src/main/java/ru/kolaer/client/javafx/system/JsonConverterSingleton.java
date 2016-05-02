package ru.kolaer.client.javafx.system;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonConverterSingleton {
	private static final Logger LOG = LoggerFactory.getLogger(JsonConverterSingleton.class);

	private static JsonConverterSingleton instance;
	
	public static JsonConverterSingleton getInstance() {
		if(instance == null) {
			instance = new JsonConverterSingleton();
		}
		return instance;
	}
	
	private JsonFactory factory;
	
	public JsonConverterSingleton() {
		this.factory = new JsonFactory();
	}
	
	public <T> List<T> getEntities(final WebResource webResource, Class<T> cls) {
		final String json = webResource.get(String.class);
		
		try(final JsonParser jp = factory.createParser(json)){
			final ObjectMapper om = new ObjectMapper();
	        jp.nextToken();

	        while (jp.nextToken() != JsonToken.END_ARRAY) {
	        	final MappingIterator<T> iter = om.readValues(jp, cls);
	        	return iter.readAll();
	        }
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}
	
	public <T> T getEntity(final WebResource webResource, Class<T> cls) {
		final String json = webResource.get(String.class);
		
		try(final JsonParser jp = factory.createParser(json)){
			final ObjectMapper om = new ObjectMapper();
	        jp.nextToken();
	        
	        while (jp.nextToken() == JsonToken.START_OBJECT) {
	        	final T obs = om.readValue(jp, cls);
	        	return obs;
	        }
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}

}