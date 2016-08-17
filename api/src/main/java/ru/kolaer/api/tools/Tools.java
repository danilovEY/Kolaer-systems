package ru.kolaer.api.tools;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;

public class Tools {
    private static final Logger LOG = LoggerFactory.getLogger(Tools.class);

    public static Date convertToDate(final LocalDate date) {
        if(date == null)
            throw new IllegalArgumentException("Date is null!");
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convertToLocalDate(final Date date) {
        if(date == null)
            throw new IllegalArgumentException("LocalDate is null!");
        return LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    public static void runOnThreadFXAndWain(final Runnable runnable, final long time, final TimeUnit timeUnit) {
        Objects.requireNonNull(runnable, "runnable");

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        if(Platform.isFxApplicationThread()) {
            runnable.run();
            countDownLatch.countDown();
        } else {
            Platform.runLater( () -> {
                runnable.run();
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await(time, timeUnit);
        } catch (InterruptedException e) {
            LOG.error("Превышено ожидание потока!", e);
        }

    }

    public static void runOnThreadFX(final Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");

        if(Platform.isFxApplicationThread()) {
        	final ExecutorService threadOnFX = Executors.newSingleThreadExecutor();
        	CompletableFuture.runAsync(() -> {
        		Platform.runLater(runnable);
        		threadOnFX.shutdown();
        	}, threadOnFX);
        } else {
            Platform.runLater(runnable);
        }
    }
}