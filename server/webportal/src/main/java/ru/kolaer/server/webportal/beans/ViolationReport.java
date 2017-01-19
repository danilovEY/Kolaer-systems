package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.servirces.JournalViolationService;
import ru.kolaer.server.webportal.mvc.model.servirces.TypeViolationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ViolationService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by danilovey on 18.01.2017.
 */
@Component
@Slf4j
public class ViolationReport {
    @Autowired
    private ViolationService violationService;

    @Autowired
    private JournalViolationService journalViolationService;

    @Autowired
    private TypeViolationService typeViolationService;

    public File makeReportStatisticByDep(Integer idDep, boolean isDetailed) {
        if(idDep == null || idDep < 0)
            throw new BadRequestException("ID подразделения должно быть больше нуля!");

        return makeReportStatisticByDepBetween(idDep, isDetailed, null, null);
    }

    public File makeReportStatisticByDepBetween(Integer idDep, boolean isDetailed, Date start, Date end) {
        if(idDep == null || idDep < 0)
            throw new BadRequestException("ID подразделения должно быть больше нуля!");

        return makeReportStatisticByViolations(this.violationService.getAllByJournalAndEffective(idDep, start, end), isDetailed);
    }

    public File makeReportStatisticByAllDep(boolean isDetailed) {
        return makeReportStatisticByAllDepBetween(isDetailed, null, null);
    }

    public File makeReportStatisticByAllDepBetween(boolean isDetailed, Date start, Date end) {
        return makeReportStatisticByViolations(this.violationService.getAllEffective(start, end), isDetailed);
    }

    public File makeReportStatisticByViolations(final List<Violation> violations, boolean isDetailed) {
        if(violations.size() == 0) {
            return null;
        }

        final Resource violationStatisticResource = new ClassPathResource("template/violations/violation_report.xls");
        try(final HSSFWorkbook violationStatistic = new HSSFWorkbook(violationStatisticResource.getInputStream())) {

            HSSFSheet sheet = violationStatistic.getSheet("Статистика нарушений");
            sheet = this.updateSheetByStatistic(sheet, violations, sheet.getLastRowNum());

            if(isDetailed) {
                final CellStyle cellStyle = violationStatistic.createCellStyle();
                cellStyle.setDataFormat(violationStatistic.getCreationHelper()
                        .createDataFormat().getFormat("dd.MM.yyyy hh:mm"));

                sheet = violationStatistic.getSheet("Нарушения");

                Integer rowIndex = sheet.getLastRowNum();
                JournalViolation journalViolation = null;
                for(Violation v : violations) {
                    if(journalViolation == null || !journalViolation.getId().equals(v.getJournalViolation().getId())) {
                        journalViolation = this.journalViolationService.getById(v.getJournalViolation().getId());
                    }
                    copyRow(sheet, rowIndex, rowIndex+1);
                    final HSSFRow row = sheet.createRow(rowIndex++);
                    this.updateSheetByViolation(row, cellStyle, journalViolation, v);

                }
            } else {
                violationStatistic.removeSheetAt(1);
            }

            try {
                final String filePath = "violations\\reports\\"+UUID.randomUUID();
                final String fileName =  "Отчет нарушений.xls";

                final File dirs = new File(filePath);
                dirs.mkdirs();

                final File reportFile = new File(filePath + "\\" + fileName);

                FileOutputStream fileOut = new FileOutputStream(reportFile);
                violationStatistic.write(fileOut);
                fileOut.close();

                return reportFile;
            } catch (Exception ex) {
                log.error("Ошибка при записи данных!", ex);
            }
        } catch (IOException e) {
            log.error("Ошибка при чтении шаблона!", e);
        }


        return null;
    }

    private HSSFSheet updateSheetByStatistic(HSSFSheet sheet, List<Violation> violations, Integer rowIndex) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        final Date dateOfFirstDayInYear = cal.getTime();

