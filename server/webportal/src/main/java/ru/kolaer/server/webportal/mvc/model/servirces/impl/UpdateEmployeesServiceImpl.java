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
import ru.kolaer.server.webportal.mvc.model.dto.HistoryChangeDto;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeeDto;
import ru.kolaer.server.webportal.mvc.model.dto.UpdatableElement;
import ru.kolaer.server.webportal.mvc.model.dto.UpdatableEmployee;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEvent;
import ru.kolaer.server.webportal.mvc.model.servirces.HistoryChangeService;
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

    private final HistoryChangeService historyChangeService;
    private final List<UpdatableEmployeeService> updatableEmployeeServices;

    private final ExcelReaderEmployee excelReaderEmployee;
    private final ExcelReaderDepartment excelReaderDepartment;
    private final ExcelReaderPost excelReaderPost;

    public UpdateEmployeesServiceImpl(
            EmployeeDao employeeDao,
            EmployeeConverter employeeConverter,
            PostDao postDao,
            DepartmentDao departmentDao,
            HistoryChangeService historyChangeService,
            ExcelReaderEmployee excelReaderEmployee,
            ExcelReaderDepartment excelReaderDepartment,
            ExcelReaderPost excelReaderPost,
            @Autowired(required = false) List<UpdatableEmployeeService> updatableEmployeeServices) {
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
        this.postDao = postDao;
        this.departmentDao = departmentDao;

        this.historyChangeService = historyChangeService;

        this.updatableEmployeeServices = updatableEmployeeServices;

        this.excelReaderEmployee = excelReaderEmployee;
        this.excelReaderDepartment = excelReaderDepartment;
        this.excelReaderPost = excelReaderPost;
    }

    @Override
    @Transactional
    public List<HistoryChangeDto> updateEmployees(@NonNull File file) {
        try {
            return updateEmployees(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден!", e);
        }
    }

    @Override
    @Transactional
    public List<HistoryChangeDto> updateEmployees(InputStream inputStream) {
        historyChangeService.createHistoryChange(HistoryChangeEvent.UPLOAD_FILE_TO_UPDATE_EMPLOYEE);

        List<HistoryChangeDto> changes = new ArrayList<>();

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

            Map<String, UpdatableElement<PostEntity>> postEntityMap = new HashMap<>();
            Map<String, UpdatableElement<DepartmentEntity>> departmentEntityMap = new HashMap<>();
            Map<String, UpdatableElement<UpdatableEmployee>> newEmployeesMap = new HashMap<>();

            for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
                XSSFRow row = sheetAt.getRow(i);

                EmployeeEntity newEmployeeEntity = excelReaderEmployee.process(row, nameColumns);

                UpdatableElement<UpdatableEmployee> updatableEmployee;

                String employeeKey = generateKey(newEmployeeEntity);
                if (employeeKey != null && !newEmployeesMap.containsKey(employeeKey)) {
                    updatableEmployee = new UpdatableElement<>(new UpdatableEmployee(newEmployeeEntity));
                    newEmployeesMap.put(employeeKey, updatableEmployee);
                } else {
                    continue;
                }

                DepartmentEntity departmentEntity = excelReaderDepartment.process(row, nameColumns);
                PostEntity postEntity = excelReaderPost.parse(row, nameColumns);

                String postKey = generateKey(postEntity);
                String depKey = generateKey(departmentEntity);

                if (postKey != null && !postEntityMap.containsKey(postKey)) {
                    postEntityMap.put(postKey, new UpdatableElement<>(postEntity));
                }

                if (depKey != null && !departmentEntityMap.containsKey(depKey)) {
                    departmentEntityMap.put(depKey, new UpdatableElement<>(departmentEntity));
                }

                updatableEmployee.getElement().setDepartmentKey(depKey);
                updatableEmployee.getElement().setPostKey(postKey);
            }

            changes.addAll(updatePostMap(postEntityMap));
            changes.addAll(updateDepMap(departmentEntityMap));
            changes.addAll(updateEmployeeMap(newEmployeesMap,
                    resultUpdateEmployeeDto,
                    saveDepartment(departmentEntityMap),
                    savePost(postEntityMap)));

            saveEmployees(newEmployeesMap);
        } catch (Exception ex) {
            log.error("Ошибка при чтении файла!", ex);
            throw new UnexpectedRequestParams(ex.getMessage(), ex, ErrorCode.PARSE_EXCEPTION);
        }

