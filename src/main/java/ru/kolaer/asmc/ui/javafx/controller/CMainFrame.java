package ru.kolaer.asmc.ui.javafx.controller;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;
import ru.kolaer.asmc.ui.javafx.view.ImageViewPane;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Контроллер главного окна приложения.
 *
 * @author Danilov
 * @version 0.2
 */
public class CMainFrame implements UniformSystemPlugin {
	private final Logger LOG = LoggerFactory.getLogger(CMainFrame.class);

	/** Панель с ярлыками. */
	private BorderPane mainPanel;
	private BorderPane contentPanel;
	
	private CNavigationContentObserver observer;
	private UniformSystemEditorKit uniformSystemEditorKit;

	public void initialize() {
		this.contentPanel = new BorderPane();

		final ExecutorService threadForBanner = Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(() -> {
			this.updateBanner();
			threadForBanner.shutdown();
		}, threadForBanner);

		final FlowPane labelsPanel = new FlowPane();
		final VBox groupsPanel = new VBox();

		Tools.runOnThreadFX(() -> {
			labelsPanel.setStyle("-fx-background-image: url('" + Resources.BACKGROUND_IMAGE.toString() + "');");
		});

		final ScrollPane groupsScrollPanel = new ScrollPane();
		final ScrollPane labelsScrollPanel = new ScrollPane();

		Tools.runOnThreadFX(() -> {
			groupsScrollPanel.setContent(groupsPanel);
			groupsScrollPanel.setFitToHeight(true);
			groupsScrollPanel.setFitToWidth(true);

			labelsScrollPanel.setContent(labelsPanel);
			labelsScrollPanel.setFitToHeight(true);
			labelsScrollPanel.setFitToWidth(true);

			final SplitPane splitPane = new SplitPane(groupsScrollPanel, labelsScrollPanel);

			this.contentPanel.setCenter(splitPane);
		});

		this.observer = new CNavigationContentObserver(groupsPanel, labelsPanel, uniformSystemEditorKit);
		this.updateData();

		final MenuItem addGroupLabels = new MenuItem(Resources.MENU_ITEM_ADD_GROUP);
		final MenuItem addLabel = new MenuItem(Resources.MENU_ITEM_ADD_LABEL);
		final MenuItem settingMenuItem = new MenuItem("Настройки");
		final MenuItem getRootMenuItem = new MenuItem("Админ");

		final Menu fileMenu = new Menu("Файл");

		Tools.runOnThreadFX(() -> {
			fileMenu.getItems().addAll(getRootMenuItem, settingMenuItem);
			groupsScrollPanel.setContextMenu(new ContextMenu(addGroupLabels));
			labelsScrollPanel.setContextMenu(new ContextMenu(addLabel));

			this.mainPanel.setTop(new MenuBar(fileMenu));
			this.mainPanel.setCenter(this.contentPanel);
		});

		// =====Events======
		getRootMenuItem.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				new CAuthenticationDialog().showAndWait().get();
			});
		});

		Tools.runOnThreadFX(() -> {
			settingMenuItem.getParentMenu().setOnShowing(e -> {
				Tools.runOnThreadFX(() -> {
					if (SettingSingleton.getInstance().isRoot()) {
						settingMenuItem.setDisable(false);
					} else {
						settingMenuItem.setDisable(true);
					}
				});
			});
		});
		settingMenuItem.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				final CSetting setting = new CSetting();
				setting.showAndWait();
				this.updateBanner();
			});
		});

		groupsScrollPanel.setOnContextMenuRequested((event) -> {
			if(!SettingSingleton.getInstance().isRoot()) {
				groupsScrollPanel.getContextMenu().hide();
			}
		});

		labelsScrollPanel.setOnContextMenuRequested((event) -> {
			if(observer.getSelectedItem() == null
					|| !SettingSingleton.getInstance().isRoot()) {
				labelsScrollPanel.getContextMenu().hide();
			}
		});

		addGroupLabels.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				final CAddingGroupLabelsDialog addingGroup = new CAddingGroupLabelsDialog();
				final Optional<MGroupLabels> result = addingGroup.showAndWait();
				if (result.isPresent()) {
					final MGroupLabels res = result.get();
					observer.addGroupLabels(res);
				}
			});
		});

		addLabel.setOnAction(e -> {
			Tools.runOnThreadFX(() -> {
				final CAddingLabelDialog addingLabel = new CAddingLabelDialog();
				final Optional<MLabel> result = addingLabel.showAndWait();
				if (result.isPresent()) {
					observer.addLabel(result.get());
				}
			});
		});
	}
	
	private void updateBanner() {
		final File imgCenter = new File(SettingSingleton.getInstance().getPathBanner());
		final File imgLeft = new File(SettingSingleton.getInstance().getPathBannerLeft());
		final File imgRight = new File(SettingSingleton.getInstance().getPathBannerRigth());

		final BorderPane imagePane = new BorderPane();

		Tools.runOnThreadFX(() -> {
			imagePane.setStyle("-fx-background-color: #FFFFFF"); //,linear-gradient(#f8f8f8, #e7e7e7);
			imagePane.setMaxHeight(300);
			imagePane.setMaxWidth(Double.MAX_VALUE);
			
			this.contentPanel.setTop(imagePane);
			
			if(imgLeft.exists() && imgLeft.isFile()) {
				ImageView left = new ImageView(new Image("file:" + imgLeft.getAbsolutePath(), true));
				left.setPreserveRatio(false);

				imagePane.setLeft(left);
			} 
			
			if(imgRight.exists() && imgRight.isFile()) {
				ImageView right = new ImageView(new Image("file:" + imgRight.getAbsolutePath(), true));
				right.setPreserveRatio(false);
				
				imagePane.setRight(right);
			}
			
			if(imgCenter.exists() && imgCenter.isFile()) {
				ImageViewPane center = new ImageViewPane(new ImageView(new Image("file:" + imgCenter.getAbsolutePath(), true)));
				imagePane.setCenter(center);
			}
		});
	}

	public void updateData() {
		final ExecutorService threadForLoadGroup = Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName("Загрузка и добавление групп");
			observer.loadAndRegGroups();
			threadForLoadGroup.shutdown();
		}, threadForLoadGroup).exceptionally(t -> {
			LOG.error("Ошибка при обновлении объектов!", t);
			return null;
		});
	}

	@Override
	public void initialization(UniformSystemEditorKit uniformSystemEditorKit) throws Exception {
		this.uniformSystemEditorKit = uniformSystemEditorKit;
		SettingSingleton.initialization();
		this.mainPanel = new BorderPane();
	}

	@Override
	public URL getIcon() {
		return null;
	}

	@Override
	public Collection<Service> getServices() {
		return null;
	}

	@Override
	public void start() throws Exception {
		final URL inputStreamUrl = this.getClass().getResource("/CSS/Default/Default.css");
		if(inputStreamUrl != null)
			this.mainPanel.getStylesheets().addAll(inputStreamUrl.toExternalForm());

		this.initialize();
	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void updatePluginObjects(String s, Object o) {

	}

	@Override
	public void setContent(Parent parent) {

	}

	@Override
	public Parent getContent() {
		return this.mainPanel;
	}
}