        long totalAllViolationCountInFirst = 0;
        long totalEndViolationCountFirst = 0;
        long totalCountViolationEffectiveInYearFirst = 0;

        long totalAllViolationCountInSecond = 0;
        long totalEndViolationCountSecond = 0;
        long totalCountViolationEffectiveInYearSecond = 0;

        Integer lastIdType = 0;

        for(TypeViolation typeViolation : this.typeViolationService.getAll()) {
            Supplier<Stream<Violation>> violationByType = () -> violations.stream().filter(violation ->
                violation.getTypeViolation().getId().equals(typeViolation.getId())
            );

            final long allViolationCountInFirst = violationByType.get().filter(v -> v.getStageEnum() == StageEnum.I).count();
            final long endViolationCountFirst = violationByType.get()
                    .filter(v -> v.getStageEnum() == StageEnum.I
                            && v.getDateEndEliminationViolation() == null).count();

            final long allViolationCountInSecond = violationByType.get().count() - allViolationCountInFirst;
            final long endViolationCountSecond = allViolationCountInSecond - endViolationCountFirst;

            final long countViolationEffectiveInYearFirst = this.violationService
                    .getCountViolationEffectiveByTypeBetween(typeViolation.getId(), StageEnum.I, dateOfFirstDayInYear, new Date());

            final long countViolationEffectiveInYearSecond = this.violationService
                    .getCountViolationEffectiveByTypeBetween(typeViolation.getId(), StageEnum.II, dateOfFirstDayInYear, new Date());

            totalAllViolationCountInFirst += allViolationCountInFirst;
            totalEndViolationCountFirst += endViolationCountFirst;
            totalCountViolationEffectiveInYearFirst += countViolationEffectiveInYearFirst;

            totalAllViolationCountInSecond += allViolationCountInSecond;
            totalEndViolationCountSecond += endViolationCountSecond;
            totalCountViolationEffectiveInYearSecond += countViolationEffectiveInYearSecond;

            copyRow(sheet, rowIndex, rowIndex+1);
            final HSSFRow row = sheet.getRow(rowIndex++);

            Integer cellIndex = 0;

            lastIdType = typeViolation.getId();

            HSSFCell cell = row.getCell(cellIndex++);
            cell.setCellValue(typeViolation.getId());

            cell = row.getCell(cellIndex++);
            cell.setCellValue(typeViolation.getName());

            //Stage I
            cell = row.getCell(cellIndex++);
            cell.setCellValue(allViolationCountInFirst);

            cell = row.getCell(cellIndex++);
            cell.setCellValue(endViolationCountFirst);

            cell = row.getCell(cellIndex++);
            cell.setCellValue(countViolationEffectiveInYearFirst);

            //Stage II
            cell = row.getCell(cellIndex++);
            cell.setCellValue(allViolationCountInSecond);

            cell = row.getCell(cellIndex++);
            cell.setCellValue(endViolationCountSecond);

            cell = row.getCell(cellIndex);
            cell.setCellValue(countViolationEffectiveInYearSecond);
        }

        //ИТОГИ
        final HSSFRow row = sheet.getRow(rowIndex);

        Integer cellIndex = 0;

        HSSFCell cell = row.getCell(cellIndex++);
        cell.setCellValue(lastIdType);

        cell = row.getCell(cellIndex++);
        cell.setCellValue("ИТОГ");

        //Stage I
        cell = row.getCell(cellIndex++);
        cell.setCellValue(totalAllViolationCountInFirst);

        cell = row.getCell(cellIndex++);
        cell.setCellValue(totalEndViolationCountFirst);

        cell = row.getCell(cellIndex++);
        cell.setCellValue(totalCountViolationEffectiveInYearFirst);

        //Stage II
        cell = row.getCell(cellIndex++);
        cell.setCellValue(totalAllViolationCountInSecond);

        cell = row.getCell(cellIndex++);
        cell.setCellValue(totalEndViolationCountSecond);

        cell = row.getCell(cellIndex);
        cell.setCellValue(totalCountViolationEffectiveInYearSecond);

