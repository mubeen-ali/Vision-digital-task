package au.net.horizondigital.assessment.controllers;

import au.net.horizondigital.assessment.bl.QueueHandler;
import au.net.horizondigital.assessment.entities.CoffeeItem;
import au.net.horizondigital.assessment.entities.Customer;
import au.net.horizondigital.assessment.entities.Order;
import au.net.horizondigital.assessment.entities.OrderItems;
import au.net.horizondigital.assessment.exceptions.DataNotFoundException;
import au.net.horizondigital.assessment.services.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CustomerController {
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private QueueHandler queueHandler;

    @PostMapping("/registerCustomer")
    public ResponseEntity registerCustomer(@RequestBody Customer customer) {
        log.info("Going to register customer with userId {}", customer.getUserId());
        Customer registeredCustomer = databaseService.registerCustomer(customer);
        return new ResponseEntity("Customer registered Successfully. id: " + registeredCustomer.getId(), HttpStatus.OK);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity placeOrder(@RequestBody Order order) throws InterruptedException {
        log.info("Going to place order for customerId {}", order.getCustomer().getId());
        order.setShop(databaseService.fetchCoffeeShopById(order.getShop().getId()));
        order.setCustomer(databaseService.fetchCustomerById(order.getCustomer().getId()));

        validateOrderItems(order);
        log.info("Going to fetch queue of shop {}", order.getShop().getId());
        BlockingQueue shopQueue = queueHandler.getShopQueue(order.getShop().getId());

        if (shopQueue.remainingCapacity() == 0) {
            log.warn("Queue is already full. Rejecting order for customer {}", order.getCustomer().getId());
            return new ResponseEntity("Shop's queue is full at the moment. Please try again later", HttpStatus.SERVICE_UNAVAILABLE);
        }

        log.info("Adding order to the queue of shop {} for customerId {}", order.getShop().getId(), order.getCustomer().getId());
//        shopQueue.put(order);

        log.info("Saving order to the database");
        shopQueue.put(databaseService.placeOrder(order));
        return new ResponseEntity("Order placed successfully", HttpStatus.OK);
    }

    private void validateOrderItems(Order order) {
        Set<CoffeeItem> menuItems = order.getShop().getCoffeeMenu().getMenuItems();
        Set<CoffeeItem> itemList = order.getOrderItems().stream().map(OrderItems::getItem).collect(Collectors.toSet());
        if (!menuItems.containsAll(itemList)) {
            throw new DataNotFoundException("Provided item id doesn't exits in shop's menu. Please provide valid item from menu ");

        }
    }
}
