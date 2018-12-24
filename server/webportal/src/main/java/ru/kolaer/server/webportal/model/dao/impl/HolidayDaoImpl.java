package ru.kolaer.server.webportal.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.kolaer.common.dto.kolaerweb.DateTimeJson;
import ru.kolaer.common.dto.kolaerweb.Holiday;
import ru.kolaer.common.dto.kolaerweb.TypeDay;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.HolidayDao;
import ru.kolaer.server.webportal.model.dto.holiday.FindHolidayRequest;
import ru.kolaer.server.webportal.model.entity.holiday.HolidayEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 31.10.2016.
 */
@Repository(value = "holidayDao")
public class HolidayDaoImpl extends AbstractDefaultDao<HolidayEntity> implements HolidayDao {
    private final List<Holiday> holidays = new ArrayList<>();

    protected HolidayDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, HolidayEntity.class);
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return this.holidays;
    }

    @Override
    public Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson) {
        final String[] tempDate = dateTimeJson.getDate().split("\\.");
        final Integer day = Integer.valueOf(tempDate[0]);
        final Integer month = Integer.valueOf(tempDate[1]);

        for(Holiday holiday : this.holidays){
            final String[] holidayDate = holiday.getDate().split("\\.");
            final Integer holidayDay = Integer.valueOf(holidayDate[0]);
            final Integer holidayMonth = Integer.valueOf(holidayDate[1]);
            if(holidayDay.equals(day) && holidayMonth.equals(month))
                return holiday;

        }

        return null;
    }

    @Override
    public List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson) {
        final Integer month = Integer.valueOf(dateTimeJson.getDate().split("\\.")[1]);

        return this.holidays.stream().filter(holiday -> {
            final String[] holidayDate = holiday.getDate().split("\\.");
            final Integer holidayMonth = Integer.valueOf(holidayDate[1]);
            return holidayMonth.equals(month);
        }).collect(Collectors.toList());
    }

    @Override
    public void insertHolidays(List<Holiday> holidays) {
        this.holidays.clear();
        this.holidays.addAll(holidays);
    }

    @Override
    public HolidayEntity findByDate(LocalDate holidayDate) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " WHERE holidayDate = :holidayDate", getEntityClass())
                .setParameter("holidayDate", holidayDate)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public List<HolidayEntity> findAll(FindHolidayRequest findHolidayRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("fromDate", findHolidayRequest.getFromDate());
        params.put("toDate", findHolidayRequest.getToDate());

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("FROM ")
                .append(getEntityName())
                .append(" WHERE holidayDate >= :fromDate AND holidayDate <= :toDate ");

        if (!CollectionUtils.isEmpty(findHolidayRequest.getTypeHolidays())) {
            sqlQuery = sqlQuery.append("AND holidayType IN (:types) ");
            params.put("types", findHolidayRequest.getTypeHolidays());
        }

        sqlQuery = sqlQuery.append("ORDER BY holidayDate ASC");

        return getSession()
                .createQuery(sqlQuery.toString(), getEntityClass())
                .setProperties(params)
                .list();
    }

    @Override
    public Long findCountAll(FindHolidayRequest findHolidayRequest) {
        return getSession()
                .createQuery("SELECT COUNT(id) FROM " + getEntityName() +
                        " WHERE holidayDate >= :fromDate AND holidayDate <= :toDate AND holidayType IN (:types)", Long.class)
                .setParameter("fromDate", findHolidayRequest.getFromDate())
                .setParameter("toDate", findHolidayRequest.getToDate())
                .setParameterList("types", findHolidayRequest.getTypeHolidays())
                .uniqueResultOptional()
                .orElse(0L);
    }

    private Holiday getHolidayByDateTime(DateTimeJson dateTimeJson) {
        final Holiday holiday = new Holiday();
        holiday.setDate(dateTimeJson.getDate());

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        final LocalDate localDate = LocalDate.parse(dateTimeJson.getDate(), dateTimeFormatter);
        if(localDate.getDayOfWeek() == DayOfWeek.SUNDAY || localDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            holiday.setTypeDay(TypeDay.HOLIDAY);
            holiday.setName("Выходной");
        } else {
            holiday.setTypeDay(TypeDay.WORK);
            holiday.setName("Рабочий день");
        }

        return holiday;
    }
}
