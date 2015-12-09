package ru.kolaer.server.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class Test {

	@Autowired
	private DriverManagerDataSource dataSource;

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("/beans.xml");

		Test cust = (Test) context.getBean("test");
		
		
		JdbcTemplate select = new JdbcTemplate(cust.getDataSource());

		select.query("select * from MY_TABLE", new RowMapper<Object>() {
			public Object mapRow(ResultSet rs, int line) throws SQLException {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					System.out.print(rsmd.getColumnName(i) + "\t\t");
				}				
				System.out.println();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					System.out.print(rs.getObject(i) + "\t\t");
				}
				System.out.println();
				return null;
			}
		});

		try {
			cust.getDataSource().getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}

}
