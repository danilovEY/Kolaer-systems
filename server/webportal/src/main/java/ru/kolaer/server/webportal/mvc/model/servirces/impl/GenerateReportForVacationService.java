package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.mvc.model.converter.DateTimeConverter;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.dao.VacationDao;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.GenerateReportExportRequest;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UploadFileService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenerateReportForVacationService {
    private final UploadFileService uploadFileService;
    private final VacationDao vacationDao;
    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;
    private final PostDao postDao;

    public GenerateReportForVacationService(UploadFileService uploadFileService,
                                            VacationDao vacationDao,
                                            EmployeeDao employeeDao,
                                            DepartmentDao departmentDao,
                                            PostDao postDao) {
        this.uploadFileService = uploadFileService;
        this.vacationDao = vacationDao;
        this.employeeDao = employeeDao;
        this.departmentDao = departmentDao;
        this.postDao = postDao;
    }


//    public void updateEmployee(ResultUpdate resultUpdate) {
//        EmployeeSort employeeSort = new EmployeeSort();
//        employeeSort.setSortPersonnelNumber(SortType.ASC);
//
//        EmployeeFilter employeeFilter = new EmployeeFilter();
//        employeeFilter.setTypeFilterDismissalDate(FilterType.IS_NULL);
//
//        List<EmployeeDto> allActualEmployees = this.employeeService.getAll(employeeSort, employeeFilter);
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//
//        HSSFSheet sheet = workbook.createSheet();
//
//        for (int rowIndex = 0; rowIndex < allActualEmployees.size(); rowIndex++) {
//            HSSFRow row = sheet.createRow(rowIndex);
//
//            createCells(row, allActualEmployees.get(rowIndex));
//        }
//
//        UploadFileEntity uploadFileEntity = this.uploadFileService.createFile("oldDbReport", "users_1c.xls", false);
//        String absolutePath = this.uploadFileService.getAbsolutePath(uploadFileEntity);
//
//        File file = new File(absolutePath);
//
//        try(FileOutputStream out = new FileOutputStream(file)) {
//            workbook.write(out);
//            workbook.close();
//
//        } catch (FileNotFoundException e) {
//            log.error("Не удалось создать файл");
//        } catch (IOException e) {
//            log.error("Не удалось записать данные в файл", e);
//        }
//
//        if (file.length() > 0) {
//            sendMail(uploadFileEntity);
//        }
//    }

    ResponseEntity generateReportExtort(GenerateReportExportRequest request, HttpServletResponse response) {
        Map<Long, List<VacationEntity>> vacationMap = vacationDao.findAll(request)
                .stream()
                .collect(Collectors.groupingBy(VacationEntity::getEmployeeId));

        if (vacationMap.isEmpty()) {
            throw new NotFoundDataException("Не найдены отпуска");
        }

        DepartmentEntity departmentEntity = departmentDao.findById(request.getDepartmentId());

        List<EmployeeEntity> employees = employeeDao.findById(vacationMap.keySet());
        employees.sort(Comparator.comparing(EmployeeEntity::getInitials));

        Set<Long> postIds = employees
                .stream()
                .map(EmployeeEntity::getPostId)
                .collect(Collectors.toSet());

        Map<Long, PostEntity> postMap = postDao.findById(postIds)
                .stream()
                .collect(Collectors.toMap(PostEntity::getId, Function.identity()));


        try (XSSFWorkbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/template/vacation_template.xlsx"))) {
            XSSFSheet sheet = setDepartmentName(workbook.getSheetAt(0), departmentEntity);
            sheet = setVacations(sheet, vacationMap, employees, postMap, departmentEntity);

            UploadFileEntity uploadFileEntity = saveWorkBook(workbook, departmentEntity.getAbbreviatedName());
            return uploadFileService.loadFile(uploadFileEntity.getId(), uploadFileEntity.getFileName(), response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private UploadFileEntity saveWorkBook(XSSFWorkbook workbook, String departmentName) throws IOException {
        String fileName = departmentName + " (Отчет).xlsx";

        UploadFileEntity tempFile = uploadFileService.createTempFile(fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(uploadFileService.getAbsolutePath(tempFile))){
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

        } finally {
            workbook.close();
        }

        return tempFile;
    }

    private XSSFSheet setVacations(XSSFSheet sheet,
                                   Map<Long, List<VacationEntity>> vacationMap,
                                   List<EmployeeEntity> employees,
                                   Map<Long, PostEntity> postMap,
                                   DepartmentEntity departmentEntity) {
        int indexRow = 17;

        XSSFRow currentRow = sheet.getRow(indexRow);

        for (EmployeeEntity employee : employees) {
            for (VacationEntity vacation : vacationMap.get(employee.getId())) {
                sheet.shiftRows(indexRow + 1, sheet.getLastRowNum(), 1);

                XSSFRow nextRow = sheet.createRow(indexRow + 1);
                nextRow.copyRowFrom(currentRow, new CellCopyPolicy());

                XSSFCell departmentCell = currentRow.getCell(0);
                departmentCell.setCellValue(departmentEntity.getAbbreviatedName());

                XSSFCell postCell = currentRow.getCell(1);
                postCell.setCellValue(postMap.get(employee.getPostId()).getAbbreviatedName());

                XSSFCell initialsCell = currentRow.getCell(2);
                initialsCell.setCellValue(employee.getInitials());

                XSSFCell personnelNumberCell = currentRow.getCell(3);
                personnelNumberCell.setCellValue(employee.getPersonnelNumber());

                XSSFCell vacationDaysCell = currentRow.getCell(4);
                vacationDaysCell.setCellValue(vacation.getVacationDays());

                XSSFCell vacationDateCell = currentRow.getCell(5);
                vacationDateCell.setCellValue(DateTimeConverter.dateToString(vacation.getVacationFrom()));

                indexRow = indexRow + 1;
                currentRow = nextRow;
            }
        }

        sheet.removeRow(currentRow);

        return sheet;
    }

    private XSSFSheet setDepartmentName(XSSFSheet sheet, DepartmentEntity departmentEntity) {
        XSSFRow depNameRow = sheet.getRow(1);
        XSSFCell depNameCell = depNameRow.getCell(0);

        depNameCell.setCellValue(departmentEntity.getAbbreviatedName());

        return sheet;
    }

    private String createCells(XSSFRow row, EmployeeDto employee) {
        String departmentName = Optional.ofNullable(employee.getDepartment())
                .map(dep -> String.format("%s (%s)", dep.getAbbreviatedName(), dep.getName()))
                .orElse("NONE (Unknown)");

        String postName = Optional.ofNullable(employee.getPost())
                .map(PostDto::getAbbreviatedName)
                .orElse("NONE");

        String pn = employee.getPersonnelNumber().toString().substring(4);

        XSSFCell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(employee.getInitials());

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(departmentName);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue(postName);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue(employee.getGender().getName().toLowerCase());

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue(pn);

//        cell = row.createCell(5, CellType.STRING);
//        cell.setCellValue(simpleDateFormat.format(employee.getBirthday()));
//
//        if(employee.getEmploymentDate() != null) {
//            cell = row.createCell(6, CellType.STRING);
//            cell.setCellValue(simpleDateFormat.format(employee.getEmploymentDate()));
//        }

        return pn;
    }
}
