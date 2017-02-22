package ru.kolaer.asmc.mvp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MLabel implements Serializable {
	private static final long serialVersionUID = -8342840478051620237L;
	
	private String name;
	private String info;
	private String pathImage;
	private String pathOpenAppWith;
	private String pathApplication;
	private int priority = 0;
}
