package com.github.ivansenchukov.topjavagraduation.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Dish extends AbstractBaseEntity {

    //<editor-fold desc="Fields">
    private String name;
    private BigDecimal price;

    private Restaurant restaurant;
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
