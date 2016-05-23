package ru.kolaer.admin.bid.mvp.presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.admin.bid.mvp.model.BidModelManager;
import ru.kolaer.admin.bid.mvp.model.MBid;
import ru.kolaer.admin.bid.mvp.view.VBidTabContent;
import ru.kolaer.admin.bid.mvp.view.VTabContent;
import ru.kolaer.api.tools.Tools;

public class PBidTable {
	private final Logger LOG = LoggerFactory.getLogger(PBidTable.class);

	private VBidTabContent vBidTabContent;
	private final BidModelManager bidModelManager;

	private List<MBid> bidList;

	public PBidTable(final BidModelManager bidModelManager) {
		this.bidModelManager = bidModelManager;
		Tools.runOnThreadFXAndWain(() -> {
			this.vBidTabContent = new VBidTabContent();
		}, 1, TimeUnit.MINUTES);

		final ExecutorService getDataThread = Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(() -> {
			LOG.info("Получение данных...");
			this.bidList = new ArrayList<>(this.bidModelManager.updateModel());
			this.vBidTabContent.setAllBid(this.bidList);
		}, getDataThread).exceptionally(t -> {
			return null;
		});

		this.vBidTabContent.setFilterEvent(e -> {
			List<MBid> list = new ArrayList<>(this.bidList);
			
			if(this.vBidTabContent.isStartDate()) {
				list = list.parallelStream().filter(bid->{
					final Date date = Date.from(this.vBidTabContent.getStartDatePic().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
					if(bid.getDateStart().after(date) || bid.getDateStart().equals(date)) {
						return true;
					} 
					return false;
				}).collect(Collectors.toList());				
			}
			
			if(this.vBidTabContent.isEndDate()) {
				list = list.parallelStream().filter(bid->{
					final Date date = Date.from(this.vBidTabContent.getEndDatePic().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
					if(bid.getDateStart().before(date) || bid.getDateStart().equals(date)) {
						return true;
					} 
					return false;
				}).collect(Collectors.toList());				
			}

			vBidTabContent.addAllBid(list);	
		});
		
		vBidTabContent.setEventToExportExcel(e -> {
			final HSSFWorkbook workbook = new HSSFWorkbook();
			final HSSFSheet sheet = workbook.createSheet("Zayavki");

			final AtomicInteger atomicIntegerRow = new AtomicInteger(0);
			
			Row row = sheet.createRow(atomicIntegerRow.getAndIncrement());
			row.createCell(0).setCellValue("Номер");
			
			if(vBidTabContent.isDateStartColumnVis()) {
				row.createCell(1).setCellValue("Дата подачи");
			}
			
			if(vBidTabContent.isInitialsColumnVis()) {
				row.createCell(2).setCellValue("Ф.И.О.");
			}
			
			if(vBidTabContent.isDescriptionsColumnVis()) {
				row.createCell(3).setCellValue("Описание");
			}
			
			final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

			vBidTabContent.getViewBid().stream().forEach(bid -> {			
				if(bid.getStatus().equals("Закрыта")) {	
					Row row1 = sheet.createRow(atomicIntegerRow.getAndIncrement());
					int columnIndex = 0;
					
					Cell cell = row1.createCell(columnIndex++);
					cell.setCellValue(bid.getNumber());
					
					if(vBidTabContent.isDateStartColumnVis()) {
						cell = row1.createCell(columnIndex++);
						cell.setCellValue(dateFormat.format(bid.getDateStart()));
					}
					
					if(vBidTabContent.isInitialsColumnVis()) {
						cell = row1.createCell(columnIndex++);
						cell.setCellValue(bid.getInitials());
					}
					
					if(vBidTabContent.isDescriptionsColumnVis()) {
						cell = row1.createCell(columnIndex++);
						cell.setCellValue(bid.getDescription());
					}	
				}
			});

			try(final FileOutputStream out = new FileOutputStream(new File("C:\\new.xls"))){
				workbook.write(out);
				workbook.close();
				System.out.println("Excel written successfully..");
			}catch(FileNotFoundException e1){
				e1.printStackTrace();
			}catch(IOException e2){
				e2.printStackTrace();
			}
		});

	}

	public VTabContent getVTabContent() {
		return this.vBidTabContent;
	}

	public BidModelManager getBidModelManager() {
		return bidModelManager;
	}
}
