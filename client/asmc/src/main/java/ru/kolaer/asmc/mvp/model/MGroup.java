package ru.kolaer.asmc.mvp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MGroup implements Serializable {

	@Getter @Setter
	private String nameGroup = "name group";

	@Getter @Setter
	private int priority = 0;

	@Getter
	private List<MLabel> labelList;

	@Getter
	private List<MGroup> groups;

	public MGroup(final String text, final int priority) {
		this.nameGroup = text;
		this.priority = priority;
		this.labelList = new ArrayList<>();
		this.groups = new ArrayList<>();
	}

	public MGroup(MGroup mGroup) {
	    this(mGroup.getNameGroup()  + " (Копия)", mGroup.getPriority());

        Optional.ofNullable(mGroup.getLabelList())
                .ifPresent(labels -> this.labelList.addAll(labels.stream().map(MLabel::new)
                        .collect(Collectors.toList())));

        Optional.ofNullable(mGroup.getGroups())
                .ifPresent(groups -> this.groups.addAll(groups.stream().map(MGroup::new)
                        .collect(Collectors.toList())));
	}

    public void setLabelList(List<MLabel> labelList) {
        this.labelList = Optional.ofNullable(labelList).orElse(new ArrayList<>());
    }

    public void setGroups(List<MGroup> groups) {
        this.groups = Optional.ofNullable(groups).orElse(new ArrayList<>());
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
		int result = nameGroup.hashCode();
		result = 31 * result + priority;
		return result;
	}
}
