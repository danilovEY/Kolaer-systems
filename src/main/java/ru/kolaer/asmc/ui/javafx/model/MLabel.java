package ru.kolaer.asmc.ui.javafx.model;

import java.io.Serializable;

public class MLabel implements Serializable {
	private static final long serialVersionUID = -8342840478051620237L;
	
	private String name;
	private String info;
	private String pathImage;
	private String pathApplication;
	private int priority = 0;
	
	public MLabel(String name, String info, String pathImage, String pathApplication, int priority) {
		this.name = name;
		this.info = info;
		this.pathImage = pathImage;
		this.pathApplication = pathApplication;
		this.priority = priority;
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
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
