package ru.kolaer.client.counter.service;

import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.plugins.services.Service;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 25.08.2016.
 */
public class CounterService implements Service {
    private boolean isRun = false;

    @Override
    public boolean isRunning() {
        return this.isRun;
    }

    @Override
    public String getName() {
        return "Счетчик";
    }

    @Override
    public void stop() {
        this.isRun = false;
    }

    @Override
    public void run() {
        ServerResponse<List<CounterDto>> counters = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getCounterTable()
                .getAllCounters();

        if(counters.isServerError()) {
            this.isRun = false;
            return;
        }

        this.isRun = true;

        List<CounterViewImpl> countersList = new ArrayList<>();

        LocalDateTime dateNow = LocalDateTime.now();
        for(CounterDto counter : counters.getResponse()) {
            if(counter.getStart() == null || counter.getEnd() == null ||
                    counter.getStart().isBefore(dateNow) ||
                    counter.getEnd().isAfter(dateNow))
                continue;

            CounterViewImpl staticViewPPR = new CounterViewImpl(counter);
            staticViewPPR.initView(UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getStatic()::addStaticView);
            staticViewPPR.setTitle(counter.getTitle());
            staticViewPPR.setDescription(counter.getDescription());

            countersList.add(staticViewPPR);
        }

        while (isRun) {
            Iterator<CounterViewImpl> iterator = countersList.iterator();
            while (iterator.hasNext()) {
                CounterViewImpl ppr = iterator.next();

                LocalDateTime dateTimeJson = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getServerTools()
                        .getCurrentDataTime()
                        .getResponse();

                LocalDateTime ldt = ppr.getCounter().getEnd();

                LocalDateTime tempDateTime = LocalDateTime.from(dateTimeJson);

                long daysStart = ChronoUnit.DAYS.between(ppr.getCounter().getStart(), dateTimeJson.toLocalDate());

                long years = tempDateTime.until(ldt, ChronoUnit.YEARS);
                tempDateTime = tempDateTime.plusYears(years);

                long months = tempDateTime.until(ldt, ChronoUnit.MONTHS);
                tempDateTime = tempDateTime.plusMonths(months);

                long days = tempDateTime.until(ldt, ChronoUnit.DAYS);
                tempDateTime = tempDateTime.plusDays(days);

                long hours = tempDateTime.until(ldt, ChronoUnit.HOURS);
                tempDateTime = tempDateTime.plusHours(hours);

                long minutes = tempDateTime.until(ldt, ChronoUnit.MINUTES);
                tempDateTime = tempDateTime.plusMinutes(minutes);

                long seconds = tempDateTime.until(ldt, ChronoUnit.SECONDS);

                Tools.runOnWithOutThreadFX(() -> {
                    if(ppr.getCounter().getTitle().contains("ППР")) {
                        ppr.setFoot(String.format("Текущие сутки ремонта: %d", daysStart + 1));
                    }
                    ppr.setTime(Math.toIntExact(months), Math.toIntExact(days), Math.toIntExact(hours), Math.toIntExact(minutes), Math.toIntExact(seconds));

                    if(years == 0 && months == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
                        ppr.setTitle(ppr.getCounter().getTitle() + " (Окончено!)");
                        iterator.remove();
                    }
                });
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