        return sheet;

    }

    public File makeReportJournal(Integer idJournal, boolean isAll, Date start, Date end) {
        if(idJournal == null || idJournal < 0)
            throw new BadRequestException("ID Журнала должно быть больше 0!");

        final List<Violation> violations = isAll
                ? this.violationService.getByIdJournal(idJournal).getData()
                : this.violationService.getAllByJournalAndEffective(idJournal, start, end);

        if(violations.size() == 0) {
            return null;
        }

        final JournalViolation journal = this.journalViolationService.getById(idJournal);
        journal.setViolations(violations);

        final HSSFWorkbook workbook = new HSSFWorkbook();

        final CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.getCreationHelper()
                .createDataFormat().getFormat("dd.MM.yyyy hh:mm"));

        HSSFSheet sheet = workbook.createSheet("Нарушения");



        sheet = this.updateSheetByJournal(sheet, cellStyle, journal, 2);
        return null;
    }

    private HSSFSheet updateSheetByJournal(HSSFSheet sheet, CellStyle cellDateStyle, JournalViolation journal, Integer rowIndex) {
        final HSSFRow row = sheet.createRow(rowIndex);

        for(final Violation violation : journal.getViolations()) {
            updateSheetByViolation(row, cellDateStyle, journal, violation);
        }

        return sheet;
    }

    private HSSFRow updateSheetByViolation(HSSFRow row, CellStyle cellDateStyle, JournalViolation journal, Violation violation) {
        Integer cellIndex = 0;
        //ID журнала
        HSSFCell cell = row.getCell(cellIndex++);
        cell.setCellValue(journal.getId());
        //Имя журнала
        cell = row.getCell(cellIndex++);
        cell.setCellValue(journal.getName());
        //Подразделение журнала
        cell = row.getCell(cellIndex++);
        cell.setCellValue(journal.getDepartament().getAbbreviatedName());
        //Имя записавшего журнал
        cell = row.getCell(cellIndex++);
        cell.setCellValue(journal.getWriter().getInitials());
        //ID нарушения
        cell = row.getCell(cellIndex++);
        cell.setCellValue(violation.getId());
        //Кто создал нарушение
        cell = row.getCell(cellIndex++);
        cell.setCellValue(violation.getWriter().getInitials());
        //Исполнитель нарушения
        cell = row.getCell(cellIndex++);
        if(violation.getExecutor() != null)
            cell.setCellValue(violation.getExecutor().getInitials());
        else
            cell.setCellValue("");
        //Описание нарушения
        cell = row.getCell(cellIndex++);
        cell.setCellValue(this.html2text(violation.getViolation()));
        //Описание для выполнения нарушения
        cell = row.getCell(cellIndex++);
        cell.setCellValue(this.html2text(violation.getTodo()));
        //Дата создания нарушения
        cell = row.getCell(cellIndex++);
        cell.setCellValue(violation.getStartMakingViolation());
        cell.setCellStyle(cellDateStyle);
        //Срок исполнения нарушения
        cell = row.getCell(cellIndex++);
        if(violation.getDateLimitEliminationViolation() != null) {
            cell.setCellValue(violation.getDateLimitEliminationViolation());
            cell.setCellStyle(cellDateStyle);
        }

        //Даты выполнения нарушения
        cell = row.getCell(cellIndex++);
        if(violation.getDateEndEliminationViolation() != null) {
            cell.setCellValue(violation.getDateEndEliminationViolation());
            cell.setCellStyle(cellDateStyle);
        }
        //Тип нарушения
        cell = row.getCell(cellIndex++);
        cell.setCellValue(violation.getTypeViolation().getName());
        //Ступень нарушения
        cell = row.getCell(cellIndex);
        cell.setCellValue(violation.getStageEnum().toString());

        return row;
    }

    private String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    private static void copyRow(HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            newCell.setCellStyle(oldCell.getCellStyle());

            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }
    }
}
