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
	
	/**Загрузить и добавить группы.*/
	public void loadAndRegGroups(SerializationGroups data) {
		this.panelWithGroups.getChildren().forEach(g -> {
			((CGroupLabels)g).removeObserver(this);
		});
		this.panelWithGroups.getChildren().clear();
		
		data.getSerializeGroups().forEach((group) ->{
    		final CGroupLabels cGroup = new CGroupLabels(group);
    		cGroup.registerOberver(this);
    		this.panelWithGroups.getChildren().add(cGroup);
    	});
	}
	
	@Override
	public void updateClick(MGroupLabels mGroup) {
		this.panelWithLabels.getChildren().forEach(g -> {
			((CLabel)g).removeObserver(this);
		});
		this.panelWithLabels.getChildren().clear();
		
		mGroup.getLabelList().forEach((g) -> {
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
