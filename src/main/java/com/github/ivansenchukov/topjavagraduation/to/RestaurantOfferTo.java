package com.github.ivansenchukov.topjavagraduation.to;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class RestaurantOfferTo implements Serializable {

    //<editor-fold desc="Fields">
    @NotEmpty
    private LocalDate date;

    @NotEmpty
    private Restaurant restaurant;

    private List<Dish> dishes;

    private List<Vote> votes;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public RestaurantOfferTo() {
    }


    public RestaurantOfferTo(LocalDate date, Restaurant restaurant, List<Dish> dishes, List<Vote> votes) {
        this.date = date;
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.votes = votes;
    }
    //</editor-fold>

    //<editor-fold desc="Getters and Setters">
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "RestaurantOfferTo{" +
                "date=" + date +
                ", restaurant=" + restaurant +
                ", dishes=" + dishes +
                ", votes=" + votes +
                '}';
    }
}
