package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UploadFileService;
import ru.kolaer.server.webportal.mvc.model.servirces.VacationService;

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

            UploadFileEntity uploadFileEntity = saveWorkBook(workbook, "График пересечений");
            return uploadFileService.loadFile(uploadFileEntity.getId(), uploadFileEntity.getFileName(), response);
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

        XSSFCellStyle workStyle = sheet.getWorkbook().createCellStyle();
        workStyle.setFillPattern(FillPatternType.FINE_DOTS);
        workStyle.setFillBackgroundColor(new XSSFColor(Color.WHITE));
        workStyle.setFillForegroundColor(new XSSFColor(Color.WHITE));
        workStyle.setBorderBottom(BorderStyle.THIN);
        workStyle.setBorderTop(BorderStyle.THIN);
        workStyle.setBorderLeft(BorderStyle.THIN);
        workStyle.setBorderRight(BorderStyle.THIN);
        workStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFCellStyle vacationStyle = sheet.getWorkbook().createCellStyle();
        vacationStyle.setFillPattern(FillPatternType.FINE_DOTS);
        vacationStyle.setFillBackgroundColor(new XSSFColor(Color.GREEN));
        vacationStyle.setFillForegroundColor(new XSSFColor(Color.GREEN));
        vacationStyle.setBorderBottom(BorderStyle.THIN);
        vacationStyle.setBorderTop(BorderStyle.THIN);
        vacationStyle.setBorderLeft(BorderStyle.THIN);
        vacationStyle.setBorderRight(BorderStyle.THIN);

        XSSFCellStyle holidayStyle = sheet.getWorkbook().createCellStyle();
        holidayStyle.setFillPattern(FillPatternType.FINE_DOTS);
        holidayStyle.setFillBackgroundColor(new XSSFColor(Color.RED));
        holidayStyle.setFillForegroundColor(new XSSFColor(Color.RED));
        holidayStyle.setBorderBottom(BorderStyle.THIN);
        holidayStyle.setBorderTop(BorderStyle.THIN);
        holidayStyle.setBorderLeft(BorderStyle.THIN);
        holidayStyle.setBorderRight(BorderStyle.THIN);

        XSSFCellStyle dayOffStyle = sheet.getWorkbook().createCellStyle();
        dayOffStyle.setFillPattern(FillPatternType.FINE_DOTS);
        dayOffStyle.setFillBackgroundColor(new XSSFColor(Color.BLUE));
        dayOffStyle.setFillForegroundColor(new XSSFColor(Color.BLUE));
        dayOffStyle.setBorderBottom(BorderStyle.THIN);
        dayOffStyle.setBorderTop(BorderStyle.THIN);
        dayOffStyle.setBorderLeft(BorderStyle.THIN);
        dayOffStyle.setBorderRight(BorderStyle.THIN);

        XSSFRow yearRow = sheet.createRow(rowNumber++);
        XSSFRow monthRow = sheet.createRow(rowNumber++);
        XSSFRow dayRow = sheet.createRow(rowNumber++);

        XSSFCell title = yearRow.createCell(0);
        title.setCellValue("Сотрудники");
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

        mergedRegion = new CellRangeAddress(0, 3, 0, 0);
        sheet.addMergedRegion(mergedRegion);

        RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);

        for (VacationReportCalendarEmployeeDto employee : vacations) {
            XSSFRow row = sheet.createRow(rowNumber++);

            colNumber = 0;

            XSSFCell initialsCell = row.createCell(colNumber++);
            initialsCell.setCellStyle(workStyle);
            initialsCell.setCellValue(employee.getEmployee());

            for (VacationReportCalendarYearDto year : employee.getYears()) {
                for (VacationReportCalendarMonthDto month : year.getMonths()) {
                    for (VacationReportCalendarDayDto day : month.getDays()) {
                        XSSFCell dayCell = row.createCell(colNumber++);

                        if (day.isVacation()) {
                            dayCell.setCellStyle(vacationStyle);
                        } else if (day.isHoliday()) {
                            dayCell.setCellStyle(holidayStyle);
                        } else if (day.isDayOff()) {
                            dayCell.setCellStyle(dayOffStyle);
                        } else {
                            dayCell.setCellStyle(workStyle);
                        }

                        sheet.setColumnWidth(colNumber - 1, 300);
                    }
                }
            }
        }

        sheet.setColumnWidth(0, 10000);

        return sheet;
    }

    private UploadFileEntity saveWorkBook(XSSFWorkbook workbook, String departmentName) throws IOException {
        String fileName = departmentName + ".xlsx";

        UploadFileEntity tempFile = uploadFileService.createFile("temp", fileName, true);

        try (FileOutputStream fileOutputStream = new FileOutputStream(uploadFileService.getAbsolutePath(tempFile))){
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

        } finally {
            workbook.close();
        }

        return tempFile;
    }
}
