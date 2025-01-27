package auction.main;

import auction.views.frames.Frame;

public class ServerAuctionApp {

    public static Frame frame;
    
    public static void main(String[] args) {
//        Seeding seed = new Seeding();
//        seed.start();
        
        ServerAuctionApp.frame = new Frame();
        ServerAuctionApp.frame.start();
    }

}
