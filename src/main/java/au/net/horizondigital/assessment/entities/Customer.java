package au.net.horizondigital.assessment.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("customer")
@Getter
@Setter
public class Customer extends User {
    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Set<OrderItems> orders = new LinkedHashSet<>();
}
