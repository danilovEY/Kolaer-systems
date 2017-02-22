package ru.kolaer.asmc.mvp.presenter;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.mvp.view.VLabelCss;
import ru.kolaer.asmc.tools.Application;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Контроллер-слушатель действий групп и ярлыков.
 * 
 * @author Danilov
 * @version 0.1
 */
public class CNavigationContentObserver implements ObserverGroupLabels, ObserverLabel {
	private final Logger LOG = LoggerFactory.getLogger(CNavigationContentObserver.class);
	
	private final Pane panelWithGroups;
	private final Pane panelWithLabels;
	private MGroup selectedGroup;
	private final UniformSystemEditorKit editorKit;

	private final Map<MGroup, CGroupLabels> groupsViewMap = new HashMap<>();
	private final Map<MLabel, VLabelCss> labelsViewMap = new HashMap<>();

	/**
	 * {@linkplain CNavigationContentObserver}
	 */
	public CNavigationContentObserver(final Pane panelWithGroups, final Pane panelWithLabels, final UniformSystemEditorKit editorKit) {
		this.panelWithGroups = panelWithGroups;
		this.panelWithLabels = panelWithLabels;
		this.editorKit = editorKit; 
	}

	public void addGroupLabels(final MGroup group) {
		SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().add(group);
		SettingSingleton.getInstance().saveGroups();

		Tools.runOnThreadFX(() -> {
			final CGroupLabels cGroup = new CGroupLabels(group);
			cGroup.registerObserver(this);
			this.panelWithGroups.getChildren().add(cGroup);
			final ExecutorService sort = Executors.newSingleThreadExecutor();
			sort.submit(() -> {
				this.sortGroup();
				sort.shutdown();
			});
		});
	}


	public MGroup getSelectedItem() {
		return this.selectedGroup;
	}



	private void sortIntInUserObject(final Pane pane) {
		final ObservableList<Node> nodes = pane.getChildren();
		Tools.runOnThreadFX(() -> {
			pane.getChildren().setAll(nodes.parallelStream().sorted((n1, n2) -> {
				final int pN1 = (int)Optional.ofNullable(n1.getUserData()).orElse(0);
				final int pN2 = (int)Optional.ofNullable(n2.getUserData()).orElse(0);
				return Integer.compare(pN1, pN2);
			}).collect(Collectors.toList()));
		});
	}

	private void sortGroup() {
		this.sortIntInUserObject(this.panelWithGroups);
	}

	private void sortLabel() {
		this.sortIntInUserObject(this.panelWithLabels);
	}

	@Override
	public void updateClick(final MGroup mGroup) {
		this.labelsViewMap.values().parallelStream().forEach(label -> {

		});

		this.labelsViewMap.clear();

		this.panelWithLabels.getChildren().clear();

		if(this.selectedGroup != null) {
			this.groupsViewMap.get(this.selectedGroup).notifyObserverUnClick();
		}

		this.selectedGroup = mGroup;

		final CountDownLatch countDownLatch = new CountDownLatch(mGroup.getLabelList().size());

		final ExecutorService sort = Executors.newSingleThreadExecutor();
		sort.submit(() -> {
			try {
				countDownLatch.await();
				this.sortLabel();
				sort.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public void updateEdit(final MGroup group) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			SettingSingleton.getInstance().saveGroups();
			thread.shutdown();
		});

		final ExecutorService sort = Executors.newSingleThreadExecutor();
		sort.submit(() -> {
			this.sortGroup();
			sort.shutdown();
		});
	}

	@Override
	public void updateDelete(final MGroup model) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().remove(model);
			SettingSingleton.getInstance().saveGroups();
			thread.shutdown();
		});

		if(model == this.selectedGroup){
			this.selectedGroup = null;

			Tools.runOnThreadFX(() -> {
				this.panelWithLabels.getChildren().clear();
			});


			this.labelsViewMap.clear();
		}

		Tools.runOnThreadFX(() -> {
			this.panelWithGroups.getChildren().remove(this.groupsViewMap.get(model));
		});
	}

	@Override
	public void updateClick(final MLabel model) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			final Application app = new Application(model.getPathApplication(), model.getPathOpenAppWith(), this.editorKit);
			app.start();
		});
		thread.shutdown();
	}

	@Override
	public void updateEdit(final MLabel model) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			SettingSingleton.getInstance().saveGroups();
			this.sortLabel();
		});
		thread.shutdown();
	}

	@Override
	public void updateDelete(final MLabel model) {
		if(this.selectedGroup == null)
			return;

		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			SettingSingleton.getInstance().saveGroups();
		});
		thread.shutdown();

		this.selectedGroup.getLabelList().remove(model);

	}
}