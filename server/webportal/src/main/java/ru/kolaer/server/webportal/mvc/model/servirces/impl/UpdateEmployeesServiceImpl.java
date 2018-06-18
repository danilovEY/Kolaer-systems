package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.EmployeeConverter;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdate;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeeDto;
import ru.kolaer.server.webportal.mvc.model.dto.UpdatableEmployee;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdatableEmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdateEmployeesService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
@Slf4j
public class UpdateEmployeesServiceImpl implements UpdateEmployeesService {
    private final EmployeeDao employeeDao;
    private final EmployeeConverter employeeConverter;
    private final PostDao postDao;
    private final DepartmentDao departmentDao;

    private final List<UpdatableEmployeeService> updatableEmployeeServices;

    private final ExcelReaderEmployee excelReaderEmployee;
    private final ExcelReaderDepartment excelReaderDepartment;
    private final ExcelReaderPost excelReaderPost;

    public UpdateEmployeesServiceImpl(
            EmployeeDao employeeDao,
            EmployeeConverter employeeConverter,
            PostDao postDao,
            DepartmentDao departmentDao,
            ExcelReaderEmployee excelReaderEmployee,
            ExcelReaderDepartment excelReaderDepartment,
            ExcelReaderPost excelReaderPost,
            @Autowired(required = false) List<UpdatableEmployeeService> updatableEmployeeServices) {
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
        this.postDao = postDao;
        this.departmentDao = departmentDao;

        this.updatableEmployeeServices = updatableEmployeeServices;

        this.excelReaderEmployee = excelReaderEmployee;
        this.excelReaderDepartment = excelReaderDepartment;
        this.excelReaderPost = excelReaderPost;
    }

    @Override
    @Transactional
    public void updateEmployees(@NonNull File file) {
        try {
            updateEmployees(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден!", e);
        }
    }

    @Override
    @Transactional
    public void updateEmployees(InputStream inputStream) {
        ResultUpdateEmployeeDto resultUpdateEmployeeDto = new ResultUpdateEmployeeDto();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            if (workbook.getNumberOfSheets() < 1) {
                throw new UnexpectedRequestParams("Книга пустая");
            }

            XSSFSheet sheetAt = workbook.getSheetAt(0);
            XSSFRow firstRow = sheetAt.getRow(sheetAt.getFirstRowNum());

            List<String> nameColumns = StreamSupport
                    .stream(firstRow.spliterator(), false)
                    .map(Cell::getStringCellValue)
                    .collect(Collectors.toList());

            Map<String, PostEntity> postEntityMap = new HashMap<>();
            Map<String, DepartmentEntity> departmentEntityMap = new HashMap<>();
            Map<String, UpdatableEmployee> newEmployeesMap = new HashMap<>();

            for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
                XSSFRow row = sheetAt.getRow(i);

                EmployeeEntity newEmployeeEntity = excelReaderEmployee.process(row, nameColumns);

                UpdatableEmployee updatableEmployee;

                String employeeKey = generateKey(newEmployeeEntity);
                if (employeeKey != null && !newEmployeesMap.containsKey(employeeKey)) {
                    updatableEmployee = new UpdatableEmployee(newEmployeeEntity);
                    newEmployeesMap.put(employeeKey, updatableEmployee);
                } else {
                    continue;
                }

                DepartmentEntity departmentEntity = excelReaderDepartment.process(row, nameColumns);
                PostEntity postEntity = excelReaderPost.parse(row, nameColumns);

                String postKey = generateKey(postEntity);
                String depKey = generateKey(departmentEntity);

                if (postKey != null && !postEntityMap.containsKey(postKey)) {
                    postEntityMap.put(postKey, postEntity);
                }

                if (depKey != null && !departmentEntityMap.containsKey(depKey)) {
                    departmentEntityMap.put(depKey, departmentEntity);
                }

                updatableEmployee.setDepartmentKey(depKey);
                updatableEmployee.setPostKey(postKey);
            }

            updatePostMap(postEntityMap);
            updateDepMap(departmentEntityMap);
            updateEmployeeMap(newEmployeesMap, resultUpdateEmployeeDto);

            saveDepartment(departmentEntityMap);
            savePost(postEntityMap);
            saveEmployees(newEmployeesMap, departmentEntityMap, postEntityMap);
        } catch (Exception ex) {
            log.error("Ошибка при чтении файла!", ex);
            throw new UnexpectedRequestParams(ex.getMessage(), ex, ErrorCode.PARSE_EXCEPTION);
        }

