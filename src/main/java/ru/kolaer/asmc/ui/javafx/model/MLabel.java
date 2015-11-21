package ru.kolaer.asmc.ui.javafx.model;

import java.io.Serializable;

public class MLabel implements Serializable {
	private static final long serialVersionUID = -8342840478051620237L;
	
	private String name;
	private String info;
	private String pathImage;
	private String pathApplication;
	
	public MLabel(String name, String info, String pathImage, String pathApplication) {
		this.name = name;
		this.info = info;
		this.pathImage = pathImage;
		this.pathApplication = pathApplication;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getPathImage() {
		return pathImage;
	}
	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}
	public String getPathApplication() {
		return pathApplication;
	}
	public void setPathApplication(String pathApplication) {
		this.pathApplication = pathApplication;
	}
	@Override
	public String toString() {
		return "MLabel [name=" + name + ", info=" + info + ", pathImage=" + pathImage + ", pathApplication=" + pathApplication + "]";
	}
}
