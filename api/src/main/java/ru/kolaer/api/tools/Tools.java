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
    private static final SimpleDateFormat dateFormatUs = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("DD-MM-yyyy HH:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(Tools.class);

    public static String dateToString(Date date) {
        if(date == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static String dateTimeToString(Date date) {
        if(date == null) {
            return null;
        }

        return dateTimeFormat.format(date);
    }

    public static Date convertToDate(final LocalDate date) {
        if(date == null)
            throw new IllegalArgumentException("Date is null!");
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convertToLocalDate(final Date date) {
        if(date == null)
            throw new IllegalArgumentException("LocalDate is null!");
        return LocalDate.parse(dateFormatUs.format(date));
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

    @Deprecated
    public static void runOnThreadFX(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");

        if(Platform.isFxApplicationThread()) {
        	ExecutorService threadOnFX = Executors.newSingleThreadExecutor();
        	CompletableFuture.runAsync(() -> {
        		Platform.runLater(runnable);
        		threadOnFX.shutdown();
        	}, threadOnFX);
        } else {
            Platform.runLater(runnable);
        }
    }

    public static void runOnWithOutThreadFX(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");

        if(Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
}