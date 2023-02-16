package au.net.horizondigital.assessment.bl;

import au.net.horizondigital.assessment.entities.CoffeeShop;
import au.net.horizondigital.assessment.entities.Order;
import au.net.horizondigital.assessment.services.DatabaseService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Data
@Component
@Slf4j
public class QueueHandler {
    private Map<Long, BlockingQueue<Order>> shopQueues = new HashMap<>();

    @Autowired
    private DatabaseService databaseService;

    public BlockingQueue<Order> getShopQueue(long shopId) {
        if (!shopQueues.containsKey(shopId)) {
            log.info("Queue is not in memory for shopId {}, creating new one", shopId);
            CoffeeShop shop = databaseService.fetchCoffeeShopById(shopId);
            BlockingQueue<Order> queue = new ArrayBlockingQueue(shop.getQueueMaxSize());
            shopQueues.put(shop.getId(), queue);

            log.info("Going to populate queue from database for pending orders");
            List<Order> pendingOrders = databaseService.fetchPendingOrders(shopId);

            log.info("Total {} pending orders found from db for shopId {}", pendingOrders.size(), shopId);
            if (pendingOrders.size() > shop.getQueueMaxSize()) {
                log.warn("There are more pending orders than max queue size. For now only taking {} pending orders ", shop.getQueueMaxSize());
                queue.addAll(pendingOrders.stream().limit(shop.getQueueMaxSize()).collect(Collectors.toList()));
            } else {
                queue.addAll(pendingOrders);
            }
        }
        return shopQueues.get(shopId);
    }


}
