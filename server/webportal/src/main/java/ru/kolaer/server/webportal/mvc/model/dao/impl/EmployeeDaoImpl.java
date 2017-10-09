package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeesDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PassportEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Danilov on 27.07.2016.
 *
 */
@Repository
@Slf4j
public class EmployeeDaoImpl extends AbstractDefaultDao<EmployeeEntity> implements EmployeeDao {
    private final static int YEAR_NOT_DISMISSAL = 9999;
    private final static String SECOND_NAME = "Фамилия";
    private final static String FIRST_NAME = "Имя";
    private final static String THIRD_NAME = "Отчество";
    private final static String SEX = "Пол";
    private final static String DEP_ID = "Подразделение";
    private final static String DEP_NAME = "Текст Подразделение";
    private final static String POST_CODE = "Штатная должность(Код)";
    private final static String POST_NAME = "Штатная должность(Название)";
    private final static String EMPLOYMENT_DATE = "Поступл.";
    private final static String DISMISSAL_DATE = "Дата увольнения";
    private final static String BIRTHDAY_DATE = "ДатаРожд";
    private final static String PERSONNEL_NUMBER = "Таб.№";
    private final static String PHONE_NUMBER = "Телефон";
    private final static String EMAIL = "Эл. почта(MAIL)";
    private final static String SERIAL_2_DOCUMENT = "Серия2";
    private final static String SERIAL_1_DOCUMENT = "Серия1";
    private final static String NUMBER_DOCUMENT = "Номер документа";

    private final JdbcTemplate jdbcTemplateKolaerBase;

    protected EmployeeDaoImpl(@Qualifier(value = "jdbcTemplateKolaerBase") JdbcTemplate jdbcTemplateKolaerBase,
                              SessionFactory sessionFactory) {
        super(sessionFactory, EmployeeEntity.class);
        this.jdbcTemplateKolaerBase = jdbcTemplateKolaerBase;
    }

