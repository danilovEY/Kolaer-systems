package ru.kolaer.asmc.ui.javafx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MGroupLabels implements Serializable {

	private static final long serialVersionUID = -178505769987411468L;
	
	private String nameGroup = "name group";
	private int priority = 0;
	private final List<MLabel> labelList = new ArrayList<>();
	
	public MGroupLabels() {
		
	}
	
	public MGroupLabels(final String text, final int prioriry) {
		this.nameGroup = text;
		this.priority = prioriry;
	}

	public void addLabel(final MLabel label){
		this.labelList.add(label);
	}
	
	public String getNameGroup() {
		return nameGroup;
	}
	public void setNameGroup(final String nameGroup) {
		this.nameGroup = nameGroup;
	}
	public List<MLabel> getLabelList() {
		return labelList;
	}
	public void setLabelList(List<MLabel> labelList) {
		this.labelList.clear();
		this.labelList.addAll(labelList);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
