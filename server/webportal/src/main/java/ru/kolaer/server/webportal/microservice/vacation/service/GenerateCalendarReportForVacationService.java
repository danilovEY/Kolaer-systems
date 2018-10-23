package ru.kolaer.server.webportal.microservice.vacation.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.common.exception.NotFoundDataException;
import ru.kolaer.server.webportal.microservice.storage.service.UploadFileService;
import ru.kolaer.server.webportal.microservice.storage.dto.UploadFileDto;
import ru.kolaer.server.webportal.microservice.vacation.dto.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class GenerateCalendarReportForVacationService {
    private final UploadFileService uploadFileService;
    private final VacationService vacationService;

    public GenerateCalendarReportForVacationService(UploadFileService uploadFileService,
                                                    VacationService vacationService) {
        this.uploadFileService = uploadFileService;
        this.vacationService = vacationService;
    }

    public ResponseEntity generateCalendarReportExtort(GenerateReportCalendarRequest request, HttpServletResponse response) {
        List<VacationReportCalendarEmployeeDto> vacations = vacationService.generateReportCalendar(request);

        if (vacations.isEmpty()) {
            throw new NotFoundDataException("Не найдены отпуска");
        }


        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("График пересечений");

            sheet = setVacations(sheet, vacations);

            UploadFileDto uploadFileEntity = saveWorkBook(workbook, "График пересечений");
            return uploadFileService.loadFile(uploadFileEntity, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private XSSFSheet setVacations(XSSFSheet sheet, List<VacationReportCalendarEmployeeDto> vacations) {
        int rowNumber = 0;

        XSSFCellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFCellStyle counterStyle = getCellStyle(sheet.getWorkbook(), Color.ORANGE);
        XSSFCellStyle workStyle = getCellStyle(sheet.getWorkbook(), Color.WHITE);
        XSSFCellStyle vacationStyle = getCellStyle(sheet.getWorkbook(), Color.GREEN);
        XSSFCellStyle holidayStyle = getCellStyle(sheet.getWorkbook(), Color.PINK);
        XSSFCellStyle dayOffStyle = getCellStyle(sheet.getWorkbook(), Color.BLUE);

        XSSFRow yearRow = sheet.createRow(rowNumber++);
        XSSFRow monthRow = sheet.createRow(rowNumber++);
        XSSFRow dayRow = sheet.createRow(rowNumber++);

        XSSFCell title = yearRow.createCell(0);
        title.setCellValue("Объекты");
        title.setCellStyle(titleStyle);

        int colNumber = 1;

        for (VacationReportCalendarYearDto year : vacations.get(0).getYears()) {
            title = yearRow.createCell(colNumber);
            title.setCellValue(year.getYear());
            title.setCellStyle(titleStyle);
            for (VacationReportCalendarMonthDto month : year.getMonths()) {
                title = monthRow.createCell(colNumber);
                title.setCellValue(month.getMonth());
                title.setCellStyle(titleStyle);
                for (VacationReportCalendarDayDto day : month.getDays()) {
                    title = dayRow.createCell(colNumber++);
                    title.setCellValue(day.getDay());
                    title.setCellStyle(titleStyle);
                }

                CellRangeAddress mergedRegion = new CellRangeAddress(1, 1, colNumber - month.getDays().size(), colNumber - 1);

                sheet.addMergedRegion(mergedRegion);

                RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);
            }
        }

        CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 1, colNumber - 1);
        sheet.addMergedRegion(mergedRegion);

        RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);

        mergedRegion = new CellRangeAddress(0, 2, 0, 0);
        sheet.addMergedRegion(mergedRegion);

        RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);

        for (VacationReportCalendarEmployeeDto employee : vacations) {
            XSSFRow row = sheet.createRow(rowNumber++);

            colNumber = 0;

            XSSFCell initialsCell = row.createCell(colNumber++);
            initialsCell.setCellStyle(titleStyle);
            initialsCell.setCellValue(employee.getEmployee());

            int counterStartCell = 0;

            for (VacationReportCalendarYearDto year : employee.getYears()) {
                for (VacationReportCalendarMonthDto month : year.getMonths()) {
                    for (VacationReportCalendarDayDto day : month.getDays()) {
                        XSSFCell dayCell = row.createCell(colNumber++);

                        if (day.isCounter()) {
                            if (counterStartCell == 0) {
                                counterStartCell = colNumber - 1;
                                dayCell.setCellValue(day.getTitle());
                            }
                        } else {
                            if (counterStartCell != 0) {
                                mergedRegion = new CellRangeAddress(rowNumber - 1, rowNumber - 1, counterStartCell, colNumber - 1);
                                sheet.addMergedRegion(mergedRegion);

                                RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
                                RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
                                RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
                                RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);

                                counterStartCell = 0;
                            }
                        }

                        if (day.isVacation()) {
                            dayCell.setCellStyle(vacationStyle);
                        } else if (day.isHoliday()) {
                            dayCell.setCellStyle(holidayStyle);
                        } else if (day.isDayOff()) {
                            dayCell.setCellStyle(dayOffStyle);
                        } else if (day.isCounter()) {
                            dayCell.setCellStyle(counterStyle);
                        } else {
                            dayCell.setCellStyle(workStyle);
                        }

                        sheet.setColumnWidth(colNumber - 1, 150);
                    }
                }
            }
        }

        sheet.setColumnWidth(0, 10000);

        return sheet;
    }

    private XSSFCellStyle getCellStyle(XSSFWorkbook workbook, Color backgroundColor) {
        XSSFColor borderColor = new XSSFColor(Color.LIGHT_GRAY);
        XSSFColor backColor = new XSSFColor(backgroundColor);

        XSSFFont xssfFont = workbook.createFont();
        xssfFont.setFontHeight(8);
        xssfFont.setBold(true);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
        cellStyle.setFillBackgroundColor(backColor);
        cellStyle.setFillForegroundColor(backColor);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, borderColor);
        cellStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, borderColor);
        cellStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, borderColor);
        cellStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, borderColor);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(xssfFont);

        return cellStyle;
    }

    private UploadFileDto saveWorkBook(XSSFWorkbook workbook, String departmentName) throws IOException {
        String fileName = departmentName + ".xlsx";

        UploadFileDto tempFile = uploadFileService.createTempFile(fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(uploadFileService.getAbsolutePath(tempFile))){
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

        } finally {
            workbook.close();
        }

        return tempFile;
    }
}
