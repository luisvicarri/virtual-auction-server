package auction.controllers;

import auction.dispatchers.MessageDispatcher;
import auction.handlers.ClientConnected;
import auction.handlers.PlaceBid;
import auction.repositories.BiddingRepository;
import auction.repositories.ItemRepository;
import auction.repositories.UserRepository;
import auction.services.AuctionService;
import auction.services.BiddingService;
import auction.services.ItemService;
import auction.services.MulticastService;
import auction.services.TimeService;
import auction.services.UserService;
import auction.stubs.UserServiceStub;

public final class AppController {

    private final UserController userController;
    private final UserServiceStub userStub;
    private final MulticastController multicastController;
    private final TimeController timeController;
    private final BiddingController biddingController;
    private final ItemController itemController;

    public AppController() {
        this.userController = configUserController();
        this.userStub = configUserStub();
        this.multicastController = configMulticastController();
        this.timeController = configTimeController();
        this.timeController.addListener(multicastController);
        this.biddingController = configBiddingController();
        this.itemController = configItemController();
        
        addHandlers();
    }

    private void addHandlers() {
        MessageDispatcher dispatcher = getMulticastController().getDispatcher();
        dispatcher.registerHandler("CLIENT-CONNECTED", new ClientConnected(new AuctionService()));
        dispatcher.registerHandler("NEW-BID", new PlaceBid(new AuctionService()));
    }
    
    private ItemController configItemController() {
        ItemRepository repository = new ItemRepository();
        ItemService service = new ItemService(repository);
        return new ItemController(service);
    }
    
    private BiddingController configBiddingController() {
        BiddingRepository repository = new BiddingRepository();
        BiddingService service = new BiddingService(repository);
        return new BiddingController(service);
    }

    private TimeController configTimeController() {
        TimeService service = new TimeService();
        return new TimeController(service);
    }

    private MulticastController configMulticastController() {
        MulticastService service = new MulticastService();
        return new MulticastController(service);
    }

    private UserController configUserController() {
        UserRepository repository = new UserRepository();
        UserService service = new UserService(repository);
        return new UserController(service);
    }

    private UserServiceStub configUserStub() {
        UserRepository repository = new UserRepository();
        UserService service = new UserService(repository);
        return new UserServiceStub(service);
    }

    public UserController getUserController() {
        return userController;
    }

    public UserServiceStub getUserStub() {
        return userStub;
    }

    public MulticastController getMulticastController() {
        return multicastController;
    }

    public TimeController getTimeController() {
        return timeController;
    }

    public BiddingController getBiddingController() {
        return biddingController;
    }

    public ItemController getItemController() {
        return itemController;
    }

}