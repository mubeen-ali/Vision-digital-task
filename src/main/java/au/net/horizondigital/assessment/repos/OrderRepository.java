package au.net.horizondigital.assessment.repos;

import au.net.horizondigital.assessment.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByShopIdAndStatusOrderByIdAsc(long shopId, char status);
    List<Order> findAllByShopEqualsAndStatusOrderByIdAsc(long shopId, char status);
}
