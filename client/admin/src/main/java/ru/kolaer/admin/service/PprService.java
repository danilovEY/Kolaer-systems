package ru.kolaer.admin.service;

import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by danilovey on 25.08.2016.
 */
public class PprService implements Service {
    private boolean isRun = false;
    private final UniformSystemEditorKit editorKit;

    public PprService(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
    }

    @Override
    public boolean isRunning() {
        return this.isRun;
    }

    @Override
    public String getName() {
        return "ППР счетчик";
    }

    @Override
    public void stop() {
        this.isRun = false;
    }

    @Override
    public void run() {
        this.isRun = true;


        final Counter[] counters = this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getCounterTable().getAllCounters();
        final List<PPR> pprs = new ArrayList<>();

        for(final Counter counter : counters) {
            final StaticViewPPR staticViewPPR = new StaticViewPPR(counter);
            staticViewPPR.setTitle(counter.getTitle());
            staticViewPPR.setDescription(counter.getDescription());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(counter.getEnd());

            editorKit.getUISystemUS().getStaticUs().addStaticView(staticViewPPR);

            pprs.add(staticViewPPR);
        }


        while (this.isRun) {
            Tools.runOnThreadFX(() -> {
                pprs.forEach( ppr -> {
                    try {
                        final LocalDateTime dateTimeJson = editorKit.getUSNetwork().getKolaerWebServer().getServerTools().getCurrentDataTime();
                        final Date dateEnd = ppr.getCounter().getEnd();
                        final LocalDateTime ldt = LocalDateTime.ofInstant(dateEnd.toInstant(), ZoneId.systemDefault());

                        LocalDateTime tempDateTime = LocalDateTime.from(dateTimeJson);
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
                        long daysStart = Period.between(Tools.convertToLocalDate(ppr.getCounter().getStart()), dateTimeJson.toLocalDate()).get(ChronoUnit.DAYS);
                        ppr.setFoot(String.format("Текущие сутки ремонта: %d", daysStart + 1));
                        ppr.setTime(Math.toIntExact(months), Math.toIntExact(days), Math.toIntExact(hours), Math.toIntExact(minutes), Math.toIntExact(seconds));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
