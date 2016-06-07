package ru.kolaer.asmc.ui.javafx.model;

import java.io.Serializable;

public class MLabel implements Serializable {
	private static final long serialVersionUID = -8342840478051620237L;
	
	private String name;
	private String info;
	private String pathImage;
	private String pathOpenAppWith;
	private String pathApplication;
	private int priority = 0;
	
	public MLabel(final String name, final String info, final String pathImage, final String pathApplication, final String pathOpenAppWith, final int priority) {
		this.name = name;
		this.info = info;
		this.pathImage = pathImage;
		this.pathApplication = pathApplication;
		this.pathOpenAppWith = pathOpenAppWith;
		this.priority = priority;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(final String info) {
		this.info = info;
	}
	public String getPathImage() {
		return pathImage;
	}
	public void setPathImage(final String pathImage) {
		this.pathImage = pathImage;
	}
	public String getPathApplication() {
		return pathApplication;
	}
	public void setPathApplication(final String pathApplication) {
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
	public String getPathOpenAppWith() {
		return pathOpenAppWith;
	}
	public void setPathOpenAppWith(final String pathOpenAppWith) {
		this.pathOpenAppWith = pathOpenAppWith;
	}
}
