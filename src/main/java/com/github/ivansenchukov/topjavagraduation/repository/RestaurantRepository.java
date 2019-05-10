package com.github.ivansenchukov.topjavagraduation.repository;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant user);

    List<Restaurant> getAll();

    // NULL if not found
    Restaurant get(int id);

    // false if not found
    boolean delete(int id);
}
