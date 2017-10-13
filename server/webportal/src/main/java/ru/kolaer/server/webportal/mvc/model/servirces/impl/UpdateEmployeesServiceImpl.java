package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeesDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdateEmployeesService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
@Slf4j
public class UpdateEmployeesServiceImpl implements UpdateEmployeesService {

    private final JdbcTemplate jdbcTemplateKolaerBase;

    private final EmployeeDao employeeDao;
    private final PostDao postDao;
    private final DepartmentDao departmentDao;
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;

    private final ExcelReaderEmployee excelReaderEmployee;
    private final ExcelReaderDepartment excelReaderDepartment;
    private final ExcelReaderPost excelReaderPost;

    public UpdateEmployeesServiceImpl(@Qualifier("dataSourceKolaerBase") JdbcTemplate jdbcTemplateKolaerBase,
                                      EmployeeDao employeeDao,
                                      PostDao postDao,
                                      DepartmentDao departmentDao,
                                      AccountDao accountDao,
                                      AccountConverter accountConverter,
                                      ExcelReaderEmployee excelReaderEmployee,
                                      ExcelReaderDepartment excelReaderDepartment,
                                      ExcelReaderPost excelReaderPost) {
        this.jdbcTemplateKolaerBase = jdbcTemplateKolaerBase;
        this.employeeDao = employeeDao;
        this.postDao = postDao;
        this.departmentDao = departmentDao;
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
        this.excelReaderEmployee = excelReaderEmployee;
        this.excelReaderDepartment = excelReaderDepartment;
        this.excelReaderPost = excelReaderPost;
    }

