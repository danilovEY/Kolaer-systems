package ru.kolaer.admin.bid.mvp.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import ru.kolaer.admin.bid.mvp.model.MBid;
import ru.kolaer.api.tools.Tools;

public class VBidTabContent extends VTabContent {
	private final TableView<MBid> table;
	private final BorderPane mainPane;
	private final CheckBox initialsColumnVis;
	private final CheckBox dateStartColumnVis;
	private final CheckBox statusColumnVis;
	private final CheckBox executorColumnVis;
	private final CheckBox descriptionsColumnVis;
	private final CheckBox startDate;
	private final CheckBox endDate;
	private final DatePicker startDatePic;
	private final DatePicker endDatePic;
	private final Button exportToExcel;

	
	public VBidTabContent() {
		this.table = new TableView<MBid>();
		this.mainPane = new BorderPane();
		
		this.initialsColumnVis = new CheckBox("Ф.И.О.");
		this.initialsColumnVis.setSelected(true);

		this.dateStartColumnVis = new CheckBox("Дата подачи");
		this.dateStartColumnVis.setSelected(true);

		this.statusColumnVis = new CheckBox("Статус");
		this.statusColumnVis.setSelected(true);

		this.executorColumnVis = new CheckBox("Исполнитель");
		this.executorColumnVis.setSelected(true);

		this.descriptionsColumnVis = new CheckBox("Описание");
		this.descriptionsColumnVis.setSelected(true);

		this.startDate = new CheckBox("Начало");
		this.endDate = new CheckBox("Конец");

		this.startDatePic = new DatePicker();
		this.startDatePic.setDisable(true);
		this.startDatePic.setValue(LocalDate.now().minusWeeks(1));

		this.endDatePic = new DatePicker();
		this.endDatePic.setDisable(true);
		this.endDatePic.setValue(LocalDate.now());
		
		this.exportToExcel = new Button("Экспортировать в Excel...");
		
		this.init();
	}

	public void setFilterEvent(final EventHandler<ActionEvent> event) {
		this.initialsColumnVis.setOnAction(event);
		this.dateStartColumnVis.setOnAction(event);
		this.statusColumnVis.setOnAction(event);
		this.executorColumnVis.setOnAction(event);
		this.descriptionsColumnVis.setOnAction(event);
		this.startDatePic.setOnAction(event);
		this.endDatePic.setOnAction(event);
		
		this.startDate.setOnAction(e -> {
			startDatePic.setDisable(!startDatePic.isDisable());			
			event.handle(e);
		});

		this.endDate.setOnAction(e -> {
			endDatePic.setDisable(!endDatePic.isDisable());
			event.handle(e);
		});
	}

	private void init() {
		this.tab.setText("Заявки в ОИТ");
		
		this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);		
		table.setFixedCellSize(Region.USE_COMPUTED_SIZE);
		
		final TableColumn<MBid, Integer> numberColumn = new TableColumn<>("Номер");
		numberColumn.setCellValueFactory(new PropertyValueFactory<MBid, Integer>("number"));
		numberColumn.setSortType(TableColumn.SortType.DESCENDING);
		
		final TableColumn<MBid, String> initialColumn = new TableColumn<>("Ф.И.О.");
		initialColumn.setCellValueFactory(new PropertyValueFactory<MBid, String>("initials"));
		
		final TableColumn<MBid, Date> startDateColumn = new TableColumn<>("Дата подачи");
		startDateColumn.setCellValueFactory(new PropertyValueFactory<MBid, Date>("dateStart"));
		startDateColumn.setCellFactory(film -> {
	    	 return new TableCell<MBid, Date>() {
	    	        @Override
	    	        protected void updateItem(Date item, boolean empty) {
	    	        	Platform.runLater(() -> {
		    	        	setText("");
		    	        	super.updateItem(item, empty);
		    	            if (item == null || empty) {
		    	                setText("");
		    	            } else {		    	            	
		    	    	    	final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		    	    	    	
		    	                setText(dateFormat.format(item));	
		    	            }
	    	        	});
	    	        }
	    	    };
		});
		final TableColumn<MBid, Date> endDateColumn = new TableColumn<>("Дата выполнения");
		endDateColumn.setCellValueFactory(new PropertyValueFactory<MBid, Date>("dateEnd"));
		endDateColumn.setCellFactory(startDateColumn.getCellFactory());
		
