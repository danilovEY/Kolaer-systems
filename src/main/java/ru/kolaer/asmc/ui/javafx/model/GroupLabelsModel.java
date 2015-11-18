package ru.kolaer.asmc.ui.javafx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupLabelsModel implements Serializable {

	private static final long serialVersionUID = -178505769987411468L;
	
	private String nameGroup;
	private final List<LabelModel> labelList = new ArrayList<>();
	
	public void addLabel(LabelModel label){
		this.labelList.add(label);
	}
	
	public String getNameGroup() {
		return nameGroup;
	}
	public void setNameGroup(String nameGroup) {
		this.nameGroup = nameGroup;
	}
	public List<LabelModel> getLabelList() {
		return labelList;
	}
	public void setLabelList(List<LabelModel> labelList) {
		this.labelList.clear();
		this.labelList.addAll(labelList);
	}
	
}
