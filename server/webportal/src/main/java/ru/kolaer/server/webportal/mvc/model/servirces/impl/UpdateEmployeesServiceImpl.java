package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.exception.ServerException;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeesDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PassportEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdateEmployeesService;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
@Slf4j
public class UpdateEmployeesServiceImpl implements UpdateEmployeesService {
    private static int YEAR_NOT_DISMISSAL = 9999;
    private static String SECOND_NAME = "Фамилия";
    private static String FIRST_NAME = "Имя";
    private static String THIRD_NAME = "Отчество";
    private static String SEX = "Пол";
    private static String DEP_ID = "Подразделение";
    private static String DEP_NAME = "Текст Подразделение";
    private static String POST_CODE = "Штатная должность(Код)";
    private static String POST_NAME = "Штатная должность(Название)";
    private static String EMPLOYMENT_DATE = "Поступл.";
    private static String DISMISSAL_DATE = "Дата увольнения";
    private static String BIRTHDAY_DATE = "ДатаРожд";
    private static String PERSONNEL_NUMBER = "Таб.№";
    private static String PHONE_NUMBER = "Телефон";
    private static String EMAIL = "Эл. почта(MAIL)";
    private static String SERIAL_2_DOCUMENT = "Серия2";
    private static String SERIAL_1_DOCUMENT = "Серия1";
    private static String NUMBER_DOCUMENT = "Номер документа";

    private EmployeeDao employeeDao;
    private PostDao postDao;
    private DepartmentDao departmentDao;

