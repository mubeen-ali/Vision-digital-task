package au.net.horizondigital.assessment.controllers;

import au.net.horizondigital.assessment.bl.QueueHandler;
import au.net.horizondigital.assessment.entities.CoffeeItem;
import au.net.horizondigital.assessment.entities.CoffeeShop;
import au.net.horizondigital.assessment.entities.Customer;
import au.net.horizondigital.assessment.entities.Order;
import au.net.horizondigital.assessment.enums.OrderStatus;
import au.net.horizondigital.assessment.repos.ShopPublicData;
import au.net.horizondigital.assessment.services.DatabaseService;
import au.net.horizondigital.assessment.vo.QueueDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(description = "Controller that expose coffee shop specific endpoints")
public class ShopController {
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private QueueHandler queueHandler;

    @ApiOperation(value = "Endpoint to configure coffee shop",
            notes = "This endpoint is to be used by shop owner for configuration of shop i.e. shop details, menu etc. Currently only single queue support is added")
    @PostMapping("/configureShop")
    public ResponseEntity configureShop(@RequestBody CoffeeShop coffeeShop) {
        log.debug("coffee shop configuration request received for {}", coffeeShop);
        databaseService.configureShop(coffeeShop);
        return new ResponseEntity<>("Shop Configured Successfully", HttpStatus.OK);
    }


    @ApiOperation(value = "Endpoint to find nearest shops",
            notes = "location parameter will be matched with location field of coffee shops and will be returned")
    @GetMapping("/fetchNearestShops")
    public ResponseEntity fetchNearestShops(@RequestParam String location) {
        log.info("Going to fetch nearest shops for location: {}", location);
        List<ShopPublicData> shops = databaseService.fetchNearestShopsByLocation(location);
        return new ResponseEntity(shops, HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint to fetch coffee menu of shop")
    @GetMapping("/fetchShopMenu")
    public ResponseEntity fetchShopMenuItems(@RequestParam long shopId) {
        log.info("Going to fetch menu for shopId: {}", shopId);
        Set<CoffeeItem> menuItems = databaseService.fetchShopMenu(shopId);
        log.info("Total {} items found in menu of shopId {}", menuItems.size(), shopId);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint to get queue details of coffee shop",
            notes = "This endpoint will be used by show operator to see queue size, and total number of waiting customers in queue")
    @GetMapping("/getQueueDetails")
    public ResponseEntity getQueueDetails(@RequestParam long shopId) {
        log.info("Going to fetch number of waiting customers in queue of shopId {}", shopId);
        CoffeeShop shop = databaseService.fetchCoffeeShopById(shopId);
        BlockingQueue queue = queueHandler.getShopQueue(shopId);
        QueueDetails queueDetails = new QueueDetails();
        queueDetails.setQueueSize(shop.getQueueMaxSize());
        queueDetails.setNoOfWaitingCustomers(queue.size());

        log.info("Queue size {}, current number of waiting customers {} for shopId {}", queueDetails.getQueueSize(), queueDetails.getNoOfWaitingCustomers(), shopId);
        return new ResponseEntity(queueDetails, HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint to get names of waiting customers",
            notes = "This endpoint will be used by shop operator to fetch list of names of customers that are waiting in queue")
    @GetMapping("/getWaitingCustomerNames")
    public ResponseEntity getWaitingCustomerNames(@RequestParam long shopId) {
        log.info("Going to fetch waiting customer's names for shopId {}", shopId);
        BlockingQueue<Order> queue = queueHandler.getShopQueue(shopId);
        if (queue.isEmpty()) {
            log.info("Queue is empty for shopId {}", shopId);
            return new ResponseEntity("Queue is currently empty", HttpStatus.OK);
        }
        List<String> customerNames = queue.stream().map(Order::getCustomer).map(Customer::getName).collect(Collectors.toList());
        return new ResponseEntity(customerNames, HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint to server a customer from queue",
            notes = "This endpoint will be used by shop operator, it will remove first customer from queue, serve it, and mark it's order as processed")
    @GetMapping("/serverCustomer")
    public ResponseEntity serverCustomer(@RequestParam long shopId) {
        BlockingQueue<Order> queue = queueHandler.getShopQueue(shopId);
        if (queue.isEmpty()) {
            log.info("Queue is empty for shopId {}", shopId);
            return new ResponseEntity("Queue is empty at the moment", HttpStatus.OK);
        }
        Order order = queue.poll();
        order.setStatus(OrderStatus.SERVED.getStatus());

        databaseService.updateOrder(order);

        String response = "Order # " + order.getId() + " served successfully";
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);

    }
}
