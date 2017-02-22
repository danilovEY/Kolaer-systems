package ru.kolaer.asmc.mvp.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MGroup implements Serializable {

	private static final long serialVersionUID = -178505769987411468L;
	
	private String nameGroup = "name group";
	private int priority = 0;

	private List<MLabel> labelList = new ArrayList<>();
	private List<MGroup> groups = new ArrayList<>();
	
	public MGroup() {
		
	}
	
	public MGroup(final String text, final int priority) {
		this.nameGroup = text;
		this.priority = priority;
	}

	public void addLabel(final MLabel label){
		this.labelList.add(label);
	}

}
