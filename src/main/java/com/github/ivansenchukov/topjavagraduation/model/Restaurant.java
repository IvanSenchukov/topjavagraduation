package com.github.ivansenchukov.topjavagraduation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractBaseEntity implements Comparable<Restaurant> {


    //<editor-fold desc="Fields">
    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    String name;
    //</editor-fold>


    //<editor-fold desc="Constructors">
    public Restaurant() {
    }

    public Restaurant(Restaurant prototypeRestaurant) {
        this(prototypeRestaurant.getId(), prototypeRestaurant.getName());
    }

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>


    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(Restaurant o) {
        if (Objects.isNull(o) || Objects.isNull(o.getName())) return -1;
        if (Objects.isNull(getName())) return 1;
        return this.name.compareTo(o.getName());
    }
}
