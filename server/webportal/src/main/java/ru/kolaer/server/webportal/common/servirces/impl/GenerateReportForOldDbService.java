package ru.kolaer.server.webportal.common.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.common.dto.FilterType;
import ru.kolaer.server.webportal.common.dto.ResultUpdate;
import ru.kolaer.server.webportal.common.dto.SortType;
import ru.kolaer.server.webportal.common.servirces.EmployeeService;
import ru.kolaer.server.webportal.common.servirces.UpdatableEmployeeService;
import ru.kolaer.server.webportal.common.servirces.UploadFileService;
import ru.kolaer.server.webportal.microservice.employee.EmployeeFilter;
import ru.kolaer.server.webportal.microservice.employee.EmployeeSort;
import ru.kolaer.server.webportal.microservice.storage.UploadFileDto;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GenerateReportForOldDbService implements UpdatableEmployeeService {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final UploadFileService uploadFileService;
    private final JavaMailSender mailSender;
    private final SimpleMailMessage mailMessage;
    private final Environment environment;
    private final EmployeeService employeeService;

    private String emailToSend;

    public GenerateReportForOldDbService(UploadFileService uploadFileService,
                                         JavaMailSender mailSender,
                                         SimpleMailMessage mailMessage,
                                         Environment environment,
                                         EmployeeService employeeService) {
        this.uploadFileService = uploadFileService;
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
        this.environment = environment;
        this.employeeService = employeeService;
    }

    @PostConstruct
    public void init() {
        this.emailToSend = environment.getProperty("old.db.report.email");
    }

    @Override
    @Async
    public void updateEmployee(ResultUpdate resultUpdate) {
        EmployeeSort employeeSort = new EmployeeSort();
        employeeSort.setSortPersonnelNumber(SortType.ASC);

        EmployeeFilter employeeFilter = new EmployeeFilter();
        employeeFilter.setTypeFilterDismissalDate(FilterType.IS_NULL);

        List<EmployeeDto> allActualEmployees = this.employeeService.getAll(employeeSort, employeeFilter);

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet();

        for (int rowIndex = 0; rowIndex < allActualEmployees.size(); rowIndex++) {
            HSSFRow row = sheet.createRow(rowIndex);

            createCells(row, allActualEmployees.get(rowIndex));
        }

        UploadFileDto uploadFileEntity = this.uploadFileService.createFile("oldDbReport", "users_1c.xls", true, true, true, true);
        String absolutePath = this.uploadFileService.getAbsolutePath(uploadFileEntity);

        File file = new File(absolutePath);

        try(FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
            workbook.close();

        } catch (FileNotFoundException e) {
            log.error("Не удалось создать файл");
        } catch (IOException e) {
            log.error("Не удалось записать данные в файл", e);
        }

        if (file.length() > 0) {
            sendMail(uploadFileEntity);
        }
    }

    private boolean sendMail(UploadFileDto uploadFileEntity) {
        if (uploadFileEntity != null && emailToSend != null) {
            this.mailSender.send(mimeMessage -> {
                Resource resource = uploadFileService.loadFile(uploadFileEntity.getPath(), uploadFileEntity.isAbsolutePath());

                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                messageHelper.setFrom(mailMessage.getFrom());
                messageHelper.setTo(emailToSend);
                messageHelper.setSubject("Обновление базы");
                messageHelper.setText("Файл для обновление старой базы");
                messageHelper.addAttachment(uploadFileEntity.getFileName(), resource);
            });
            return true;
        }

        return false;
    }

    private String createCells(HSSFRow row, EmployeeDto employee) {
        String departmentName = Optional.ofNullable(employee.getDepartment())
                .map(dep -> String.format("%s (%s)", dep.getAbbreviatedName(), dep.getName()))
                .orElse("NONE (Unknown)");

        String postName = Optional.ofNullable(employee.getPost())
                .map(PostDto::getAbbreviatedName)
                .orElse("NONE");

        String pn = employee.getPersonnelNumber().toString().substring(4);

        HSSFCell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(employee.getInitials());

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(departmentName);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue(postName);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue(employee.getGender().getName().toLowerCase());

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue(pn);

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue(simpleDateFormat.format(employee.getBirthday()));

        if(employee.getEmploymentDate() != null) {
            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue(simpleDateFormat.format(employee.getEmploymentDate()));
        }

        return pn;
    }
}
