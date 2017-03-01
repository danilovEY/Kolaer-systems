package ru.kolaer.asmc.mvp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MGroup implements Serializable {

	private static final long serialVersionUID = -178505769987411468L;
	
	private String nameGroup = "name group";
	private int priority = 0;

	private List<MLabel> labelList = new ArrayList<>();
	private List<MGroup> groups = new ArrayList<>();

	public MGroup(final String text, final int priority) {
		this.nameGroup = text;
		this.priority = priority;
	}

	public MGroup(MGroup mGroup) {
		this.nameGroup =  mGroup.getNameGroup()  + " (Копия)";
		this.priority = mGroup.getPriority();

		Optional.ofNullable(mGroup.getLabelList())
				.ifPresent(labels -> this.labelList = labels.stream().map(MLabel::new).collect(Collectors.toList()));

		Optional.ofNullable(mGroup.getGroups())
				.ifPresent(groups -> this.groups = groups.stream().map(MGroup::new).collect(Collectors.toList()));
	}

    public void addLabel(final MLabel label){
		this.labelList.add(label);
	}

}
