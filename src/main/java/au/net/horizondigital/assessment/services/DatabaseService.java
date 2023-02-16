package au.net.horizondigital.assessment.services;

import au.net.horizondigital.assessment.entities.*;
import au.net.horizondigital.assessment.enums.OrderStatus;
import au.net.horizondigital.assessment.exceptions.DataNotFoundException;
import au.net.horizondigital.assessment.repos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class DatabaseService {
    @Autowired
    private CoffeeShopRepository shopRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CoffeeItemRepository coffeeItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public void configureShop(CoffeeShop shop) {
        log.debug("Going to save menu");
        saveCoffeeMenu(shop.getCoffeeMenu());

        log.debug("Going to save shop");
        shopRepository.save(shop);
    }

    public List<ShopPublicData> fetchNearestShopsByLocation(String location) {
        log.info("Going to fetch nearest shops by location: {}", location);
        return shopRepository.findAllByLocation(location);
    }

    public Set<CoffeeItem> fetchShopMenu(long shopId) throws DataNotFoundException {
        Optional<CoffeeShop> optionalCoffeeShop = shopRepository.findById(shopId);
        if (!optionalCoffeeShop.isPresent()) {
            throw new DataNotFoundException("No shop exists with provided id");
        }
        CoffeeShop shop = optionalCoffeeShop.get();
        return null == shop ? null : shop.getCoffeeMenu().getMenuItems();
    }

    public Order placeOrder(Order order) {
        log.info("Going to place order for customerId {}", order.getCustomer().getId());
        order.setStatus(OrderStatus.PENDING.getStatus());
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Set<OrderItems> orderItems = order.getOrderItems();
        orderItems.forEach(i -> i.setOrder(order));
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order) {
        order.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public CoffeeShop fetchCoffeeShopById(long id) {
        log.info("Going to fetch coffee shop against id {}", id);
        return shopRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No Coffee shop exists with provided id"));
    }

    public List<Order> fetchPendingOrders(long shopId) {
        log.info("Going to fetch pending orders against shopId {}", shopId);
        return orderRepository.findAllByShopIdAndStatusOrderByIdAsc(shopId, OrderStatus.PENDING.getStatus());
    }

    public Customer fetchCustomerById(long id) {
        return (Customer) userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Provided User doesn't exists"));
    }

    public CoffeeMenu saveCoffeeMenu(CoffeeMenu coffeeMenu) {
        Set<CoffeeItem> items = coffeeMenu.getMenuItems();
        Set<CoffeeItem> savedItems = new LinkedHashSet<>();

        for (CoffeeItem item : items) {
            String itemName = item.getName();
            CoffeeItem existingItem = coffeeItemRepository.findByName(itemName);
            if (null != existingItem) {
                savedItems.add(existingItem);
            } else {
                savedItems.add(coffeeItemRepository.save(item));
            }
        }

        coffeeMenu.setMenuItems(savedItems);
        return menuRepository.save(coffeeMenu);
    }
}
