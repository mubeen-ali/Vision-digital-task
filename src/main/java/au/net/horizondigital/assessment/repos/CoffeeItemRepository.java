package au.net.horizondigital.assessment.repos;

import au.net.horizondigital.assessment.entities.CoffeeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeItemRepository extends JpaRepository<CoffeeItem, Long> {
    CoffeeItem findByName(String name);
}
