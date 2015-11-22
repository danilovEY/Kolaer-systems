package ru.kolaer.asmc.ui.javafx.controller;

import java.util.stream.Collectors;

import javafx.scene.layout.Pane;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 * Контроллер-слушатель действий групп и ярлыков.
 * 
 * @author Danilov
 * @version 0.1
 */
public class CNavigationContentObserver implements ObserverGroupLabels, ObserverLabel {

	private final Pane panelWithGroups;
	private final Pane panelWithLabels;
	private MGroupLabels selectedGroup;

	/**
	 * {@linkplain CNavigationContentObserver}
	 */
	public CNavigationContentObserver(Pane panelWithGroups, Pane panelWithLabels) {
		this.panelWithGroups = panelWithGroups;
		this.panelWithLabels = panelWithLabels;
	}

	public void addGroupLabels(MGroupLabels group) {
		final CGroupLabels cGroup = new CGroupLabels(group);
		cGroup.registerOberver(this);
		this.panelWithGroups.getChildren().add(cGroup);
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(((CGroupLabels) a).getModel().getNameGroup(), ((CGroupLabels) b).getModel().getNameGroup())));
	}

	public void addLabel(MLabel label) {
		if(this.selectedGroup == null) return;
		this.selectedGroup.addLabel(label);
		final CLabel cLabel = new CLabel(label);
		cLabel.registerOberver(this);
		this.panelWithLabels.getChildren().add(cLabel);
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(((CLabel) a).getModel().getName(), ((CLabel) b).getModel().getName())));
	}

	public MGroupLabels getSelectedItem() {
		return this.selectedGroup;
	}

	/** Загрузить и добавить группы. */
	public void loadAndRegGroups() {
		this.selectedGroup = null;

		this.panelWithGroups.getChildren().forEach(g -> {
			((CGroupLabels) g).removeObserver(this);
		});
		this.panelWithGroups.getChildren().clear();

		this.panelWithLabels.getChildren().forEach(g -> {
			((CLabel) g).removeObserver(this);
		});
		this.panelWithLabels.getChildren().clear();

		SettingSingleton.getInstance().getSerializationGroups().getSerializeGroups().stream().sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getNameGroup(), b.getNameGroup())).forEach((group) -> {
			final CGroupLabels cGroup = new CGroupLabels(group);
			cGroup.registerOberver(this);
			this.panelWithGroups.getChildren().add(cGroup);
		});
	}

	@Override
	public void updateClick(MGroupLabels mGroup) {
		selectedGroup = null;
		selectedGroup = mGroup;

		this.panelWithLabels.getChildren().forEach(g -> {
			final CLabel label = (CLabel) g;
			label.removeObserver(this);
		});
		this.panelWithLabels.getChildren().clear();

		mGroup.getLabelList().stream().sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName())).forEach((g) -> {
			final CLabel label = new CLabel(g);
			this.panelWithLabels.getChildren().add(label);
			label.registerOberver(this);
		});
	}

	@Override
	public void update(MLabel model) {
		System.out.println(model.getName());
	}

	@Override
	public void updateEdit(MGroupLabels group) {
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().stream().map(g -> {
			return((CGroupLabels) g);
		}).sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getModel().getNameGroup(), b.getModel().getNameGroup())).collect(Collectors.toList()));
	}

	@Override
	public void updateDelete(MGroupLabels model) {
		if(model == this.selectedGroup){
			this.selectedGroup = null;

			this.panelWithLabels.getChildren().forEach(g -> {
				final CLabel label = ((CLabel) g);
				label.removeObserver(this);
			});
			this.panelWithLabels.getChildren().clear();
		}
		final CGroupLabels[] array = new CGroupLabels[1];
		model.getLabelList().clear();
		this.panelWithGroups.getChildren().forEach(g -> {
			final CGroupLabels group = ((CGroupLabels) g);
			if(group.getModel() == model){
				array[0] = group;
				return;
			}
		});
		this.panelWithGroups.getChildren().remove(array[0]);
		SettingSingleton.getInstance().getSerializationGroups().getSerializeGroups().remove(model);
		SettingSingleton.getInstance().saveGroups();
	}
}
