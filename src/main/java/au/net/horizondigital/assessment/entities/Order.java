package au.net.horizondigital.assessment.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private CoffeeShop shop;

    @Column(name = "status", nullable = false, columnDefinition = "char default 'P'")
    private char status;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "last_updated_at", nullable = false)
    private Timestamp lastUpdatedAt;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<OrderItems> orderItems = new LinkedHashSet<>();

}
