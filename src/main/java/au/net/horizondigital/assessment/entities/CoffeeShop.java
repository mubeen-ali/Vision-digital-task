package au.net.horizondigital.assessment.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.sql.Time;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "coffee_shops")
@Data
@NoArgsConstructor
public class CoffeeShop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "location")
    private String location;

    @Column(name = "number_of_queues", nullable = false)
    @Max(3)
    private int numberOfQueues;

    @Column(name = "queue_max_size", nullable = false)
    private int queueMaxSize;

    @Column(name = "opening_time", nullable = false)
    @NonNull
    private Time openingTime;

    @Column(name = "closing_time", nullable = false)
    @NonNull
    private Time closingTime;

    @OneToOne
    @JoinColumn(name = "coffee_menu_id")
    private CoffeeMenu coffeeMenu;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new LinkedHashSet<>();

}
