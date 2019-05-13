package com.github.ivansenchukov.topjavagraduation.model;

import java.time.LocalDateTime;

public class Vote extends AbstractBaseEntity {

    //<editor-fold desc="Fields">
    private User user;
    private LocalDateTime date;
    private Restaurant restaurant;
    //</editor-fold>


    //<editor-fold desc="Constructors">
    public Vote() {
    }

    public Vote(Vote prototype) {
        this(prototype.getId(), prototype.getUser(), prototype.getRestaurant(), prototype.getDate());
    }

    public Vote(User user, Restaurant restaurant, LocalDateTime date) {
        this.user = user;
        this.date = date;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDateTime date) {
        super(id);
        this.user = user;
        this.date = date;
        this.restaurant = restaurant;
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    //</editor-fold>+
}
