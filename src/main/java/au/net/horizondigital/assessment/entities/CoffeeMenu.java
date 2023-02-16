package au.net.horizondigital.assessment.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "coffee_menu")
@Data
public class CoffeeMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "menu_items",
            joinColumns = @JoinColumn(name = "coffee_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "coffee_items_id"))
    private Set<CoffeeItem> menuItems = new LinkedHashSet<>();

}