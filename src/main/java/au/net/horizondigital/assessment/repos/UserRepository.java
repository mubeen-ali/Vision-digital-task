package au.net.horizondigital.assessment.repos;

import au.net.horizondigital.assessment.entities.Customer;
import au.net.horizondigital.assessment.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Customer findUserByUserId(String userId);
}
