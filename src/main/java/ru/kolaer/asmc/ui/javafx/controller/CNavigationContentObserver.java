package ru.kolaer.asmc.ui.javafx.controller;

import java.util.stream.Collectors;

import javafx.scene.layout.Pane;
import ru.kolaer.asmc.tools.Application;
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
		SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().add(group);
		SettingSingleton.getInstance().saveGroups();
		
		final CGroupLabels cGroup = new CGroupLabels(group);
		cGroup.registerOberver(this);
		this.panelWithGroups.getChildren().add(cGroup);
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().sorted((a, b) -> Integer.compare(((CGroupLabels) a).getModel().getPriority(), ((CGroupLabels) b).getModel().getPriority())));
	}

	public void addLabel(MLabel label) {
		if(this.selectedGroup == null) return;
		this.selectedGroup.addLabel(label);
		SettingSingleton.getInstance().saveGroups();
		
		final CLabel cLabel = new CLabel(label);
		cLabel.registerOberver(this);
		this.panelWithLabels.getChildren().add(cLabel);
		this.panelWithLabels.getChildren().setAll(this.panelWithLabels.getChildren().sorted((a, b) -> Integer.compare(((CLabel) a).getModel().getPriority(), ((CLabel) b).getModel().getPriority())));
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

		SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups()
		.stream()
		.sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
		.forEach((group) -> {
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

		mGroup.getLabelList().stream()
		.sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
		.forEach((g) -> {
			final CLabel label = new CLabel(g);
			this.panelWithLabels.getChildren().add(label);
			label.registerOberver(this);
		});
	}

	@Override
	public void updateEdit(MGroupLabels group) {
		SettingSingleton.getInstance().saveGroups();
		
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().stream().map(g -> {
			return((CGroupLabels) g);})
				.sorted((a, b) -> Integer.compare(a.getModel().getPriority(), b.getModel().getPriority()))
				.collect(Collectors.toList()));
	}

	@Override
	public void updateDelete(MGroupLabels model) {
		SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().remove(model);
		SettingSingleton.getInstance().saveGroups();
		
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
	}

	@Override
	public void updateClick(MLabel model) {
		new Application(model.getPathApplication()).start();
	}
	
	@Override
	public void updateEdit(MLabel model) {
		SettingSingleton.getInstance().saveGroups();
		this.panelWithLabels.getChildren().setAll(this.panelWithLabels.getChildren().stream().map(l -> {
			return((CLabel) l); })
				.sorted((a, b) -> Integer.compare(a.getModel().getPriority(), b.getModel().getPriority()))
				.collect(Collectors.toList()));
	}

	@Override
	public void updateDelete(MLabel model) {
		if(this.selectedGroup == null) return;
		
		this.selectedGroup.getLabelList().remove(model);
		
		SettingSingleton.getInstance().saveGroups();
		
		final CLabel[] array = new CLabel[1];
		this.panelWithLabels.getChildren().forEach(l -> {
			final CLabel label = ((CLabel) l);
			if(label.getModel() == model) {
				array[0] = label;
				return;
			}
		});

		this.panelWithLabels.getChildren().remove(array[0]);		
	}
}