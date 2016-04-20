package ru.kolaer.api.tools;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Tools {
    private static final Logger LOG = LoggerFactory.getLogger(Tools.class);

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
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
}