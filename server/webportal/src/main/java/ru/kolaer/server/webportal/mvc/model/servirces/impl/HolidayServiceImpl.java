package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.mvc.model.dao.HolidayDao;
import ru.kolaer.server.webportal.mvc.model.servirces.HolidayService;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by danilovey on 31.10.2016.
 */
@Service(value = "holidayService")
public class HolidayServiceImpl implements HolidayService {
    private static Logger logger = LoggerFactory.getLogger(HolidayServiceImpl.class);

    private final HolidayDao holidayDao;

    @Autowired
    public HolidayServiceImpl(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayDao.getAllHolidays();
    }

    @Override
    public Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson) {
        if(dateTimeJson == null)
            return null;

        return this.holidayDao.getHolidayByDayMonth(dateTimeJson);
    }

    @Override
    public List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson) {
        if(dateTimeJson == null)
            return Collections.emptyList();
        return this.holidayDao.getHolidayByMonth(dateTimeJson);
    }

    @PostConstruct
    public void initHoliday() {
        this.updateHoliday();
    }

    public void updateHoliday() {
        final String year = String.valueOf(LocalDate.now().getYear());

        try {
            ClassPathResource classPathResource = new ClassPathResource("/calendar.xml");
            if(!classPathResource.exists())
                logger.warn("Нет локальных данных с http://xmlcalendar.ru в xml формате!");

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = classPathResource.exists()
                    ? documentBuilder.parse(classPathResource.getInputStream())
                    : documentBuilder.parse("http://xmlcalendar.ru/data/ru/" + year + "/calendar.xml");

            final Node holidays = document.getElementsByTagName("holidays").item(0);
            final NodeList holidaysList = holidays.getChildNodes();

            final Map<String, String> holidaysName = new HashMap<>();

            for (int holidayIndex = 0; holidayIndex < holidaysList.getLength(); holidayIndex++) {
                final Node item = holidaysList.item(holidayIndex);
                if(item.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) item;
                    holidaysName.put(eElement.getAttribute("id"), eElement.getAttribute("title"));
                }
            }

            final Node days = document.getElementsByTagName("days").item(0);
            final NodeList daysList = days.getChildNodes();

            final List<Holiday> result = new ArrayList<>();

            for (int dayIndex = 0; dayIndex < daysList.getLength(); dayIndex++) {
                final Node item = daysList.item(dayIndex);
                if(item.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) item;
                    final String[] dayMonth = eElement.getAttribute("d").split("\\.");
                    final String day = dayMonth[1];
                    final String month = dayMonth[0];

                    final TypeDay typeDay = this.getTypeDay(eElement.getAttribute("t"));
                    final String nameHoliday = Optional.ofNullable(holidaysName.get(eElement.getAttribute("h"))).orElse("Предпраздничный день");

                    final Holiday holiday = new Holiday();
                    holiday.setDate(day + "." + month + "." + year );
                    holiday.setName(nameHoliday);
                    holiday.setTypeDay(typeDay);

                    result.add(holiday);
                }
            }

            if(result.size() > 0) {
                this.holidayDao.insertHolidays(result);
                logger.info("Holidays size: {}", result.size());
            }

        } catch (Exception e) {
            logger.error("Ошибка при получении праздников!", e);
        }
    }

    private TypeDay getTypeDay(String type) {
        switch (type) {
            case "1" : return TypeDay.CELEBRATION;
            case "2" : return TypeDay.SHORT;
            case "3" : return TypeDay.WORK;
            default: return TypeDay.HOLIDAY;
        }
    }
}
