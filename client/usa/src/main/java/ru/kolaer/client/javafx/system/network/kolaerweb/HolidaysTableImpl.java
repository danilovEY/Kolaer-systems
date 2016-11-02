package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.system.network.HolidaysTable;

import java.time.LocalDate;

/**
 * Created by danilovey on 02.11.2016.
 */
public class HolidaysTableImpl implements HolidaysTable {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_GET;
    private final String URL_GET_ALL;

    public HolidaysTableImpl(String path) {
        this.URL_GET = path + "/get";
        this.URL_GET_ALL = this.URL_GET + "/all";
    }


    @Override
    public Holiday[] getHolidaysInThisMonth() {
        final LocalDate date = LocalDate.now();
        return this.getHolidays(date.getMonthValue(), date.getYear());
    }

    @Override
    public Holiday[] getHolidays(final int month, final int year) {
        System.out.println(this.URL_GET + "/" + String.valueOf(month) + "/" + String.valueOf(year));
        final Holiday[] holidays = restTemplate.getForObject(this.URL_GET + "/" + String.valueOf(month) + "/" + String.valueOf(year), Holiday[].class);

        return holidays;
    }

    @Override
    public Holiday[] getHolidaysAll() {
        final Holiday[] holidays = restTemplate.getForObject(this.URL_GET_ALL, Holiday[].class);
        return holidays;
    }
}
