package com.github.ivansenchukov.topjavagraduation.repository;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish);

    // NULL if not found
    List<Dish> get(Restaurant restaurant, LocalDate date);

    // NULL if not found
    Dish get(int id);

    // false if not found
    boolean delete(int id);
}
