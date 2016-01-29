package ru.kolaer.asmc.ui.javafx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

	private final Map<CGroupLabels, List<CLabel>> cache = new HashMap<>();
	private ExecutorService threadCache;
	
	/**
	 * {@linkplain CNavigationContentObserver}
	 */
	public CNavigationContentObserver(final Pane panelWithGroups, final Pane panelWithLabels) {
		this.panelWithGroups = panelWithGroups;
		this.panelWithLabels = panelWithLabels;
	}

	public void addGroupLabels(final MGroupLabels group) {
		SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().add(group);
		SettingSingleton.getInstance().saveGroups();

		final CGroupLabels cGroup = new CGroupLabels(group);
		cGroup.registerOberver(this);
		this.panelWithGroups.getChildren().add(cGroup);
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().sorted((a, b) -> Integer.compare(((CGroupLabels) a).getModel().getPriority(), ((CGroupLabels) b).getModel().getPriority())));
		this.addCache(cGroup, new ArrayList<>());
	}

	public void addLabel(MLabel label) {
		if(this.selectedGroup == null) return;
		this.selectedGroup.addLabel(label);
		SettingSingleton.getInstance().saveGroups();
		
		final CLabel cLabel = new CLabel(label);
		cLabel.registerOberver(this);
		this.panelWithLabels.getChildren().add(cLabel);
		this.panelWithLabels.getChildren().setAll(this.panelWithLabels.getChildren().sorted((a, b) -> Integer.compare(((CLabel) a).getModel().getPriority(), ((CLabel) b).getModel().getPriority())));
		this.cache.get(this.getCGroupLabelCache(this.selectedGroup)).add(cLabel);
	}

	public MGroupLabels getSelectedItem() {
		return this.selectedGroup;
	}

	/** Загрузить и добавить группы. */
	public void loadAndRegGroups() {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		
		this.selectedGroup = null;

		if(this.threadCache!=null) {
			this.threadCache.shutdownNow();
		}
		
		this.cache.keySet().forEach(group -> {
			this.cache.get(group).forEach(label -> label.removeObserver(this));
			group.removeObserver(this);
		});
		
		this.cache.clear();
		
		this.panelWithGroups.getChildren().clear();
		this.panelWithLabels.getChildren().clear();
		final List<Node> nodes = new ArrayList<>();
		this.threadCache = Executors.newCachedThreadPool();
		thread.submit(() -> {
			List<MGroupLabels> groupsList = SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups();
			groupsList.parallelStream()
			.forEach((group) -> {
				this.threadCache.submit(() -> {
						final CGroupLabels cGroup = new CGroupLabels(group);
						nodes.add(cGroup);
						final ExecutorService threads = Executors.newSingleThreadExecutor();
						threads.submit(() -> {
							final List<CLabel> labelList = cGroup.getModel().getLabelList().stream().map(label -> {
								return new CLabel(label);
							}).collect(Collectors.toList());
							this.addCache(cGroup, labelList);
						});
						threads.shutdown();
						cGroup.registerOberver(this);
						return cGroup;
				});	
			});
			
			this.threadCache.shutdown();
			
			Platform.runLater(() -> {
				try {
					this.threadCache.awaitTermination(2, TimeUnit.MINUTES);

					final List<Node> n = nodes.parallelStream().sorted((a, b) ->  Integer.compare(Integer.valueOf(Optional.ofNullable(a.getUserData().toString()).orElse("99")), Integer.valueOf(Optional.ofNullable(b.getUserData().toString()).orElse("99"))))
					.collect(Collectors.toList());
					Platform.runLater(() -> {
						this.panelWithGroups.getChildren().setAll(n);	
					});
					
				} catch (final Exception e) {
					e.printStackTrace();
				}
			});
		});
		thread.shutdown();
	}
	
	/**
	 * Получить группу из кэша.
	 * @param group
	 * @return
	 */
	private CGroupLabels getCGroupLabelCache(final MGroupLabels group) {
		for(CGroupLabels gr : this.cache.keySet()){
			if(gr.getModel() == group)
				return gr;
		}
		return null;
	}
	/**Добавить группу и ярлыки в кэш.*/
	private void addCache(final CGroupLabels group, final List<CLabel> labels) {
		this.cache.put(group, labels);
	}
	
	@Override
	public void updateClick(final MGroupLabels mGroup) {
		final CGroupLabels group = this.getCGroupLabelCache(this.selectedGroup);
		if(group!=null)
			this.cache.get(group).forEach(label -> {
				label.removeObserver(this);
			});
		
		this.panelWithLabels.getChildren().clear();
		
		this.selectedGroup = mGroup;
		
		final CGroupLabels newGroup = this.getCGroupLabelCache(mGroup);
		//Если в кэше нет группы с ярлыками, то загружаем вручную
		if(newGroup != null) {
			this.cache.get(newGroup).parallelStream()
			.sorted((a, b) -> Integer.compare(a.getModel().getPriority(), b.getModel().getPriority()))
			.forEach(label -> {
				Platform.runLater(() -> {
					this.panelWithLabels.getChildren().add(label);
					label.registerOberver(this);
				});
			});
		} else {
			mGroup.getLabelList().parallelStream()
			.sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
			.forEach((g) -> {
				Platform.runLater(() -> {
					final CLabel label = new CLabel(g);
					this.panelWithLabels.getChildren().add(label);
					label.registerOberver(this);
				});
			});
		}
	}

	@Override
	public void updateEdit(final MGroupLabels group) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(()->{
			SettingSingleton.getInstance().saveGroups();
		});
		thread.shutdown();
		
		this.panelWithGroups.getChildren().setAll(this.panelWithGroups.getChildren().stream().map(g -> {
			return((CGroupLabels) g);})
				.sorted((a, b) -> Integer.compare(a.getModel().getPriority(), b.getModel().getPriority()))
				.collect(Collectors.toList()));
	}

	@Override
	public void updateDelete(final MGroupLabels model) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(()->{
			SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().remove(model);
			SettingSingleton.getInstance().saveGroups();
		});
		thread.shutdown();
		
		final Alert alertDeleteMassage = new Alert(AlertType.NONE,"Удаление элемента...", ButtonType.OK);
		alertDeleteMassage.show();
		
		try {
			boolean load = this.threadCache.awaitTermination(15, TimeUnit.SECONDS);
			alertDeleteMassage.close();
			if(!load) {
				final Alert alertError = new Alert(AlertType.ERROR,"Превышено ошидание. Повтор загрузки...", ButtonType.OK);
				alertError.show();
				this.loadAndRegGroups();
				return;
			}
		} catch (InterruptedException e) {
			alertDeleteMassage.close();
			return;
		}
		
		alertDeleteMassage.close();
		
		if(this.threadCache.isTerminated()) {			
			final CGroupLabels del = this.getCGroupLabelCache(model);
			if(model == this.selectedGroup){
				this.selectedGroup = null;
				
				this.panelWithLabels.getChildren().clear();
			}	
			
			this.cache.get(del).forEach(label -> {
				label.removeObserver(this);
			});
			
			this.panelWithGroups.getChildren().remove(del);
			this.cache.remove(del);
		}
	}

	@Override
	public void updateClick(final MLabel model) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(()->{
			final Application app = new Application(model.getPathApplication(), model.getPathOpenAppWith());
			app.start();
		});		
		thread.shutdown();
	}
	
	@Override
	public void updateEdit(final MLabel model) {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(()->{
			SettingSingleton.getInstance().saveGroups();
		});
		thread.shutdown();
		
		this.panelWithLabels.getChildren().setAll(this.panelWithLabels.getChildren().stream().map(l -> {
			return((CLabel) l); })
				.sorted((a, b) -> Integer.compare(a.getModel().getPriority(), b.getModel().getPriority()))
				.collect(Collectors.toList()));
	}

	@Override
	public void updateDelete(final MLabel model) {
		
		if(this.selectedGroup == null) return;
		
		this.selectedGroup.getLabelList().remove(model);
		
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(()->{
			SettingSingleton.getInstance().saveGroups();
		});
		thread.shutdown();
		
		final Alert alertDeleteMassage = new Alert(AlertType.NONE,"Удаление элемента...", ButtonType.OK);
		alertDeleteMassage.show();
		
		try {
			boolean load = this.threadCache.awaitTermination(15, TimeUnit.SECONDS);
			alertDeleteMassage.close();
			if(!load) {
				final Alert alertError = new Alert(AlertType.ERROR,"Превышено ошидание. Повтор загрузки...", ButtonType.OK);
				alertError.show();
				this.loadAndRegGroups();
				return;
			}
		} catch (InterruptedException e) {
			alertDeleteMassage.close();
			return;
		}
		
		alertDeleteMassage.close();
		
		if(this.threadCache.isTerminated()) {
			
			final CGroupLabels del = this.getCGroupLabelCache(this.selectedGroup);
			
			for(final CLabel l : this.cache.get(del).stream().filter(label -> {
				return label.getModel() == model;
			}).collect(Collectors.toList())) {
				this.panelWithLabels.getChildren().remove(l);
				this.cache.get(del).remove(l);
			}
		}	
	}
}