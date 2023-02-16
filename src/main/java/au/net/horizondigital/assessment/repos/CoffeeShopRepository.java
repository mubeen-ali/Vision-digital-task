package au.net.horizondigital.assessment.repos;

import au.net.horizondigital.assessment.entities.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, Long> {
    List<ShopPublicData> findAllByLocation(String location);
}
