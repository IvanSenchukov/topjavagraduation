package com.github.ivansenchukov.topjavagraduation.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dishes")
@NamedQueries({
        @NamedQuery(name = Dish.GET_BY_RESTAURANT_AND_DATE, query = "SELECT d FROM Dish d WHERE d.restaurant=:restaurant AND d.date=:date ORDER BY d.name"),
        @NamedQuery(name = Dish.DELETE, query = "DELETE FROM Dish d WHERE d.id=:id"),
})
public class Dish extends AbstractBaseEntity {

    public static final String GET_BY_RESTAURANT_AND_DATE = "Dish.getByRestaurantAndDate";
    public static final String DELETE = "Dish.delete";

    //<editor-fold desc="Fields">
    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", columnDefinition = "BIGINT")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)   // TODO - doesn't work. Workaround in service
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull                                                // TODO - doesn't work. Workaround in service
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)                // TODO - doesn't work. Workaround in service
    @NotNull                                                // TODO - doesn't work. Workaround in service
    private LocalDate date;
    //</editor-fold>


    //<editor-fold desc="Constructors">
    public Dish() {

    }

    public Dish(Dish prototype) {
        this(prototype.getId(), prototype.getName(), prototype.getPrice(), prototype.getRestaurant(), prototype.getDate());
    }

    public Dish(String name, BigDecimal price, Restaurant restaurant, LocalDate date) {
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
        this.date = date;
    }

    public Dish(Integer id, String name, BigDecimal price, Restaurant restaurant, LocalDate date) {
        super(id);
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
        this.date = date;
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    //</editor-fold>
}
