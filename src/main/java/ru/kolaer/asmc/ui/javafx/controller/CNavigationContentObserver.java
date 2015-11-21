package ru.kolaer.asmc.ui.javafx.controller;

import javafx.scene.layout.Pane;
import ru.kolaer.asmc.tools.serializations.SerializationGroups;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 * Контроллер-слушатель действий групп и ярлыков.
 * @author Danilov
 * @version 0.1
 */
public class CNavigationContentObserver implements ObserverGroupLabels, ObserverLabel{

	private final Pane panelWithGroups;
	private final Pane panelWithLabels;
	private MGroupLabels selectedGroup;
	
	/**
	 * {@linkplain CNavigationContentObserver}
	 */
	public CNavigationContentObserver( Pane panelWithGroups, Pane panelWithLabels) {
		this.panelWithGroups = panelWithGroups;
		this.panelWithLabels = panelWithLabels;
	}
	
	public void addGroupLabels(MGroupLabels group) {
		final CGroupLabels cGroup = new CGroupLabels(group);
		cGroup.registerOberver(this);
		this.panelWithGroups.getChildren().add(cGroup);
	}
	
	public void addLabel(MLabel label) {
		this.selectedGroup.addLabel(label);
		final CLabel cLabel = new CLabel(label);
		cLabel.registerOberver(this);
		this.panelWithLabels.getChildren().add(cLabel);
	}
	
	public MGroupLabels getSelectedItem() {
		return this.selectedGroup;
	}
	
	/**Загрузить и добавить группы.*/
	public void loadAndRegGroups(SerializationGroups data) {
		this.selectedGroup = null;
		
		this.panelWithGroups.getChildren().forEach(g -> {
			((CGroupLabels)g).removeObserver(this);
		});
		this.panelWithGroups.getChildren().clear();
		
		this.panelWithLabels.getChildren().forEach(g -> {
			((CLabel)g).removeObserver(this);
		});
		this.panelWithLabels.getChildren().clear();
		
		data.getSerializeGroups()
		.stream()
		.sorted((a,b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getNameGroup(), b.getNameGroup()))
		.forEach((group) ->{
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
			((CLabel)g).removeObserver(this);
		});
		this.panelWithLabels.getChildren().clear();
		
		mGroup.getLabelList()
		.stream()
		.sorted((a,b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName()))
		.forEach((g) -> {
			final CLabel label = new CLabel(g);
			this.panelWithLabels.getChildren().add(label);
			label.registerOberver(this);
		});
	}

	@Override
	public void update(MLabel model) {
		System.out.println(model.getName());
	}
}
