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
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long INTERVAL_SECONDS = 1;
    private Duration timeLeft;

    private List<TimeListener> listeners = new ArrayList<>();

    public void startTimer(Duration duration) {
        timeLeft = duration;

        scheduler.scheduleWithFixedDelay(() -> {
            if (timeLeft.isZero() || timeLeft.isNegative()) {
                System.out.println("Auction ended.");
                updateTime(Duration.ZERO);
                scheduler.shutdown();
            } else {
                timeLeft = timeLeft.minusSeconds(1);
                updateTime(timeLeft);
            }
        }, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);

    }

    private void updateTime(Duration timeLeft) {
//        logger.info("Sending time update: {}s", timeLeft.getSeconds());
        Response response = new Response("TIME-UPDATE", "Updating time");
        response.addData("timeLeft", timeLeft.getSeconds());
        notifyTimeUpdate(timeLeft);
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
