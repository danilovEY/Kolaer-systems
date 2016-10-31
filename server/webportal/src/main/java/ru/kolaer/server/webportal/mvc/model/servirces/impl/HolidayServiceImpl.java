package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.mvc.model.dao.HolidayDao;
import ru.kolaer.server.webportal.mvc.model.servirces.HolidayService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by danilovey on 31.10.2016.
 */
@Service(value = "holidayService")
public class HolidayServiceImpl implements HolidayService {
    private final String CALENDAR_URL = "http://xmlcalendar.ru/data/ru/";

    @Autowired
    private HolidayDao holidayDao;

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

    @Scheduled(cron = "0 0 0 0 * ?")
    public void updateHoliday() throws ParserConfigurationException {
        final String year = String.valueOf(LocalDate.now().getYear());

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        try {
            final Document document = documentBuilder.parse(this.CALENDAR_URL + year + "/calendar.xml");
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
                    final String day = eElement.getAttribute("d");
                    final TypeDay typeDay = this.getTypeDay(eElement.getAttribute("t"));
                    final String nameHoliday = holidaysName.get(eElement.getAttribute("h"));

                    final DateTimeJson dateTimeJson = new DateTimeJson();
                    dateTimeJson.setTime("00:00:00");
                    dateTimeJson.setDate(day.replace(".", "-") + "-" + year);

                    final Holiday holiday = new Holiday();
                    holiday.setDateTimeHoliday(dateTimeJson);
                    holiday.setName(nameHoliday);
                    holiday.setTypeDay(typeDay);
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
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