        ResultUpdate resultUpdate = new ResultUpdate();
        resultUpdate.setAddEmployee(employeeConverter.convertToDto(resultUpdateEmployeeDto.getAddEmployee()));
        resultUpdate.setDeleteEmployee(employeeConverter.convertToDto(resultUpdateEmployeeDto.getDeleteEmployee()));

        Optional.ofNullable(updatableEmployeeServices)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparingInt(UpdatableEmployeeService::getOrder))
                .forEach(service -> service.updateEmployee(resultUpdate));
    }

    private void saveEmployees(Map<String, UpdatableEmployee> newEmployeesMap,
                                                   Map<String, DepartmentEntity> depMap,
                                                   Map<String, PostEntity> postMap) {

//        Map<String, EmployeeEntity> kolaerBaseEmployeeMap = jdbcTemplateKolaerBase
//                .query("SELECT person_number, phone, mobile_phone, email FROM db_data_all", (rs, rowNum) -> {
//                    EmployeeEntity employeeEntity = new EmployeeEntity();
//                    String workPhone = Optional.ofNullable(rs.getString("phone")).orElse("");
//                    String mobilePhone = Optional.ofNullable(rs.getString("mobile_phone")).orElse("");
//                    if (StringUtils.hasText(mobilePhone))
//                        workPhone += "; " + mobilePhone;
//
//                    employeeEntity.setEmail(rs.getString("email"));
//                    employeeEntity.setPersonnelNumber(rs.getLong("person_number"));
//                    employeeEntity.setWorkPhoneNumber(workPhone);
//                    return employeeEntity;
//                }).stream()
//                .collect(Collectors.toMap(e -> String.valueOf(17240000L + e.getPersonnelNumber()), e -> e));

        for (UpdatableEmployee updatableEmployee : newEmployeesMap.values()) {
            EmployeeEntity employeeEntity = updatableEmployee.getEmployee();
            PostEntity postEntity = postMap.get(updatableEmployee.getPostKey());
            DepartmentEntity departmentEntity = depMap.get(updatableEmployee.getDepartmentKey());

            if (postEntity != null) {
                employeeEntity.setPostId(postEntity.getId());
            }

            if (departmentEntity != null) {
                employeeEntity.setDepartmentId(departmentEntity.getId());
            }
        }

        List<EmployeeEntity> toSaveEmployee = newEmployeesMap.values()
                .stream()
                .map(UpdatableEmployee::getEmployee)
                .collect(Collectors.toList());

        employeeDao.save(toSaveEmployee);


        List<DepartmentEntity> updateChifDep = new ArrayList<>();

        for (UpdatableEmployee updatableEmployee : newEmployeesMap.values()) {
            EmployeeEntity employeeEntity = updatableEmployee.getEmployee();
            PostEntity postEntity = postMap.get(updatableEmployee.getPostKey());
            DepartmentEntity departmentEntity = depMap.get(updatableEmployee.getDepartmentKey());

            if(departmentEntity != null && postEntity != null) {
                String postName = postEntity.getName().toLowerCase();
                Long idChief = departmentEntity.getChiefEmployeeId();
                if (!employeeEntity.getId().equals(idChief) &&
                        (postName.contains("начальник") ||
                                postName.contains("директор") ||
                                postName.contains("руководитель") ||
                                postName.contains("ведущий") ||
                                postName.contains("главный"))) {
                    departmentEntity.setChiefEmployeeId(employeeEntity.getId());
                    updateChifDep.add(departmentEntity);
                }
            }
        }

        departmentDao.save(updateChifDep);
    }

    private void savePost(Map<String, PostEntity> postEntityMap) {
        postDao.save(new ArrayList<>(postEntityMap.values()));
    }

    private void saveDepartment(Map<String, DepartmentEntity> departmentEntityMap) {
        departmentDao.save(new ArrayList<>(departmentEntityMap.values()));
    }

    private void updateEmployeeMap(Map<String, UpdatableEmployee> newEmployeesMap,
                                                      ResultUpdateEmployeeDto resultUpdateEmployeeDto) {
        List<EmployeeEntity> deletedEmployee = new ArrayList<>();

        for (EmployeeEntity employeeEntityFromDb : employeeDao.findAll()) {
            String originKey = generateKey(employeeEntityFromDb);

            UpdatableEmployee updatableEmployee = newEmployeesMap
                    .getOrDefault(originKey, new UpdatableEmployee(employeeEntityFromDb));

            if(!newEmployeesMap.containsKey(originKey)) {
                employeeEntityFromDb.setDismissalDate(new Date());
                deletedEmployee.add(employeeEntityFromDb);
            } else {
                EmployeeEntity employeeEntity = updatableEmployee.getEmployee();
                employeeEntityFromDb.setInitials(employeeEntity.getInitials());
                employeeEntityFromDb.setFirstName(employeeEntity.getFirstName());
                employeeEntityFromDb.setSecondName(employeeEntity.getSecondName());
                employeeEntityFromDb.setThirdName(employeeEntity.getThirdName());
                employeeEntityFromDb.setGender(employeeEntity.getGender());
                employeeEntityFromDb.setBirthday(employeeEntity.getBirthday());
                employeeEntityFromDb.setEmploymentDate(employeeEntity.getEmploymentDate());
                employeeEntityFromDb.setCategory(employeeEntity.getCategory());
                updatableEmployee.setEmployee(employeeEntityFromDb);
            }

            newEmployeesMap.put(originKey, updatableEmployee);
        }

        List<EmployeeEntity> addedEmployee = newEmployeesMap.values()
                .stream()
                .map(UpdatableEmployee::getEmployee)
                .filter(employee -> employee.getId() == null && employee.getDismissalDate() == null)
                .collect(Collectors.toList());

        resultUpdateEmployeeDto.setAddEmployee(addedEmployee);
        resultUpdateEmployeeDto.setDeleteEmployee(deletedEmployee);
    }

    private void updateDepMap(Map<String, DepartmentEntity> departmentEntityMap) {
        for (DepartmentEntity depEntityFromDb : departmentDao.findAll()) {
            String originKey = generateKey(depEntityFromDb);

            if(!departmentEntityMap.containsKey(originKey)) {
                depEntityFromDb.setDeleted(true);
            } else {
                DepartmentEntity departmentEntity = departmentEntityMap.get(originKey);
                depEntityFromDb.setAbbreviatedName(departmentEntity.getAbbreviatedName());
                depEntityFromDb.setName(departmentEntity.getName());
                depEntityFromDb.setExternalId(departmentEntity.getExternalId());
                depEntityFromDb.setDeleted(false);
            }

            departmentEntityMap.put(originKey, depEntityFromDb);
        }
    }

    private void updatePostMap(Map<String, PostEntity> postEntityMap) {
        for (PostEntity postEntityFromDb : postDao.findAll()) {
            String originKey = generateKey(postEntityFromDb);

            if(!postEntityMap.containsKey(originKey)) {
                postEntityFromDb.setDeleted(true);
            } else {
                PostEntity postEntity = postEntityMap.get(originKey);
                postEntityFromDb.setAbbreviatedName(postEntity.getAbbreviatedName());
                postEntityFromDb.setName(postEntity.getName());
                postEntityFromDb.setCode(postEntity.getCode());
                postEntityFromDb.setRang(postEntity.getRang());
                postEntityFromDb.setType(postEntity.getType());
                postEntityFromDb.setExternalId(postEntity.getExternalId());
                postEntityFromDb.setDeleted(false);
            }

            postEntityMap.put(originKey, postEntityFromDb);
        }
    }

    private String generateKey(PostEntity entity) {
        if (entity == null) {
            return null;
        }

        return StringUtils.hasText(entity.getExternalId())
                ? entity.getExternalId()
                : entity.getName() + entity.getCode() +
                Optional.ofNullable(entity.getRang())
                        .map(Object::toString).orElse("") +
                Optional.ofNullable(entity.getType())
                        .map(TypePostEnum::getName).orElse("");
    }

    private String generateKey(EmployeeEntity entity) {
        if (entity == null) {
            return null;
        }

        return Optional.ofNullable(entity.getPersonnelNumber())
                .map(String::valueOf)
                .orElse(null);
    }

    private String generateKey(DepartmentEntity entity) {
        if (entity == null) {
            return null;
        }

        return StringUtils.hasText(entity.getExternalId())
                ? entity.getExternalId()
                : entity.getAbbreviatedName();
    }
}