    @Override
    public List<EmployeeEntity> findEmployeeByInitials(@NonNull String initials) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.initials LIKE :initials ORDER BY emp.initials", EmployeeEntity.class)
                .setParameter("initials", "%" + initials + "%")
                .list();
    }

    @Override
    public List<EmployeeEntity> findByDepartmentById(@NonNull Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.department.id = :id ORDER BY emp.initials", EmployeeEntity.class)
                .setParameter("id", id)
                .list();
    }

    @Override
    public List<EmployeeEntity> findByDepartmentById(int page, int pageSize, @NonNull Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.department.id = :id ORDER BY emp.initials",
                        EmployeeEntity.class)
                .setParameter("id", id)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public Long findCountByDepartmentById(Long id) {
        return getSession().createQuery("SELECT COUNT(emp.personnelNumber) FROM " + getEntityName() +
                " emp WHERE emp.department.id = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public EmployeeEntity findByPersonnelNumber(Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.personnelNumber = :id", EmployeeEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<EmployeeEntity> getUserRangeBirthday(@NonNull final Date startDate, @NonNull final Date endDate) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where t.birthday BETWEEN :startDate AND :endDate " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUsersByBirthday(@NonNull final Date date) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("date", date)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUserBirthdayToday() {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE) " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .list();
    }

    @Override
    public int getCountUserBirthday(@NonNull final Date date) {
        return getSession()
                .createQuery("SELECT count(t.personnelNumber) FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", Long.class)
                .setParameter("date", date)
                .uniqueResult()
                .intValue();
    }

    @Override
    public List<EmployeeEntity> getUsersByInitials(@NonNull final String initials) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                " t where t.initials like :initials ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("initials", "%" + initials + "%")
                .list();
    }

    @Override
    public ResultUpdateEmployeesDto updateEmployeesFromXlsx(@NonNull File file) {
        try {
            return this.updateEmployeesFromXlsx(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден!", e);
        }
    }

    @Override
    public ResultUpdateEmployeesDto updateEmployeesFromXlsx(InputStream inputStream) {
        try {
            final XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            final XSSFSheet sheetAt = workbook.getSheetAt(0);
            final XSSFRow firstRow = sheetAt.getRow(sheetAt.getFirstRowNum());
            final List<String> nameColumns = new ArrayList<>();
            firstRow.forEach(cell -> nameColumns.add(cell.getStringCellValue()));

            Map<String, PostEntity> postEntityMap = new HashMap<>();
            Map<Long, DepartmentEntity> departmentEntityMap = new HashMap<>();
            Map<Long, EmployeeEntity> newEmployeesMap = new HashMap<>();
            List<PassportEntity> passportEntityList = new ArrayList<>();

            for (int i = 1; i < sheetAt.getLastRowNum(); i++) {
                final XSSFRow row = sheetAt.getRow(i);

                final EmployeeEntity newEmployeeEntity
                        = this.createEmployeeByRow(newEmployeesMap, row, nameColumns);
                if (newEmployeeEntity == null)
                    continue;

                final DepartmentEntity departmentEntity
                        = this.getOrCreateDepartment(departmentEntityMap, row, nameColumns);

                final PostEntity postEntity = this.getOrCreatePost(postEntityMap, row, nameColumns);

                final PassportEntity passportEntity
                        = this.createPassport(newEmployeeEntity, row, nameColumns);
                if (passportEntity != null)
                    passportEntityList.add(passportEntity);

                final String postKey = postEntity.getName() +
                        Optional.ofNullable(postEntity.getRang()).orElse(0);

                if (!postEntityMap.containsKey(postKey)) {
                    postEntityMap.put(postKey, postEntity);
                    newEmployeeEntity.setPost(postEntity);
                } else {
                    newEmployeeEntity.setPost(postEntityMap.get(postKey));
                }

                if (!departmentEntityMap.containsKey(departmentEntity.getId()))
                    departmentEntityMap.put(departmentEntity.getId(), departmentEntity);

                newEmployeeEntity.setDepartment(departmentEntityMap.get(departmentEntity.getId()));

                newEmployeesMap.put(newEmployeeEntity.getPersonnelNumber(), newEmployeeEntity);
            }

            Session currentSession;
            try {
                currentSession = sessionFactory.getCurrentSession();
            } catch (HibernateException e) {
                currentSession = sessionFactory.openSession();
            }

            final Transaction transaction = currentSession.getTransaction();
            try {
                transaction.begin();

                final int defaultBachSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);

                ResultUpdateEmployeesDto resultUpdateEmployeesDto = new ResultUpdateEmployeesDto();

                resultUpdateEmployeesDto = this.getUpdateDeletePost(currentSession,
                        postEntityMap, resultUpdateEmployeesDto);

                resultUpdateEmployeesDto = this.getUpdateDeleteDepartment(currentSession,
                        departmentEntityMap, resultUpdateEmployeesDto);

                resultUpdateEmployeesDto = this.getUpdateDeleteEmployees(currentSession,
                        newEmployeesMap, resultUpdateEmployeesDto);

                resultUpdateEmployeesDto = this.saveOrUpdateDepartment(currentSession, defaultBachSize,
                        departmentEntityMap, resultUpdateEmployeesDto);

                resultUpdateEmployeesDto = this.saveOrUpdatePost(currentSession, defaultBachSize,
                        postEntityMap, resultUpdateEmployeesDto);

                resultUpdateEmployeesDto = this.saveOrUpdateEmployees(currentSession, defaultBachSize,
                        newEmployeesMap, departmentEntityMap, postEntityMap, resultUpdateEmployeesDto);

                resultUpdateEmployeesDto = this.saveOrUpdatePassports(currentSession, passportEntityList,
                        defaultBachSize, resultUpdateEmployeesDto);

                transaction.commit();
                return resultUpdateEmployeesDto;
            } catch (Exception ex) {
                transaction.rollback();
                log.error("Ошибка при обновлении сотрудников!", ex);
                throw ex;
            }
        } catch (Exception e) {
            log.error("Ошибка при чтении файла!", e);
            throw new BadRequestException("Невозможно прочитать файл!");
        }
    }

    private ResultUpdateEmployeesDto saveOrUpdatePassports(Session currentSession,
                                                           List<PassportEntity> passportEntityList,
                                                           int defaultBachSize,
                                                           ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        int i = 0;

        List<PassportEntity> passportEntities = currentSession.createQuery("FROM PassportEntity", PassportEntity.class).list();
        Map<String, PassportEntity> collectPassportMap = passportEntities.stream()
                .collect(Collectors.toMap(p -> p.getSerial() + p.getNumber(), p -> p));

        for (PassportEntity passportEntity : passportEntityList) {
            final String key = passportEntity.getSerial() + passportEntity.getNumber();
            if (!collectPassportMap.containsKey(key)) {
                log.info(passportEntity.toString());
                currentSession.persist(passportEntity);

                if (++i % defaultBachSize == 0) {
                    i = 0;
                    currentSession.flush();
                    currentSession.clear();
                }
            } else {
                collectPassportMap.remove(key);
            }
        }

        currentSession.createQuery("DELETE FROM PassportEntity p WHERE p.id=:iDs")
                .setParameterList("iDs", collectPassportMap.values().stream()
                        .map(PassportEntity::getId)
                        .collect(Collectors.toList()));

        currentSession.flush();
        currentSession.clear();

        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto saveOrUpdateEmployees(Session currentSession, int defaultBachSize,
                                                           Map<Long, EmployeeEntity> newEmployeesMap,
                                                           Map<Long, DepartmentEntity> departmentEntityMap,
                                                           Map<String, PostEntity> postEntityMap,
                                                           ResultUpdateEmployeesDto resultUpdateEmployeesDto) {

        final List<EmployeeEntity> employeeEntityWorkPhone = this.jdbcTemplateKolaerBase
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
            final EmployeeEntity employeeEntity = newEmployeesMap.get(17240000 + empBase.getPersonnelNumber());
            if(employeeEntity != null) {
                employeeEntity.setEmail(empBase.getEmail());
                employeeEntity.setWorkPhoneNumber(empBase.getWorkPhoneNumber());
            }
        });

        List<DepartmentEntity> updatesDep = new ArrayList<>();
        int countToAdd = 0;
        int i = 0;
        for (EmployeeEntity newEmployee : newEmployeesMap.values()) {
            final String postName = newEmployee.getPost().getName();

            newEmployee.setPost(postEntityMap.get(postName
                    + Optional.ofNullable(newEmployee.getPost().getRang()).orElse(0)));
            newEmployee.setDepartment(departmentEntityMap.get(newEmployee.getDepartment().getId()));

            Long idChief = newEmployee.getDepartment().getChiefEmployeeId();
            if ((postName.contains("Начальник") || postName.equals("Директор")
                    || postName.equals("Руководитель") || postName.equals("Ведущий")
                    || postName.equals("Главный"))
                    && (idChief == null || !idChief.equals(newEmployee.getPersonnelNumber()))) {
                newEmployee.getDepartment().setChiefEmployeeId(newEmployee.getPersonnelNumber());
                updatesDep.add(newEmployee.getDepartment());
            }

            currentSession.saveOrUpdate(newEmployee);
            countToAdd++;
            if (++i % defaultBachSize == 0) {
                i = 0;
                currentSession.flush();
                currentSession.clear();
            }
        }

        if (defaultBachSize > 0) {
            defaultBachSize = 1;
            currentSession.flush();
            currentSession.clear();
        }

        resultUpdateEmployeesDto.setAddPostCount(countToAdd);
        i = 0;

        final String hql = "UPDATE DepartmentEntityDecorator d SET d.chiefEntity = :idCief WHERE d.id = :id";

        for (DepartmentEntity departmentEntity : updatesDep) {
            currentSession.createQuery(hql)
                    .setParameter("id", departmentEntity.getId())
                    .setParameter("idCief", departmentEntity.getChiefEmployeeId())
                    .executeUpdate();

            if (++i % defaultBachSize == 0) {
                i = 0;
                currentSession.flush();
                currentSession.clear();
            }
        }

        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto saveOrUpdatePost(Session currentSession, int defaultBachSize,
                                                      Map<String, PostEntity> postEntityMap,
                                                      ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        int countToAdd = 0;
        int i = 0;
        for (PostEntity postEntity : postEntityMap.values()) {
            currentSession.saveOrUpdate(postEntity);

            if (++i % defaultBachSize == 0) {
                i = 0;
                currentSession.flush();
                currentSession.clear();
            }
        }

        resultUpdateEmployeesDto.setAddPostCount(countToAdd);

        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto saveOrUpdateDepartment(Session currentSession, int defaultBachSize,
                                                            Map<Long, DepartmentEntity> departmentEntityMap,
                                                            ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        int countToAdd = 0;
        int i = 0;
        for (DepartmentEntity departmentEntity : departmentEntityMap.values()) {
            currentSession.saveOrUpdate(departmentEntity);
            countToAdd++;
            if (++i % defaultBachSize == 0) {
                i = 0;
                currentSession.flush();
                currentSession.clear();
            }
        }

        resultUpdateEmployeesDto.setAddDepCount(countToAdd);

        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto getUpdateDeleteEmployees(Session currentSession,
                                                              Map<Long, EmployeeEntity> newEmployeesMap,
                                                              ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        final List<Object[]> resultEmployeeFromDb
                = currentSession.createQuery("SELECT e.id, e.personnelNumber FROM EmployeeEntityDecorator e")
                .list();

        List<EmployeeEntity> employeeFromDb = resultEmployeeFromDb.stream()
                .map(this::convertEmployee).collect(Collectors.toList());

        resultUpdateEmployeesDto.setAllEmployeeSize(employeeFromDb.size());

        final List<Long> removeEmployees = employeeFromDb.stream()
                .filter(key -> !newEmployeesMap.containsKey(key.getPersonnelNumber()))
                .map(EmployeeEntity::getPersonnelNumber)
                .collect(Collectors.toList());

        resultUpdateEmployeesDto.setDeleteEmployeeCount(removeEmployees.size());

        if (removeEmployees.size() > 0) {
            currentSession.createQuery("UPDATE FROM EmployeeEntity SET dismissalDate = :dismissalDate WHERE personnelNumber IN (:personnelNumbers)")
                    .setParameterList("personnelNumbers", removeEmployees)
                    .setParameter("dismissalDate", new Date())
                    .executeUpdate();

            currentSession.flush();
            currentSession.clear();
        }

        employeeFromDb.forEach(epm -> {
            final EmployeeEntity employeeEntity = newEmployeesMap.get(epm.getPersonnelNumber());
            if(employeeEntity != null)
                employeeEntity.setId(epm.getId());
        });

        return resultUpdateEmployeesDto;
    }

    private EmployeeEntity convertEmployee(Object[] objects) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId((Long) objects[0]);
        employeeEntity.setPersonnelNumber((Long) objects[1]);

        return employeeEntity;
    }

    private ResultUpdateEmployeesDto getUpdateDeleteDepartment(Session currentSession,
                                                               Map<Long, DepartmentEntity> departmentEntityMap,
                                                               ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        final List<DepartmentEntity> depListFromDb = currentSession
                .createQuery("FROM DepartmentEntity d")
                .list();

        resultUpdateEmployeesDto.setAllDepSize(depListFromDb.size());

        final Iterator<DepartmentEntity> iteratorDepEntity = depListFromDb.iterator();
        while (iteratorDepEntity.hasNext()) {
            final DepartmentEntity next = iteratorDepEntity.next();
            final DepartmentEntity departmentEntity = departmentEntityMap.get(next.getId());
            if (departmentEntity != null) {
                departmentEntityMap.put(next.getId(), next);
                iteratorDepEntity.remove();
            }
        }

        resultUpdateEmployeesDto.setDeleteDepCount(depListFromDb.size());

        final List<Long> postIds = depListFromDb.stream().map(DepartmentEntity::getId)
                .collect(Collectors.toList());

        if (postIds.size() > 0) {
            currentSession.createQuery("DELETE FROM DepartmentEntityDecorator d WHERE d.id IN (:idList)")
                    .setParameterList("idList", postIds)
                    .executeUpdate();

            currentSession.flush();
            currentSession.clear();
        }

        return resultUpdateEmployeesDto;
    }

    private ResultUpdateEmployeesDto getUpdateDeletePost(Session currentSession,
                                                         Map<String, PostEntity> postEntityMap,
                                                         ResultUpdateEmployeesDto resultUpdateEmployeesDto) {
        final List<PostEntity> postEntitiesFromDb = currentSession
                .createQuery("FROM PostEntity")
                .list();

        resultUpdateEmployeesDto.setAllPostSize(postEntitiesFromDb.size());

        final Iterator<PostEntity> iteratorPostEntity = postEntitiesFromDb.iterator();
        while (iteratorPostEntity.hasNext()) {
            final PostEntity next = iteratorPostEntity.next();
            final String key = next.getName() + Optional.ofNullable(next.getRang()).orElse(0);
            final PostEntity postEntity = postEntityMap.get(key);
            if (postEntity != null) {
                postEntityMap.put(key, next);
                iteratorPostEntity.remove();
            }
        }

        resultUpdateEmployeesDto.setDeletePostCount(postEntitiesFromDb.size());

        final List<Long> postIds = postEntitiesFromDb.stream().map(PostEntity::getId)
                .collect(Collectors.toList());
        if (postIds.size() > 0) {
            currentSession.createQuery("DELETE FROM PostEntity p WHERE p.id IN (:idList)")
                    .setParameterList("idList", postIds)
                    .executeUpdate();


            currentSession.flush();
            currentSession.clear();
        }

        return resultUpdateEmployeesDto;
    }

    private PassportEntity createPassport(EmployeeEntity newEmployeeEntity,
                                          XSSFRow row, List<String> nameColumns) {
        String serialFirst = row.getCell(nameColumns.indexOf(SERIAL_1_DOCUMENT)).getStringCellValue();
        PassportEntity passportEntity = null;
        if (serialFirst != null && !serialFirst.trim().isEmpty()) {
            passportEntity = new PassportEntity();
            passportEntity.setEmployee(newEmployeeEntity);

            final String serialSecond = row.getCell(nameColumns.indexOf(SERIAL_2_DOCUMENT)).getStringCellValue();

            passportEntity.setSerial(serialFirst + serialSecond);
            serialFirst = row.getCell(nameColumns.indexOf(NUMBER_DOCUMENT)).getStringCellValue();
            passportEntity.setNumber(serialFirst);
        }

        return passportEntity;
    }

    private PostEntity getOrCreatePost(Map<String, PostEntity> postEntityMap,
                                       XSSFRow row, List<String> nameColumns) {
        PostEntity postEntity;

        String value = row.getCell(nameColumns.indexOf(POST_NAME)).getStringCellValue();

        final String rang = value.replaceAll("[\\D]", "");
        if (!rang.trim().isEmpty()) {
            final String rangInfo = value.substring(0, value.indexOf(rang));

            final String key = rangInfo + rang;
            if(postEntityMap.containsKey(key))
                return postEntityMap.get(key);

            postEntity = new PostEntity();
            if (postEntity.getName() == null) {
                postEntity.setRang(Integer.valueOf(rang));
                final String postWithOutSpace = value.replaceAll(" ","");
                switch (postWithOutSpace.charAt(postWithOutSpace.indexOf(rang) + 1)) {
                    case 'р':
                        postEntity.setType(TypePostEnum.DISCHARGE);
                        break;
                    case 'к':
                        postEntity.setType(TypePostEnum.CATEGORY);
                        break;
                    case 'г':
                        postEntity.setType(TypePostEnum.GROUP);
                        break;
                    default:
                        break;
                }
            }

            postEntity.setName(rangInfo.trim());
        } else {
            final String key = value + "0";
            if(postEntityMap.containsKey(key))
                return postEntityMap.get(key);

            postEntity = new PostEntity();
            postEntity.setName(value);
        }

        final String postCode = row.getCell(nameColumns.indexOf(POST_CODE)).getStringCellValue();
        postEntity.setCode(postCode);

        return postEntity;
    }

    private DepartmentEntity getOrCreateDepartment(Map<Long, DepartmentEntity> departmentEntityMap,
                                                   XSSFRow row,
                                                   List<String> nameColumns) {
        final Long idDep = Long
                .valueOf(row.getCell(nameColumns.indexOf(DEP_ID)).getStringCellValue());

        if(departmentEntityMap.containsKey(idDep)){
            return departmentEntityMap.get(idDep);
        }

        final DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(idDep);

        final String depName = row.getCell(nameColumns.indexOf(DEP_NAME)).getStringCellValue();

        final Pattern pattern = Pattern.compile("[а-яА-Я ]*");
        final Matcher matcher = pattern.matcher(depName);

        departmentEntity.setName(depName);
        if (matcher.find())
            departmentEntity.setAbbreviatedName(matcher.group().trim());
        else
            departmentEntity.setAbbreviatedName(depName);

        return departmentEntity;
    }

    private EmployeeEntity createEmployeeByRow(Map<Long, EmployeeEntity> newEmployeesMap,
                                               XSSFRow row, List<String> nameColumns) {
        String value = row.getCell(nameColumns.indexOf(PERSONNEL_NUMBER)).getStringCellValue();

        if (value == null || value.trim().isEmpty()
                || newEmployeesMap.containsKey(Long.valueOf(value)))
            return null;

        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        newEmployeeEntity.setPersonnelNumber(Long.valueOf(value));

        value = row.getCell(nameColumns.indexOf(SECOND_NAME)).getStringCellValue();
        value += " " + row.getCell(nameColumns.indexOf(FIRST_NAME)).getStringCellValue();
        value += " " + row.getCell(nameColumns.indexOf(THIRD_NAME)).getStringCellValue();
        newEmployeeEntity.setInitials(value);

        Date date = row.getCell(nameColumns.indexOf(BIRTHDAY_DATE)).getDateCellValue();
        newEmployeeEntity.setBirthday(date);

        date = row.getCell(nameColumns.indexOf(EMPLOYMENT_DATE)).getDateCellValue();
        newEmployeeEntity.setEmploymentDate(date);

        date = row.getCell(nameColumns.indexOf(DISMISSAL_DATE)).getDateCellValue();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) != YEAR_NOT_DISMISSAL)
            newEmployeeEntity.setDismissalDate(date);
        else
            newEmployeeEntity.setDismissalDate(null);

        value = row.getCell(nameColumns.indexOf(SEX)).getStringCellValue();
        if("мужской".equals(value.toLowerCase())) {
            newEmployeeEntity.setGender(EnumGender.MALE);
        } else {
            newEmployeeEntity.setGender(EnumGender.FEMALE);
        }

        value = row.getCell(nameColumns.indexOf(PHONE_NUMBER)).getStringCellValue();
        newEmployeeEntity.setHomePhoneNumber(value);

        final int indexOfEmail = nameColumns.indexOf(EMAIL);
        if(indexOfEmail > -1) {
            value = row.getCell(indexOfEmail).getStringCellValue();
            newEmployeeEntity.setEmail(value);
        }

        try {
            newEmployeeEntity.setPhoto("http://asupkolaer.local/app_ie8/assets/images/vCard/o_"
                    + URLEncoder.encode(newEmployeeEntity.getInitials(), "UTF-8")
                    .replace("+", "%20") + ".jpg");
        } catch (UnsupportedEncodingException ignore) {
        }

        return newEmployeeEntity;
    }

}
