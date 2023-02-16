package au.net.horizondigital.assessment.controllers;

import au.net.horizondigital.assessment.bl.QueueHandler;
import au.net.horizondigital.assessment.entities.*;
import au.net.horizondigital.assessment.enums.OrderStatus;
import au.net.horizondigital.assessment.exceptions.DataNotFoundException;
import au.net.horizondigital.assessment.repos.ShopPublicData;
import au.net.horizondigital.assessment.services.DatabaseService;
import au.net.horizondigital.assessment.vo.QueueDetails;
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
public class ShopController {
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private QueueHandler queueHandler;

    @PostMapping("/configureShop")
    public ResponseEntity configureShop(@RequestBody CoffeeShop coffeeShop) {
        log.debug("coffee shop configuration request received for {}", coffeeShop);
        databaseService.configureShop(coffeeShop);
        return new ResponseEntity<>("Shop Configured Successfully", HttpStatus.OK);
    }


    @GetMapping("/fetchNearestShops")
    public ResponseEntity fetchNearestShops(@RequestParam String location) {
        log.info("Going to fetch nearest shops for location: {}", location);
        List<ShopPublicData> shops = databaseService.fetchNearestShopsByLocation(location);
        return new ResponseEntity(shops, HttpStatus.OK);
    }

    @GetMapping("/fetchShopMenu")
    public ResponseEntity fetchShopMenuItems(@RequestParam long shopId) {
        log.info("Going to fetch menu for shopId: {}", shopId);
        Set<CoffeeItem> menuItems = databaseService.fetchShopMenu(shopId);
        log.info("Total {} items found in menu of shopId {}", menuItems.size(), shopId);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

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
