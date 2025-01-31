package auction.services.interfaces;

import java.time.Duration;

public interface TimeListener {
    void onTimeUpdate(Duration timeLeft);
}