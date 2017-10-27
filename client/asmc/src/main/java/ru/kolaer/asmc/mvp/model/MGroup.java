package ru.kolaer.asmc.mvp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MGroup {

	private String nameGroup = "name group";
	private int priority = 0;
	private List<MLabel> labelList;
	private List<MGroup> groups;

	public MGroup(String text, int priority) {
		this.nameGroup = text;
		this.priority = priority;
		this.labelList = new ArrayList<>();
		this.groups = new ArrayList<>();
	}

	public MGroup(MGroup mGroup) {
	    nameGroup = mGroup.getNameGroup()  + " (Копия)";
	    priority = mGroup.getPriority();

		labelList = Optional.ofNullable(mGroup.getLabelList())
				.orElse(Collections.emptyList())
				.stream()
				.map(MLabel::new)
				.collect(Collectors.toList());

		groups = Optional.ofNullable(mGroup.getGroups())
				.orElse(Collections.emptyList())
				.stream()
				.map(MGroup::new)
				.collect(Collectors.toList());
	}

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MGroup mGroup = (MGroup) o;

		if (priority != mGroup.priority) return false;
		return nameGroup.equals(mGroup.nameGroup);
	}

	@Override
	public int hashCode() {
		return nameGroup.hashCode();
	}
}
