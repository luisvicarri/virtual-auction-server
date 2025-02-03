package auction.utils;

import auction.models.Bid;
import java.util.List;
import java.util.function.Consumer;

public class UIUpdateManager {
    
    private static Consumer<String> updateTimeLabel = s -> {};
    private static Consumer<List<Bid>> updateBidList = bids -> {};
    private static Consumer<String> updateWinningBidderLabel = s -> {};
    private static Consumer<String> updateCurrentBidLabel = s -> {};

    public static void setTimeUpdater(Consumer<String> updater) {
        updateTimeLabel = updater;
    }

    public static void setBidListUpdater(Consumer<List<Bid>> updater) {
        updateBidList = updater;
    }
    
    public static void setWinningBidderUpdater(Consumer<String> updater) {
        updateWinningBidderLabel = updater;
    }
    
    public static void setCurrentBid(Consumer<String> updater) {
        updateCurrentBidLabel = updater;
    }

    public static Consumer<String> getTimeUpdater() {
        return updateTimeLabel;
    }

    public static Consumer<List<Bid>> getBidListUpdater() {
        return updateBidList;
    }

    public static Consumer<String> getWinningBidderUpdater() {
        return updateWinningBidderLabel;
    }
    
    public static Consumer<String> getCurrentBidUpdater() {
        return updateCurrentBidLabel;
    }
}