    public UpdateEmployeesServiceImpl(EmployeeDao employeeDao,
                                      PostDao postDao,
                                      DepartmentDao departmentDao) {
        this.employeeDao = employeeDao;
        this.postDao = postDao;
        this.departmentDao = departmentDao;
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
        List<String> messages = new ArrayList<>();
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

                EmployeeEntity newEmployeeEntity = null;
                DepartmentEntity departmentEntity = null;
                PostEntity postEntity = null;
                try {
                    newEmployeeEntity = readEmployee(row, nameColumns);
                    if(newEmployeesMap.containsKey(generateKey(newEmployeeEntity))) {
                        continue;
                    }
                    departmentEntity = readDepartment(row, nameColumns);
                    postEntity = readPost(row, nameColumns);
                } catch (Exception ex) {
                    messages.add(generateMessage(row, ex));
                }

                if(newEmployeeEntity == null || departmentEntity == null || postEntity == null) {
                    continue;
                }

                log.debug("Read post: {}", postEntity);
                log.debug("Read dep: {}", departmentEntity);
                log.debug("Read entity: {}", newEmployeeEntity);

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

            resultUpdateEmployeesDto = saveDepartment(departmentEntityMap);
            resultUpdateEmployeesDto = savePost(postEntityMap);
            resultUpdateEmployeesDto = saveEmployees(newEmployeesMap, employeeKeyDepMap, employeeKeyPostMap, departmentEntityMap, postEntityMap);

            return resultUpdateEmployeesDto;

        } catch (Exception e) {
            log.error("Ошибка при чтении файла!", e);
            throw new ServerException("Невозможно прочитать файл!");
        }
    }

    private String generateMessage(XSSFRow row, Exception ex) {
        String message = "Невозможно прочитать строку :" + row.getRowNum()
                + ". Сообщение ошибки: " + ex.getLocalizedMessage();
        log.error(message, ex);
        return message;
    }

    private ResultUpdateEmployeesDto saveEmployees(Map<String, EmployeeEntity> newEmployeesMap,
                                                   Map<String, String> departmentEntityMap,
                                                   Map<String, String> postEntityMap,
                                                   Map<String, DepartmentEntity> depMap,
                                                   Map<String, PostEntity> postMap) {

        /*List<EmployeeEntity> employeeEntityWorkPhone = jdbcTemplateKolaerBase
                .query("SELECT person_number, phone, mobile_phone, email FROM db_data_all",
                        (rs, rowNum) -> {
                            EmployeeEntity employeeDto = new EmployeeEntity();
                            String workPhone = Optional.ofNullable(rs.getString("phone")).orElse("");
                            String mobilePhone = Optional.ofNullable(rs.getString("mobile_phone")).orElse("");
                            if (StringUtils.hasText(mobilePhone))
                                workPhone += "; " + mobilePhone;

                            employeeDto.setEmail(rs.getString("email"));
                            employeeDto.setPersonnelNumber(rs.getLong("person_number"));
                            employeeDto.setWorkPhoneNumber(workPhone);
                            return employeeDto;
                        });

        employeeEntityWorkPhone.forEach(empBase -> {
            EmployeeEntity employeeEntity = newEmployeesMap.get(17240000 + empBase.getPersonnelNumber());
            if(employeeEntity != null) {
                employeeEntity.setEmail(empBase.getEmail());
                employeeEntity.setWorkPhoneNumber(empBase.getWorkPhoneNumber());
            }
        });*/

        for (Map.Entry<String, EmployeeEntity> employeeEntityEntry : newEmployeesMap.entrySet()) {
            String employeeKey = employeeEntityEntry.getKey();
            EmployeeEntity employeeEntity = employeeEntityEntry.getValue();

            String postKey = postEntityMap.get(employeeKey);
            String depKey = departmentEntityMap.get(employeeKey);
            employeeEntity.setPostId(postMap.get(postKey).getId());
            employeeEntity.setDepartmentId(depMap.get(depKey).getId());
        }

        employeeDao.save(newEmployeesMap.values().stream().collect(Collectors.toList()));

        List<DepartmentEntity> updateChifDep = new ArrayList<>();

        for (Map.Entry<String, EmployeeEntity> employeeEntityEntry : newEmployeesMap.entrySet()) {
            String employeeKey = employeeEntityEntry.getKey();
            EmployeeEntity employeeEntity = employeeEntityEntry.getValue();

            String postKey = postEntityMap.get(employeeKey);
            String depKey = departmentEntityMap.get(employeeKey);
            employeeEntity.setPostId(postMap.get(postKey).getId());

            DepartmentEntity departmentEntity = depMap.get(depKey);
            PostEntity postEntity = postMap.get(postKey);

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
                employeeEntity.setId(employeeEntityFromDb.getId());

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

    private PassportEntity readPassport(XSSFRow row, List<String> nameColumns) {
        String serialFirst = getStringValue(nameColumns, SERIAL_1_DOCUMENT, row);

        if (serialFirst != null && !serialFirst.trim().isEmpty()) {
            String serialSecond = getStringValue(nameColumns, SERIAL_2_DOCUMENT, row);
            String number = getStringValue(nameColumns, NUMBER_DOCUMENT, row);

            PassportEntity passportEntity = new PassportEntity();
            passportEntity.setSerial(serialFirst + serialSecond);
            passportEntity.setNumber(number);
            return passportEntity;
        }

        return null;
    }

    private PostEntity readPost(XSSFRow row, List<String> nameColumns) {
        PostEntity postEntity = new PostEntity();

        String value = getStringValue(nameColumns, POST_NAME, row);
        postEntity.setName(value);
        postEntity.setAbbreviatedName(value);

        String rang = value.replaceAll("[\\D]", "");
        if (!rang.trim().isEmpty()) {
            String name = value.substring(0, value.indexOf(rang));

            postEntity.setName(name.trim());
            postEntity.setRang(Integer.valueOf(rang));
            postEntity.setType(getTypePostFromName(value));
        }

        String postCode = getStringValue(nameColumns, POST_CODE, row);
        postEntity.setCode(postCode);

        return postEntity;
    }

    private TypePostEnum getTypePostFromName(String name) {
        Pattern compile = Pattern.compile("\\d(\\s.*)");
        Matcher matcher = compile.matcher(name);
        if(matcher.find()) {
            String group = matcher.group(1).trim();
            switch (group.charAt(0)) {
                case 'р': return TypePostEnum.DISCHARGE;
                case 'к': return TypePostEnum.CATEGORY;
                case 'г': return TypePostEnum.GROUP;
                default: return null;
            }
        }

        return null;
    }

    private DepartmentEntity readDepartment(XSSFRow row, List<String> nameColumns) {
        String value = getStringValue(nameColumns, DEP_ID, row);
        Long idDep = Optional.ofNullable(value).map(Long::valueOf).orElse(null);

        DepartmentEntity departmentEntity = new DepartmentEntity();
        //departmentEntity.setId(idDep);

        String depName = row.getCell(nameColumns.indexOf(DEP_NAME)).getStringCellValue();

        Pattern pattern = Pattern.compile("[а-яА-Я ]*");
        Matcher matcher = pattern.matcher(depName);

        departmentEntity.setName(depName);
        if (matcher.find())
            departmentEntity.setAbbreviatedName(matcher.group().trim());
        else
            departmentEntity.setAbbreviatedName(depName);

        return departmentEntity;
    }

    private EmployeeEntity readEmployee(XSSFRow row, List<String> nameColumns) {
        String value = getStringValue(nameColumns, PERSONNEL_NUMBER, row);

        Long pNumber = Long.valueOf(value);

        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        newEmployeeEntity.setPersonnelNumber(pNumber);

        value = getStringValue(nameColumns, FIRST_NAME, row);
        newEmployeeEntity.setFirstName(value);

        value = getStringValue(nameColumns, SECOND_NAME, row);
        newEmployeeEntity.setSecondName(value);

        value = getStringValue(nameColumns, THIRD_NAME, row);
        newEmployeeEntity.setThirdName(value);

        newEmployeeEntity.setInitials(newEmployeeEntity.getFirstName() + " "
                + newEmployeeEntity.getSecondName() + " "
                + newEmployeeEntity.getThirdName());

        Date date = getDateValue(nameColumns, BIRTHDAY_DATE, row);
        newEmployeeEntity.setBirthday(date);

        date = getDateValue(nameColumns, EMPLOYMENT_DATE, row);
        newEmployeeEntity.setEmploymentDate(date);

        date = getDateValue(nameColumns, DISMISSAL_DATE, row);
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (calendar.get(Calendar.YEAR) != YEAR_NOT_DISMISSAL) {
                newEmployeeEntity.setDismissalDate(date);
            }
        }

        value = getStringValue(nameColumns, SEX, row);
        if("мужской".equals(value.toLowerCase())) {
            newEmployeeEntity.setGender(EnumGender.MALE);
        } else {
            newEmployeeEntity.setGender(EnumGender.FEMALE);
        }

        value = getStringValue(nameColumns, PHONE_NUMBER, row);
        newEmployeeEntity.setHomePhoneNumber(value);

        value = getStringValue(nameColumns, EMAIL, row);
        newEmployeeEntity.setEmail(value);

        try {
            newEmployeeEntity.setPhoto("http://asupkolaer.local/app_ie8/assets/images/vCard/o_"
                    + URLEncoder.encode(newEmployeeEntity.getInitials(), "UTF-8")
                    .replace("+", "%20") + ".jpg");
        } catch (UnsupportedEncodingException ignore) {
            log.warn("User initials can't encode to UTF-8", newEmployeeEntity.getInitials());
        }

        return newEmployeeEntity;
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

    private String getStringValue(List<String> columns, String nameColumns, XSSFRow row) {
        return Optional.ofNullable(getCell(columns, nameColumns, row))
                .map(XSSFCell::getStringCellValue)
                .orElse(null);
    }

    private Date getDateValue(List<String> columns, String nameColumns, XSSFRow row) {
        return Optional.ofNullable(getCell(columns, nameColumns, row))
                .map(XSSFCell::getDateCellValue)
                .orElse(null);
    }

    private XSSFCell getCell(List<String> columns, String nameColumns, XSSFRow row) {
        int indexColumn = columns.indexOf(nameColumns);
        if(indexColumn > -1) {
            return row.getCell(indexColumn);
        } else {
            log.warn("Column: {} not found!", nameColumns);
        }

        return null;
    }
}
