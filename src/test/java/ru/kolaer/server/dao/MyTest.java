package ru.kolaer.server.dao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(TestDataConfig.class);
		
		MyTableService ser = context.getBean("myTableService",MyTableService.class);
		
		for(MyTable tab : ser.getAll()) {
			System.out.println(tab.getName());
		}
		
		System.out.println(ser.getTable("TestNameTwo").getId());
	}

}
