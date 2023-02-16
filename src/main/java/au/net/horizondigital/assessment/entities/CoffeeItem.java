package au.net.horizondigital.assessment.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "coffee_items")
@Getter
@Setter
public class CoffeeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;


    @JsonIgnore
    @ManyToMany(mappedBy = "menuItems", cascade = {CascadeType.ALL})
    private Set<CoffeeMenu> menus = new LinkedHashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<OrderItems> items = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeItem that = (CoffeeItem) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
