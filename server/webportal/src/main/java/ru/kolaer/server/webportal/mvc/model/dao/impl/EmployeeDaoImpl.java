package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.dto.ResultUpdateEmployeesDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.PassportEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntityDecorator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Danilov on 27.07.2016.
 */
@Repository(value = "employeeDao")
@Slf4j
public class EmployeeDaoImpl implements EmployeeDao {
    private final static String SECOND_NAME = "Фамилия";
    private final static String FIRST_NAME = "Имя";
    private final static String THIRD_NAME = "Отчество";
    private final static String SEX = "Пол";
    private final static String DEP_NAME = "Текст Подразделение";
    private final static String POST_NAME = "Штатная должность";
    private final static String EMPLOYTMENT_DATE = "Поступл.";
    private final static String DISMISSAL_DATE = "Дата увольнения";
    private final static String BIRTHDAY_DATE = "ДатаРожд";
    private final static String PERSONNEL_NUMBER = "Таб.№";
    private final static String PHONE_NUMBER = "Телефон";
    private final static String EMAIL = "Эл. почта(MAIL)";
    private final static String TYPE_DOCUMENT = "Вид документа";
    private final static String SERIAL_DOCUMENT = "Серия2";
    private final static String NUMBER_DOCUMENT = "Номер документа";

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEntity> findAll() {
        final List<EmployeeEntity> result = sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator emp ORDER BY emp.initials")
                .list();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeEntity findByID(@NonNull Integer id) {
        final EmployeeEntity result = (EmployeeEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.pnumber = :id ORDER BY emp.initials")
                .setParameter("id", id)
                .uniqueResult();
        return result;
    }

    @Override
    @Transactional
    public void persist(@NonNull EmployeeEntity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull EmployeeEntity obj) {

    }

    @Override
    public void delete(@NonNull List<EmployeeEntity> objs) {

    }

    @Override
    @Transactional
    public void update(@NonNull EmployeeEntity entity) {

    }

    @Override
    public void update(@NonNull List<EmployeeEntity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    public List<EmployeeEntity> findEmployeeByInitials(@NonNull String initials) {
        final List<EmployeeEntity> result = this.sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.initials LIKE :initials ORDER BY emp.initials")
                .setParameter("initials", "%" + initials + "%")
                .list();
        return result;
    }

    @Transactional(readOnly = true)
    public List<EmployeeEntity> findByDepartamentById(@NonNull Integer id) {
        final List<EmployeeEntity> result = this.sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.departament.id = :id ORDER BY emp.initials")
                .setParameter("id", id).list();
        return result;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeEntity> findByDepartamentById(int page, int pageSize, @NonNull Integer id) {
        Session currentSession = this.sessionFactory.getCurrentSession();
        final Long count = (Long) currentSession.createQuery("SELECT COUNT(emp.pnumber) FROM GeneralEmployeesEntityDecorator emp WHERE emp.departament.id = :id")
                .setParameter("id", id)
                .uniqueResult();

        final List<EmployeeEntity> result = this.sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.departament.id = :id ORDER BY emp.initials")
                .setParameter("id", id)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();

        return new Page<>(result, page, count, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEntity> getUserRangeBirthday(@NonNull final Date startDate, @NonNull final Date endDate) {
        final List<EmployeeEntity> result = sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator t where t.birthday BETWEEN :startDate AND :endDate ORDER BY t.initials")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEntity> getUsersByBirthday(@NonNull final Date date) {
        final List<EmployeeEntity> result = sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) ORDER BY t.initials")
                .setParameter("date", date)
                .list();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEntity> getUserBirthdayToday() {
        final List<EmployeeEntity> result = sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE) ORDER BY t.initials")
                .list();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthday(@NonNull final Date date) {
        final Long result = (Long) sessionFactory.getCurrentSession()
                .createQuery("SELECT count(t.pnumber) FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
                .setParameter("date", date)
                .uniqueResult();
        return result.intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEntity> getUsersByInitials(@NonNull final String initials) {
        final List<EmployeeEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator AS t JOIN FETCH t.departament where t.initials like :initials ORDER BY t.initials")
                .setParameter("initials", "%" + initials + "%")
                .list();
        return result;
    }

    @Override
    public ResultUpdateEmployeesDto updateEmployeesFromXlsx(@NonNull File file) {
        return null;
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
            Map<String, DepartmentEntity> departmentEntityMap = new HashMap<>();
            Map<Integer, EmployeeEntity> newEmployeesMap = new HashMap<>();
            List<PassportEntity> passportEntityList = new ArrayList<>();

            for(int i = 1; i < sheetAt.getLastRowNum(); i++) {
                final XSSFRow row = sheetAt.getRow(i);

                String value = row.getCell(nameColumns.indexOf(PERSONNEL_NUMBER)).getStringCellValue();

                if(value == null || value.trim().isEmpty()
                        || newEmployeesMap.containsKey(Integer.valueOf(value)))
                    continue;

                final EmployeeEntity newEmployeeEntity = new EmployeeEntityDecorator();
                newEmployeeEntity.setPersonnelNumber(Integer.valueOf(value));

                value = row.getCell(nameColumns.indexOf(SECOND_NAME)).getStringCellValue();
                value += " " + row.getCell(nameColumns.indexOf(FIRST_NAME)).getStringCellValue();
                value += " " +row.getCell(nameColumns.indexOf(THIRD_NAME)).getStringCellValue();
                newEmployeeEntity.setInitials(value);

                Date date = row.getCell(nameColumns.indexOf(BIRTHDAY_DATE)).getDateCellValue();
                newEmployeeEntity.setBirthday(date);

                date = row.getCell(nameColumns.indexOf(EMPLOYTMENT_DATE)).getDateCellValue();
                newEmployeeEntity.setEmploymentDate(date);

                date = row.getCell(nameColumns.indexOf(DISMISSAL_DATE)).getDateCellValue();
                //TODO: Set with out on 9999 year
                    newEmployeeEntity.setDismissalDate(date);

                value = row.getCell(nameColumns.indexOf(SEX)).getStringCellValue();
                newEmployeeEntity.setGender(value);

                value = row.getCell(nameColumns.indexOf(PHONE_NUMBER)).getStringCellValue();
                if(value.length() == 10)
                    newEmployeeEntity.setMobileNumber(value);
                else
                    newEmployeeEntity.setPhoneNumber(value);

                value = row.getCell(nameColumns.indexOf(EMAIL)).getStringCellValue();
                newEmployeeEntity.setEmail(value);

                value = row.getCell(nameColumns.indexOf(DEP_NAME)).getStringCellValue();
                DepartmentEntity departmentEntity = departmentEntityMap.getOrDefault(value ,new DepartmentEntityDecorator());

                if(departmentEntity.getName() == null) {
                    final Pattern pattern = Pattern.compile("[а-яА-Я ]*");
                    final Matcher matcher = pattern.matcher(value);

                    departmentEntity.setName(value);
                    if (matcher.find())
                        departmentEntity.setAbbreviatedName(matcher.group().trim());
                    else
                        departmentEntity.setAbbreviatedName(value);
                }

                value = row.getCell(nameColumns.indexOf(POST_NAME)).getStringCellValue();
                final Pattern pattern = Pattern.compile("[^\\d|+]*");
                final Matcher matcher = pattern.matcher(value);

                PostEntity postEntity;

                if(matcher.find() && value.length() != matcher.group().length()) {
                    final String rangInfo = matcher.group();
                    int indexRang = rangInfo.length();
                    int indexType = rangInfo.length() + 2;
                    postEntity = postEntityMap.getOrDefault(rangInfo + Character.getNumericValue(value.charAt(indexRang)), new PostEntityDecorator());

                    if(postEntity.getName() == null) {
                        postEntity.setRang(Character.getNumericValue(value.charAt(indexRang)));

                        if(indexType < value.length()) {
                            switch (value.charAt(indexType)) {
                                case 'р':
                                    postEntity.setTypeRang(TypeRangEnum.DISCHARGE.getName());
                                    break;
                                case 'к':
                                    postEntity.setTypeRang(TypeRangEnum.CATEGORY.getName());
                                    break;
                                case 'г':
                                    postEntity.setTypeRang(TypeRangEnum.GROUP.getName());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    postEntity.setName(rangInfo.trim());
                } else {
                    postEntity = postEntityMap.getOrDefault(value + "0", new PostEntityDecorator());
                    if(postEntity.getName() == null) {
                        postEntity.setName(value);
                    }
                }

                value = row.getCell(nameColumns.indexOf(SERIAL_DOCUMENT)).getStringCellValue();
                if(value != null && !value.trim().isEmpty()) {
                    final PassportEntity passportEntity = new PassportEntity();
                    passportEntity.setEmployee(newEmployeeEntity);
                    passportEntity.setSerial(value);
                    value = row.getCell(nameColumns.indexOf(NUMBER_DOCUMENT)).getStringCellValue();
                    passportEntity.setNumber(value);
                    passportEntityList.add(passportEntity);

                }

                if(!postEntityMap.containsKey(postEntity.getName() + Optional.ofNullable(postEntity.getRang()).orElse(0))) {
                    postEntityMap.put(postEntity.getName() + Optional.ofNullable(postEntity.getRang()).orElse(0), postEntity);
                    newEmployeeEntity.setPostEntity(postEntity);
                } else {
                    newEmployeeEntity.setPostEntity(postEntityMap.get(postEntity.getName()
                            + Optional.ofNullable(postEntity.getRang()).orElse(0)));
                }

                if(!departmentEntityMap.containsKey(departmentEntity.getAbbreviatedName())) {
                    departmentEntityMap.put(departmentEntity.getAbbreviatedName(), departmentEntity);
                    newEmployeeEntity.setDepartment(departmentEntity);
                } else {
                    newEmployeeEntity.setDepartment(departmentEntityMap.get(departmentEntity.getAbbreviatedName()));
                }

                newEmployeeEntity.setPhoto("http://asupkolaer/app_ie8/assets/images/vCard/o_"
                        + URLEncoder.encode(newEmployeeEntity.getInitials(), "UTF-8")
                        .replace("+", "%20") + ".jpg");

                newEmployeesMap.put(newEmployeeEntity.getPersonnelNumber(), newEmployeeEntity);
            }

            final ResultUpdateEmployeesDto resultUpdateEmployeesDto = new ResultUpdateEmployeesDto();
            //final Session currentSession = this.sessionFactory.getCurrentSession();

            Session currentSession;
            try {
                currentSession = sessionFactory.getCurrentSession();
            } catch (HibernateException e) {
                currentSession = sessionFactory.openSession();
            }

            final Transaction transaction = currentSession.getTransaction();
            try {
                transaction.begin();
                List<PostEntity> postEntitiesFromDb = currentSession
                        .createQuery("FROM PostEntityDecorator")
                        .list();

                resultUpdateEmployeesDto.setAllPostSize(postEntitiesFromDb.size());

                Iterator<PostEntity> iteratorPostEntity = postEntitiesFromDb.iterator();
                while(iteratorPostEntity.hasNext()) {
                    final PostEntity next = iteratorPostEntity.next();
                    final String key = next.getName() + Optional.ofNullable(next.getRang()).orElse(0);
                    final PostEntity postEntity = postEntityMap.get(key);
                    if(postEntity != null) {
                        postEntityMap.put(key, next);
                        iteratorPostEntity.remove();
                    }
                }

                resultUpdateEmployeesDto.setDeletePostCount(postEntitiesFromDb.size());
                resultUpdateEmployeesDto.setAddPostCount(resultUpdateEmployeesDto.getAllPostSize()
                        - resultUpdateEmployeesDto.getDeletePostCount()
                );

                List<Integer> postIds = postEntitiesFromDb.stream().map(PostEntity::getId)
                        .collect(Collectors.toList());
                if(postIds.size() > 0) {
                    currentSession.createQuery("DELETE FROM PostEntityDecorator p WHERE p.id IN (:idList)")
                            .setParameterList("idList", postIds)
                            .executeUpdate();


                    currentSession.flush();
                    currentSession.clear();
                }

                List<DepartmentEntity> depListFromDb = currentSession
                        .createQuery("FROM DepartmentEntityDecorator d")
                        .list();

                resultUpdateEmployeesDto.setAllDepSize(depListFromDb.size());

                Iterator<DepartmentEntity> iteratorDepEntity = depListFromDb.iterator();
                while(iteratorDepEntity.hasNext()) {
                    final DepartmentEntity next = iteratorDepEntity.next();
                    final DepartmentEntity departmentEntity = departmentEntityMap.get(next.getName());
                    if(departmentEntity != null) {
                        departmentEntityMap.put(next.getAbbreviatedName(), next);
                        iteratorDepEntity.remove();
                    }
                }

                resultUpdateEmployeesDto.setDeleteDepCount(depListFromDb.size());
                resultUpdateEmployeesDto.setAddDepCount(
                        resultUpdateEmployeesDto.getAllDepSize()
                        - resultUpdateEmployeesDto.getDeleteDepCount()
                );

                postIds = depListFromDb.stream().map(DepartmentEntity::getId)
                        .collect(Collectors.toList());

                if(postIds.size() > 0) {
                    currentSession.createQuery("DELETE FROM DepartmentEntityDecorator d WHERE d.id IN (:idList)")
                            .setParameterList("idList", postIds)
                            .executeUpdate();

                    currentSession.flush();
                    currentSession.clear();
                }

                List<Integer> employeeFromDb = currentSession.createQuery("SELECT e.personnelNumber FROM EmployeeEntityDecorator e")
                        .list();

                resultUpdateEmployeesDto.setAllEmployeeSize(employeeFromDb.size());

                List<Integer> removeEmployees = employeeFromDb.stream()
                        .filter(key -> !newEmployeesMap.containsKey(key))
                        .collect(Collectors.toList());

                resultUpdateEmployeesDto.setDeleteEmployeeCount(removeEmployees.size());
                resultUpdateEmployeesDto.setAddEmployeeCount(
                        resultUpdateEmployeesDto.getAllEmployeeSize()
                        - resultUpdateEmployeesDto.getDeleteEmployeeCount()
                );

                if(removeEmployees.size() > 0) {
                    currentSession.createQuery("DELETE FROM EmployeeEntityDecorator e WHERE e.personnelNumber IN (:pnumbers)")
                            .setParameterList("pnumbers", removeEmployees)
                            .executeUpdate();

                    currentSession.flush();
                    currentSession.clear();
                }

                final int defaultBachSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);

                int i = 0;
                for (DepartmentEntity departmentEntity : departmentEntityMap.values()) {
                    currentSession.saveOrUpdate(departmentEntity);

                    if(++i % defaultBachSize == 0) {
                        i = 0;
                        currentSession.flush();
                        currentSession.clear();
                    }
                }

                if(i != 0) {
                    i = 0;
                    currentSession.flush();
                    currentSession.clear();
                }
                for (PostEntity postEntity : postEntityMap.values()) {
                    currentSession.saveOrUpdate(postEntity);

                    if(++i % defaultBachSize == 0) {
                        i = 0;
                        currentSession.flush();
                        currentSession.clear();
                    }
                }

                if(i != 0) {
                    i = 0;
                    currentSession.flush();
                    currentSession.clear();
                }

                List<DepartmentEntity> updatesDep = new ArrayList<>();

                for (EmployeeEntity newEmployee : newEmployeesMap.values()) {
                    final String postName = newEmployee.getPostEntity().getName();

                    newEmployee.setPostEntity(postEntityMap.get(postName
                            + Optional.ofNullable(newEmployee.getPostEntity().getRang()).orElse(0)));
                    newEmployee.setDepartment(departmentEntityMap.get(newEmployee.getDepartment().getAbbreviatedName()));

                    Integer idChief = newEmployee.getDepartment().getChiefEntity();
                    if ((postName.contains("Начальник") || postName.equals("Директор")
                            || postName.equals("Руководитель") || postName.equals("Ведущий")
                            || postName.equals("Главный"))
                            && (idChief == null || !idChief.equals(newEmployee.getPersonnelNumber()))) {
                        newEmployee.getDepartment().setChiefEntity(newEmployee.getPersonnelNumber());
                        updatesDep.add(newEmployee.getDepartment());
                    }

                    currentSession.saveOrUpdate(newEmployee);

                    if(++i % defaultBachSize == 0) {
                        i = 0;
                        currentSession.flush();
                        currentSession.clear();
                    }
                }

                if(i != 0) {
                    i = 0;
                    currentSession.flush();
                    currentSession.clear();
                }

                final String hql = "UPDATE DepartmentEntityDecorator d SET d.chiefEntity = :idCief WHERE d.id = :id";

                for (DepartmentEntity departmentEntity : updatesDep) {
                    currentSession.createQuery(hql)
                            .setParameter("id", departmentEntity.getId())
                            .setParameter("idCief", departmentEntity.getChiefEntity())
                            .executeUpdate();

                    if(++i % defaultBachSize == 0) {
                        i = 0;
                        currentSession.flush();
                        currentSession.clear();
                    }
                }

                if(i != 0) {
                    i = 0;
                    currentSession.flush();
                    currentSession.clear();
                }

                List<PassportEntity> passportEntities = currentSession.createQuery("FROM PassportEntity").list();
                Map<String, PassportEntity> collectPassportMap = passportEntities.stream()
                        .collect(Collectors.toMap(p -> p.getSerial() + p.getNumber(), p -> p));

                for (PassportEntity passportEntity : passportEntityList) {
                    final String key = passportEntity.getSerial() + passportEntity.getNumber();
                    if(!collectPassportMap.containsKey(key)) {
                        currentSession.persist(passportEntity);
                    } else {
                        collectPassportMap.remove(key);
                    }

                    if(++i % defaultBachSize == 0) {
                        i = 0;
                        currentSession.flush();
                        currentSession.clear();
                    }
                }

                if(i != 0) {
                    currentSession.flush();
                    currentSession.clear();
                }

                currentSession.createQuery("DELETE FROM PassportEntity p WHERE p.id=:iDs")
                        .setParameterList("iDs", collectPassportMap.values().stream()
                                .map(PassportEntity::getId)
                                .collect(Collectors.toList()));

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


}