    @Override
    @Transactional
    public ResultUpdateEmployeesDto updateEmployees(@NonNull File file) {
        try {
            return updateEmployees(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден!", e);
        }
    }

    @Override
    @Transactional
    public ResultUpdateEmployeesDto updateEmployees(InputStream inputStream) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheetAt = workbook.getSheetAt(0);
            XSSFRow firstRow = sheetAt.getRow(sheetAt.getFirstRowNum());

            List<String> nameColumns = new ArrayList<>();
            firstRow.forEach(cell -> nameColumns.add(cell.getStringCellValue()));

            Map<String, PostEntity> postEntityMap = new HashMap<>();
            Map<String, DepartmentEntity> departmentEntityMap = new HashMap<>();
            Map<String, EmployeeEntity> newEmployeesMap = new HashMap<>();
            Map<String, String> employeeKeyPostMap = new HashMap<>();
            Map<String, String> employeeKeyDepMap = new HashMap<>();

            for (int i = 1; i < sheetAt.getLastRowNum(); i++) {
                XSSFRow row = sheetAt.getRow(i);

                EmployeeEntity newEmployeeEntity = excelReaderEmployee.process(row, nameColumns);
                if(newEmployeesMap.containsKey(generateKey(newEmployeeEntity))) {
                    continue;
                }

                DepartmentEntity departmentEntity = excelReaderDepartment.process(row, nameColumns);;
                PostEntity postEntity = excelReaderPost.parse(row, nameColumns);;

                String postKey = generateKey(postEntity);
                String depKey = generateKey(departmentEntity);
                String employeeKey = generateKey(newEmployeeEntity);

                if (!postEntityMap.containsKey(postKey)) {
                    postEntityMap.put(postKey, postEntity);
                }

                if (!departmentEntityMap.containsKey(depKey)) {
                    departmentEntityMap.put(depKey, departmentEntity);
                }

                if (!newEmployeesMap.containsKey(employeeKey)) {
                    newEmployeesMap.put(employeeKey, newEmployeeEntity);
                }

                employeeKeyPostMap.put(employeeKey, postKey);
                employeeKeyDepMap.put(employeeKey, depKey);
            }

            ResultUpdateEmployeesDto resultUpdateEmployeesDto = new ResultUpdateEmployeesDto();
            resultUpdateEmployeesDto = updatePostMap(postEntityMap, resultUpdateEmployeesDto);
            resultUpdateEmployeesDto = updateDepMap(departmentEntityMap, resultUpdateEmployeesDto);
            resultUpdateEmployeesDto = updateEmployeeMap(newEmployeesMap, resultUpdateEmployeesDto);

            saveDepartment(departmentEntityMap);
            savePost(postEntityMap);
            saveEmployees(newEmployeesMap, employeeKeyDepMap, employeeKeyPostMap, departmentEntityMap, postEntityMap);

            saveAccounts(resultUpdateEmployeesDto.getAddEmployee());

            return resultUpdateEmployeesDto;
        } catch (Exception ex) {
            log.error("Ошибка при чтении файла!", ex);
            throw new UnexpectedRequestParams(ex.getMessage(), ex, ErrorCode.PARSE_EXCEPTION);
        }
    }

    private void saveAccounts(List<EmployeeEntity> employee) {
        List<AccountEntity> accounts = employee
                .stream()
                .map(accountConverter::convertToModel)
                .collect(Collectors.toList());

        accountDao.save(accounts);
    }

    private ResultUpdateEmployeesDto saveEmployees(Map<String, EmployeeEntity> newEmployeesMap,
                                                   Map<String, String> employeeKeyDepMap,
                                                   Map<String, String> employeeKeyPostMap,
                                                   Map<String, DepartmentEntity> depMap,
                                                   Map<String, PostEntity> postMap) {

        Map<String, EmployeeEntity> kolaerBaseEmployeeMap = jdbcTemplateKolaerBase
                .query("SELECT person_number, phone, mobile_phone, email FROM db_data_all", (rs, rowNum) -> {
                    EmployeeEntity employeeEntity = new EmployeeEntity();
                    String workPhone = Optional.ofNullable(rs.getString("phone")).orElse("");
                    String mobilePhone = Optional.ofNullable(rs.getString("mobile_phone")).orElse("");
                    if (StringUtils.hasText(mobilePhone))
                        workPhone += "; " + mobilePhone;

                    employeeEntity.setEmail(rs.getString("email"));
                    employeeEntity.setPersonnelNumber(rs.getLong("person_number"));
                    employeeEntity.setWorkPhoneNumber(workPhone);
                    return employeeEntity;
                }).stream()
                .collect(Collectors.toMap(e -> String.valueOf(17240000L + e.getPersonnelNumber()), e -> e));

        for (Map.Entry<String, EmployeeEntity> employeeEntityEntry : newEmployeesMap.entrySet()) {
            String employeeKey = employeeEntityEntry.getKey();
            EmployeeEntity employeeEntity = employeeEntityEntry.getValue();

            String postKey = employeeKeyPostMap.get(employeeKey);
            String depKey = employeeKeyDepMap.get(employeeKey);

            Optional.ofNullable(postMap.get(postKey))
                    .map(PostEntity::getId)
                    .ifPresent(employeeEntity::setPostId);

            Optional.ofNullable(depMap.get(depKey))
                    .map(DepartmentEntity::getId)
                    .ifPresent(employeeEntity::setDepartmentId);

            EmployeeEntity kolaerBaseEntity = kolaerBaseEmployeeMap.get(employeeKey);
            if(kolaerBaseEntity != null) {
                employeeEntity.setEmail(kolaerBaseEntity.getEmail());
                employeeEntity.setWorkPhoneNumber(kolaerBaseEntity.getWorkPhoneNumber());
            }
        }

        employeeDao.save(newEmployeesMap.values().stream().collect(Collectors.toList()));

        List<DepartmentEntity> updateChifDep = new ArrayList<>();

        for (Map.Entry<String, EmployeeEntity> employeeEntityEntry : newEmployeesMap.entrySet()) {
            String employeeKey = employeeEntityEntry.getKey();
            EmployeeEntity employeeEntity = employeeEntityEntry.getValue();

            String postKey = employeeKeyPostMap.get(employeeKey);
            String depKey = employeeKeyDepMap.get(employeeKey);

            DepartmentEntity departmentEntity = depMap.get(depKey);
            PostEntity postEntity = postMap.get(postKey);
            if(departmentEntity != null && postEntity != null) {
                String postName = postEntity.getName();
                Long idChief = departmentEntity.getChiefEmployeeId();
                if ((postName.contains("Начальник") || postName.contains("Директор")
                        || postName.contains("Руководитель") || postName.contains("Ведущий")
                        || postName.contains("Главный"))
                        && (idChief == null || !idChief.equals(employeeEntity.getId()))) {
                    departmentEntity.setChiefEmployeeId(employeeEntity.getId());
                    updateChifDep.add(departmentEntity);
                }
            }
        }

        departmentDao.save(updateChifDep);

        return null;
    }

    private ResultUpdateEmployeesDto savePost(Map<String, PostEntity> postEntityMap) {
        postDao.save(postEntityMap.values().stream().collect(Collectors.toList()));

        return null;
    }

    private ResultUpdateEmployeesDto saveDepartment(Map<String, DepartmentEntity> departmentEntityMap) {
        departmentDao.save(departmentEntityMap.values().stream().collect(Collectors.toList()));

        return null;
    }

    private ResultUpdateEmployeesDto updateEmployeeMap(Map<String, EmployeeEntity> newEmployeesMap,
                                                       ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        List<EmployeeEntity> deletedEmployee = new ArrayList<>();

        for (EmployeeEntity employeeEntityFromDb : employeeDao.findAll()) {
            String originKey = generateKey(employeeEntityFromDb);

            if(!newEmployeesMap.containsKey(originKey)) {
                employeeEntityFromDb.setDismissalDate(new Date());
                deletedEmployee.add(employeeEntityFromDb);
            } else {
                EmployeeEntity employeeEntity = newEmployeesMap.get(originKey);
                employeeEntityFromDb.setInitials(employeeEntity.getInitials());
                employeeEntityFromDb.setFirstName(employeeEntity.getFirstName());
                employeeEntityFromDb.setSecondName(employeeEntity.getSecondName());
                employeeEntityFromDb.setThirdName(employeeEntity.getThirdName());
                employeeEntityFromDb.setGender(employeeEntity.getGender());
                employeeEntityFromDb.setBirthday(employeeEntity.getBirthday());
                employeeEntityFromDb.setHomePhoneNumber(employeeEntity.getHomePhoneNumber());
                employeeEntityFromDb.setEmploymentDate(employeeEntity.getEmploymentDate());
            }

            newEmployeesMap.put(originKey, employeeEntityFromDb);
        }

        List<EmployeeEntity> addedEmployee = newEmployeesMap.values()
                .stream()
                .filter(post -> post.getId() == null && post.getDismissalDate() == null)
                .collect(Collectors.toList());

        resultUpdateEmployeesDto.setDeleteEmployee(deletedEmployee);
        resultUpdateEmployeesDto.setAddEmployee(addedEmployee);
        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto updateDepMap(Map<String, DepartmentEntity> departmentEntityMap,
                                                  ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        List<DepartmentEntity> deletedDep = new ArrayList<>();

        for (DepartmentEntity depEntityFromDb : departmentDao.findAll()) {
            String originKey = generateKey(depEntityFromDb);

            if(!departmentEntityMap.containsKey(originKey)) {
                depEntityFromDb.setDeleted(true);
                deletedDep.add(depEntityFromDb);
            } else {
                DepartmentEntity departmentEntity = departmentEntityMap.get(originKey);
                depEntityFromDb.setAbbreviatedName(departmentEntity.getAbbreviatedName());
                depEntityFromDb.setName(departmentEntity.getName());
                depEntityFromDb.setChiefEmployeeId(departmentEntity.getChiefEmployeeId());
            }

            departmentEntityMap.put(originKey, depEntityFromDb);
        }

        List<DepartmentEntity> addedPost = departmentEntityMap.values()
                .stream()
                .filter(post -> post.getId() == null && !post.isDeleted())
                .collect(Collectors.toList());

        resultUpdateEmployeesDto.setDeleteDep(deletedDep);
        resultUpdateEmployeesDto.setAddDep(addedPost);
        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto updatePostMap(Map<String, PostEntity> postEntityMap,
                                                   ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        List<PostEntity> deletedPost = new ArrayList<>();

        for (PostEntity postEntityFromDb : postDao.findAll()) {
            String originKey = generateKey(postEntityFromDb);

            if(!postEntityMap.containsKey(originKey)) {
                postEntityFromDb.setDeleted(true);
                deletedPost.add(postEntityFromDb);
            } else {
                PostEntity postEntity = postEntityMap.get(originKey);
                postEntityFromDb.setAbbreviatedName(postEntity.getAbbreviatedName());
                postEntityFromDb.setName(postEntity.getName());
                postEntityFromDb.setCode(postEntity.getCode());
                postEntityFromDb.setRang(postEntity.getRang());
                postEntityFromDb.setType(postEntity.getType());
                postEntityFromDb.setDeleted(false);
            }

            postEntityMap.put(originKey, postEntityFromDb);
        }


        List<PostEntity> addedPost = postEntityMap.values()
                .stream()
                .filter(post -> post.getId() == null && !post.isDeleted())
                .collect(Collectors.toList());

        resultUpdateEmployeesDto.setDeletePost(deletedPost);
        resultUpdateEmployeesDto.setAddPost(addedPost);
        return resultUpdateEmployeesDto;
    }

    private String generateKey(PostEntity entity) {
        if (entity == null) {
            return null;
        }

        return entity.getName() + entity.getCode() +
                Optional.ofNullable(entity.getRang())
                        .map(rang -> rang.toString() + Optional.ofNullable(entity.getType())
                                .map(TypePostEnum::getName)
                                .orElse(""))
                        .orElse("");
    }

    private String generateKey(EmployeeEntity entity) {
        if (entity == null) {
            return null;
        }

        return entity.getPersonnelNumber().toString();
    }

    private String generateKey(DepartmentEntity entity) {
        if (entity == null) {
            return null;
        }

        return entity.getAbbreviatedName();
    }
}
