package au.net.horizondigital.assessment.repos;

import au.net.horizondigital.assessment.entities.CoffeeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<CoffeeMenu, Integer> {
}
