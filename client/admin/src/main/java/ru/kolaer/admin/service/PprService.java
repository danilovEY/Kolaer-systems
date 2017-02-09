package ru.kolaer.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 25.08.2016.
 */
public class PprService implements Service {
    private final Logger LOG = LoggerFactory.getLogger(PprService.class);
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
        final Counter[] counters = this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getCounterTable().getAllCounters();

        if(counters == null || counters.length == 0) {
            this.isRun = false;
            return;
        }

        this.isRun = true;

        final List<StaticViewPPR> pprs = new ArrayList<>();

        final Date dateNow = new Date();
        for(final Counter counter : counters) {
            if(counter.getStart() == null || counter.getEnd() == null ||
                    counter.getEnd().before(dateNow))
                continue;

            final StaticViewPPR staticViewPPR = new StaticViewPPR(counter);
            staticViewPPR.setTitle(counter.getTitle());
            staticViewPPR.setDescription(counter.getDescription());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(counter.getEnd());

            editorKit.getUISystemUS().getStatic().addStaticView(staticViewPPR);

            pprs.add(staticViewPPR);
        }

        while (this.isRun) {
            Tools.runOnThreadFX(() -> {
                Iterator<StaticViewPPR> iterator = pprs.iterator();
                while (iterator.hasNext()) {
                    final StaticViewPPR ppr = iterator.next();
                    final LocalDateTime dateTimeJson = editorKit.getUSNetwork().getKolaerWebServer().getServerTools().getCurrentDataTime();
                    final Date dateEnd = ppr.getCounter().getEnd();
                    final LocalDateTime ldt = LocalDateTime.ofInstant(dateEnd.toInstant(), ZoneId.of("Europe/Moscow"));

                    LocalDateTime tempDateTime = LocalDateTime.from(dateTimeJson);

                    long daysStart = ChronoUnit.DAYS.between(Tools.convertToLocalDate(ppr.getCounter().getStart()), dateTimeJson.toLocalDate());

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
                    ppr.setFoot(String.format("Текущие сутки ремонта: %d", daysStart + 1));
                    ppr.setTime(Math.toIntExact(months), Math.toIntExact(days), Math.toIntExact(hours), Math.toIntExact(minutes), Math.toIntExact(seconds));

                    if(years == 0 && months == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
                        ppr.setTitle(ppr.getCounter().getTitle() + " (Окончено!)");
                        iterator.remove();
                    }
                }
            });

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