		final TableColumn<MBid, String> statusColumn = new TableColumn<>("Статус");
		statusColumn.setCellValueFactory(new PropertyValueFactory<MBid, String>("status"));
		
		final TableColumn<MBid, String> executorColumn = new TableColumn<>("Исполнитель");
		executorColumn.setCellValueFactory(new PropertyValueFactory<MBid, String>("executor"));
		
		final TableColumn<MBid, String> descriptionColumn = new TableColumn<>("Описание");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<MBid, String>("description"));
		descriptionColumn.setCellFactory(film -> { 
				final TableCell<MBid, String> cell = new TableCell<>();
	            final Text text = new Text();
	            cell.setGraphic(text);
	            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
	            text.wrappingWidthProperty().bind(cell.widthProperty());
	            text.textProperty().bind(cell.itemProperty());
	            return cell ;
	    });
		
		numberColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		initialColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		startDateColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		endDateColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		statusColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		executorColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		descriptionColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
		
		this.table.getColumns().addAll(numberColumn, initialColumn, 
				startDateColumn, descriptionColumn,
				executorColumn, statusColumn, endDateColumn);
		
		this.table.getSortOrder().add(numberColumn);
		
		final FlowPane flowPane = new FlowPane();
		flowPane.setHgap(5);
		flowPane.setVgap(5);

		flowPane.getChildren().addAll(initialsColumnVis, dateStartColumnVis, statusColumnVis, executorColumnVis, descriptionsColumnVis, startDate, startDatePic, endDate, endDatePic, this.exportToExcel);
		
		this.mainPane.setTop(flowPane);
		this.mainPane.setCenter(this.table);
		
		this.tab.setContent(this.mainPane);
	}

	public LocalDate getStartDatePic() {
		return this.startDatePic.getValue();
	}
	
	public LocalDate getEndDatePic() {
		return this.endDatePic.getValue();
	}
	
	public void setVisibleInitialsColumnVis(boolean vis) {
		this.initialsColumnVis.setVisible(vis);
	}
	
	public void setVisibleDateStartColumnVis(boolean vis) {
		this.dateStartColumnVis.setVisible(vis);
	}
	
	public void setVisibleStatusColumnVis(boolean vis) {
		this.statusColumnVis.setVisible(vis);
	}
	
	public void setVisibleExecutorColumnVis(boolean vis) {
		this.executorColumnVis.setVisible(vis);
	}
	
	public void setVisibleDescriptionsColumnVis(boolean vis) {
		this.descriptionsColumnVis.setVisible(vis);
	}
	
	public boolean isInitialsColumnVis() {
		return this.initialsColumnVis.isSelected();
	}
	
	public boolean isDateStartColumnVis() {
		return this.dateStartColumnVis.isSelected();
	}
	
	public boolean isStatusColumnVis() {
		return this.statusColumnVis.isSelected();
	}
	
	public boolean isExecutorColumnVis() {
		return this.executorColumnVis.isSelected();
	}
	
	public boolean isDescriptionsColumnVis() {
		return this.descriptionsColumnVis.isSelected();
	}
	
	public boolean isStartDate() {
		return this.startDate.isSelected();
	}
	
	public boolean isEndDate() {
		return this.endDate.isSelected();
	}
	
	public void setEventToExportExcel(EventHandler<ActionEvent> e) {
		this.exportToExcel.setOnAction(e);
	}
	
	public void addBid(final MBid bid) {
		Tools.runOnThreadFX(() -> {
			this.table.getItems().add(bid);
		});
	}
	
	public List<MBid> getViewBid() {
		return this.table.getItems();
	}
	
	public void setAllBid(final Collection<MBid> bid) {
		this.addAllBid(bid);
	}

	public void addAllBid(final Collection<MBid> bid) {
		Tools.runOnThreadFX(() -> {
			this.table.getItems().clear();
			this.table.getItems().addAll(bid);
			this.table.sort();
		});
	}
}