//        ResultUpdate resultUpdate = new ResultUpdate();
//        resultUpdate.setAddEmployee(employeeConverter.convertToDto(resultUpdateEmployeeDto.getAddEmployee()));
//        resultUpdate.setDeleteEmployee(employeeConverter.convertToDto(resultUpdateEmployeeDto.getDeleteEmployee()));
//
//        Optional.ofNullable(updatableEmployeeServices)
//                .orElse(Collections.emptyList())
//                .stream()
//                .sorted(Comparator.comparingInt(UpdatableEmployeeService::getOrder))
//                .forEach(service -> service.updateEmployee(resultUpdate));

        return changes;
    }

    private void saveEmployees(Map<String, UpdatableElement<UpdatableEmployee>> newEmployeesMap) {
        List<EmployeeEntity> employeeEntities = newEmployeesMap.values()
                .stream()
                .filter(updatable -> updatable.getElement().getEmployee().getId() == null ||
                        updatable.isUpdate() ||
                        updatable.isDelete())
                .map(UpdatableElement::getElement)
                .map(UpdatableEmployee::getEmployee)
                .collect(Collectors.toList());

        employeeDao.save(employeeEntities);
    }

    private Map<String, UpdatableElement<PostEntity>> savePost(Map<String, UpdatableElement<PostEntity>> postEntityMap) {
        List<PostEntity> departmentEntities = postEntityMap.values()
                .stream()
                .filter(updatable -> updatable.getElement().getId() == null ||
                        updatable.isUpdate() ||
                        updatable.isDelete())
                .map(UpdatableElement::getElement)
                .collect(Collectors.toList());

       postDao.save(departmentEntities);

        return postEntityMap;
    }

    private Map<String, UpdatableElement<DepartmentEntity>> saveDepartment(Map<String, UpdatableElement<DepartmentEntity>> departmentEntityMap) {
        List<DepartmentEntity> departmentEntities = departmentEntityMap.values()
                .stream()
                .filter(updatable -> updatable.getElement().getId() == null ||
                        updatable.isUpdate() ||
                        updatable.isDelete())
                .map(UpdatableElement::getElement)
                .collect(Collectors.toList());

        departmentDao.save(departmentEntities);

        return departmentEntityMap;
    }

    private List<HistoryChangeDto> updateEmployeeMap(Map<String, UpdatableElement<UpdatableEmployee>> newEmployeesMap,
                                                     ResultUpdateEmployeeDto resultUpdateEmployeeDto,
                                                     Map<String, UpdatableElement<DepartmentEntity>> depMap,
                                                     Map<String, UpdatableElement<PostEntity>> postMap) {
        List<HistoryChangeDto> histories = new ArrayList<>();

        List<EmployeeEntity> deletedEmployee = new ArrayList<>();

        for (EmployeeEntity employeeEntityFromDb : employeeDao.findAll()) {
            String originKey = generateKey(employeeEntityFromDb);

            UpdatableElement<UpdatableEmployee> updatableElement = newEmployeesMap
                    .getOrDefault(originKey, new UpdatableElement<>(new UpdatableEmployee(employeeEntityFromDb)));

            if(!newEmployeesMap.containsKey(originKey)) {
                if(employeeEntityFromDb.getDismissalDate() == null) {
                    employeeEntityFromDb.setDismissalDate(new Date());
                    deletedEmployee.add(employeeEntityFromDb);

                    HistoryChangeDto historyChange = historyChangeService
                            .createHistoryChange(null, employeeEntityFromDb.toString(), HistoryChangeEvent.HIDE_EMPLOYEE);
                    histories.add(historyChange);
                }
            } else {
                UpdatableEmployee updatableEmployee = updatableElement.getElement();
                EmployeeEntity employeeEntity = updatableEmployee.getEmployee();

                String valueOld = employeeEntityFromDb.toString();

                Long postId = Optional.ofNullable(postMap.get(updatableEmployee.getPostKey()))
                        .map(UpdatableElement::getElement)
                        .map(PostEntity::getId)
                        .orElseThrow(() -> new UnexpectedRequestParams("Не найдена должность для: " + employeeEntity.getPersonnelNumber(), employeeEntity));
                Long departmentId = Optional.ofNullable(depMap.get(updatableEmployee.getDepartmentKey()))
                        .map(UpdatableElement::getElement)
                        .map(DepartmentEntity::getId)
                        .orElseThrow(() -> new UnexpectedRequestParams("Не найдено подразделение для: " + employeeEntity.getPersonnelNumber(), employeeEntity));

                if(!Objects.equals(employeeEntityFromDb.getInitials(),employeeEntity.getInitials()) ||
                        !Objects.equals(employeeEntityFromDb.getFirstName(),employeeEntity.getFirstName()) ||
                        !Objects.equals(employeeEntityFromDb.getSecondName(),employeeEntity.getSecondName()) ||
                        !Objects.equals(employeeEntityFromDb.getThirdName(),employeeEntity.getThirdName()) ||
                        !Objects.equals(employeeEntityFromDb.getGender(),employeeEntity.getGender()) ||
                        !Objects.equals(employeeEntityFromDb.getBirthday(),employeeEntity.getBirthday()) ||
                        !Objects.equals(employeeEntityFromDb.getDepartmentId(), departmentId) ||
                        !Objects.equals(employeeEntityFromDb.getPostId(), postId) ||
                        !Objects.equals(employeeEntityFromDb.getCategory(),employeeEntity.getCategory()) ||
                        !Objects.equals(employeeEntityFromDb.getEmploymentDate(),employeeEntity.getEmploymentDate()) ||
                        employeeEntityFromDb.getDismissalDate() != null) {
                    updatableElement.setUpdate(true);
                }

                employeeEntityFromDb.setBirthday(employeeEntity.getBirthday());
                employeeEntityFromDb.setGender(employeeEntity.getGender());
                employeeEntityFromDb.setInitials(employeeEntity.getInitials());
                employeeEntityFromDb.setFirstName(employeeEntity.getFirstName());
                employeeEntityFromDb.setSecondName(employeeEntity.getSecondName());
                employeeEntityFromDb.setThirdName(employeeEntity.getThirdName());
                employeeEntityFromDb.setEmploymentDate(employeeEntity.getEmploymentDate());
                employeeEntityFromDb.setCategory(employeeEntity.getCategory());
                employeeEntityFromDb.setPostId(postId);
                employeeEntityFromDb.setDepartmentId(departmentId);
                employeeEntityFromDb.setDismissalDate(null);

                if(updatableElement.isUpdate()) {
                    HistoryChangeDto historyChange = historyChangeService
                            .createHistoryChange(valueOld, employeeEntityFromDb.toString(), HistoryChangeEvent.UPDATE_EMPLOYEE);
                    histories.add(historyChange);
                }
            }
            updatableElement.getElement().setEmployee(employeeEntityFromDb);
            newEmployeesMap.put(originKey, updatableElement);
        }

        List<EmployeeEntity> addedEmployee = newEmployeesMap.values()
                .stream()
                .map(UpdatableElement::getElement)
                .map(UpdatableEmployee::getEmployee)
                .filter(employee -> employee.getId() == null && employee.getDismissalDate() == null)
                .collect(Collectors.toList());

        resultUpdateEmployeeDto.setAddEmployee(addedEmployee);
        resultUpdateEmployeeDto.setDeleteEmployee(deletedEmployee);

        List<HistoryChangeDto> historiesForAdd = newEmployeesMap.values()
                .stream()
                .map(UpdatableElement::getElement)
                .map(UpdatableEmployee::getEmployee)
                .filter(employee -> employee.getId() == null)
                .map(employee -> historyChangeService
                        .createHistoryChange(null, employee.toString(), HistoryChangeEvent.ADD_EMPLOYEE))
                .collect(Collectors.toList());

        histories.addAll(historiesForAdd);

        return histories;
    }

    private List<HistoryChangeDto> updateDepMap(Map<String, UpdatableElement<DepartmentEntity>> departmentEntityMap) {
        List<HistoryChangeDto> histories = new ArrayList<>();

        for (DepartmentEntity depEntityFromDb : departmentDao.findAll()) {
            String originKey = generateKey(depEntityFromDb);

            UpdatableElement<DepartmentEntity> updatableElement = departmentEntityMap
                    .getOrDefault(originKey, new UpdatableElement<>(depEntityFromDb));

            if(!departmentEntityMap.containsKey(originKey)) {
                if(!depEntityFromDb.isDeleted()) {
                    depEntityFromDb.setDeleted(true);

                    HistoryChangeDto historyChange = historyChangeService
                            .createHistoryChange(null, depEntityFromDb.toString(), HistoryChangeEvent.HIDE_DEPARTMENT);
                    histories.add(historyChange);
                }
            } else {
                DepartmentEntity departmentEntity = updatableElement.getElement();

                String valueOld = depEntityFromDb.toString();

                if(!Objects.equals(depEntityFromDb.getAbbreviatedName(), departmentEntity.getAbbreviatedName()) ||
                        !Objects.equals(depEntityFromDb.getName(), departmentEntity.getName()) ||
                        !Objects.equals(depEntityFromDb.getExternalId(), departmentEntity.getExternalId()) ||
                        depEntityFromDb.isDeleted()) {
                    updatableElement.setUpdate(true);
                }

                depEntityFromDb.setAbbreviatedName(departmentEntity.getAbbreviatedName());
                depEntityFromDb.setName(departmentEntity.getName());
                depEntityFromDb.setExternalId(departmentEntity.getExternalId());
                depEntityFromDb.setDeleted(false);

                if(updatableElement.isUpdate()) {
                    HistoryChangeDto historyChange = historyChangeService
                            .createHistoryChange(valueOld, depEntityFromDb.toString(), HistoryChangeEvent.UPDATE_DEPARTMENT);
                    histories.add(historyChange);
                }
            }
            updatableElement.setElement(depEntityFromDb);
            departmentEntityMap.put(originKey, updatableElement);
        }

        List<HistoryChangeDto> historiesForAdd = departmentEntityMap.values()
                .stream()
                .map(UpdatableElement::getElement)
                .filter(department -> department.getId() == null)
                .map(department -> historyChangeService
                        .createHistoryChange(null, department.toString(), HistoryChangeEvent.ADD_DEPARTMENT))
                .collect(Collectors.toList());

        histories.addAll(historiesForAdd);

        return histories;
    }

    private List<HistoryChangeDto> updatePostMap(Map<String, UpdatableElement<PostEntity>> postEntityMap) {
        List<HistoryChangeDto> histories = new ArrayList<>();

        for (PostEntity postEntityFromDb : postDao.findAll()) {
            String originKey = generateKey(postEntityFromDb);

            UpdatableElement<PostEntity> updatableElement = postEntityMap
                    .getOrDefault(originKey, new UpdatableElement<>(postEntityFromDb));

            if(!postEntityMap.containsKey(originKey)) {
                if(!postEntityFromDb.isDeleted()) {
                    postEntityFromDb.setDeleted(true);

                    HistoryChangeDto historyChange = historyChangeService
                            .createHistoryChange(null, postEntityFromDb.toString(), HistoryChangeEvent.HIDE_POST);
                    histories.add(historyChange);

                    updatableElement.setDelete(true);
                }
            } else {
                PostEntity postEntity = updatableElement.getElement();

                String valueOld = postEntityFromDb.toString();

                if(!Objects.equals(postEntityFromDb.getAbbreviatedName(), postEntity.getAbbreviatedName()) ||
                        !Objects.equals(postEntityFromDb.getName(), postEntity.getName()) ||
                        !Objects.equals(postEntityFromDb.getCode(), postEntity.getCode()) ||
                        !Objects.equals(postEntityFromDb.getExternalId(), postEntity.getExternalId()) ||
                        !Objects.equals(postEntityFromDb.getRang(), postEntity.getRang()) ||
                        !Objects.equals(postEntityFromDb.getType(), postEntity.getType()) ||
                        postEntityFromDb.isDeleted()) {
                    updatableElement.setUpdate(true);
                }

                postEntityFromDb.setDeleted(false);
                postEntityFromDb.setType(postEntity.getType());
                postEntityFromDb.setRang(postEntity.getRang());
                postEntityFromDb.setName(postEntity.getName());
                postEntityFromDb.setAbbreviatedName(postEntity.getAbbreviatedName());
                postEntityFromDb.setCode(postEntity.getCode());
                postEntityFromDb.setExternalId(postEntity.getExternalId());

                if(updatableElement.isUpdate()) {
                    HistoryChangeDto historyChange = historyChangeService
                            .createHistoryChange(valueOld, postEntityFromDb.toString(), HistoryChangeEvent.UPDATE_POST);
                    histories.add(historyChange);
                }
            }

            updatableElement.setElement(postEntityFromDb);
            postEntityMap.put(originKey, updatableElement);
        }

        List<HistoryChangeDto> historiesForAdd = postEntityMap.values()
                .stream()
                .map(UpdatableElement::getElement)
                .filter(post -> post.getId() == null)
                .map(post -> historyChangeService
                        .createHistoryChange(null, post.toString(), HistoryChangeEvent.ADD_POST))
                .collect(Collectors.toList());

        histories.addAll(historiesForAdd);

        return histories;
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
