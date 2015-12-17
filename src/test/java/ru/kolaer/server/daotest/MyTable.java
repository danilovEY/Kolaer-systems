package ru.kolaer.server.daotest;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MY_TABLE database table.
 * 
 */
@Entity
@Table(name="MY_TABLE")
@NamedQuery(name="MyTable.findAll", query="SELECT m FROM MyTable m")
public class MyTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String name;

	public MyTable() {
	}

	public MyTable(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}