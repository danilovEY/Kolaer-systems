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
	
	/**Загрузить и добавить группы.*/
	public void loadAndRegGroups(SerializationGroups data) {
		data.getSerializeGroups().forEach((group) ->{
    		CGroupLabels cGroup = new CGroupLabels(group);
    		this.panelWithGroups.getChildren().add(cGroup);
    		cGroup.registerOberver(this);
    	});
	}
	
	@Override
	public void updateClick(MGroupLabels group) {
		this.panelWithLabels.getChildren().clear();
		group.getLabelList().forEach((l) -> {
			CLabel label = new CLabel(l);
			this.panelWithLabels.getChildren().add(label);
			label.registerOberver(this);
		});
	}

	@Override
	public void update(MLabel model) {
		System.out.println(model.getName());
	}
	
	

}
