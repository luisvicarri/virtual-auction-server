package auction.services;

import auction.models.dtos.Response;
import auction.services.interfaces.TimeListener;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeService {

    private static final Logger logger = LoggerFactory.getLogger(TimeService.class);
    private static final long INTERVAL_SECONDS = 1;
    private Duration timeLeft;

    private final List<TimeListener> listeners = new ArrayList<>();
    private volatile boolean running = false; // Flag para controlar o timer
    private ScheduledExecutorService scheduler;

    public void startTimer(Duration duration) {
        if (running) {
            logger.warn("Timer is already running.");
            return;
        }

        scheduler = Executors.newScheduledThreadPool(1);
        running = true;
        timeLeft = duration;

        scheduler.scheduleWithFixedDelay(() -> {
            if (!running) {
                return;
            }
            // Se o tempo restante for maior que 1 segundo, decrementa normalmente.
            if (timeLeft.compareTo(Duration.ofSeconds(1)) > 0) {
                timeLeft = timeLeft.minusSeconds(1);
                updateTime(timeLeft);
            } else {
                // Se o tempo restante for 1 segundo ou menos, envia uma única atualização com zero e para o timer.
                updateTime(Duration.ZERO);
                running = false;  // Interrompe o timer
                scheduler.shutdown();
                logger.info("Auction ended. Timer stopped.");
            }
        }, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private void updateTime(Duration timeLeft) {
        Response response = new Response("TIME-UPDATE", "Updating time");
        response.addData("timeLeft", timeLeft.getSeconds());
        notifyTimeUpdate(timeLeft);
    }

    public void stopTimer() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        running = false;
        logger.info("Timer stopped.");
    }

    public void addListener(TimeListener listener) {
        listeners.add(listener);
    }

    public void notifyTimeUpdate(Duration timeLeft) {
        for (TimeListener listener : listeners) {
            listener.onTimeUpdate(timeLeft);
        }
    }
}