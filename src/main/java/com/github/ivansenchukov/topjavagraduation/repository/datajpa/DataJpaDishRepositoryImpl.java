package com.github.ivansenchukov.topjavagraduation.repository.datajpa;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaDishRepositoryImpl implements DishRepository {

    private final CrudDishRepository dishDataJpaRepository;

    @Autowired
    public DataJpaDishRepositoryImpl(CrudDishRepository dishDataJpaRepository) {
        this.dishDataJpaRepository = dishDataJpaRepository;
    }

    @Override
    public Dish save(Dish dish) {
        return dishDataJpaRepository.save(dish);
    }

    @Override
    public Dish get(int id) {
        return dishDataJpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dish> getByRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return dishDataJpaRepository.getByRestaurantAndDate(restaurant, date);
    }

    @Override
    public List<Dish> getByRestaurantIdAndDate(Integer restaurantId, LocalDate date) {
        return dishDataJpaRepository.getByRestaurantIdAndDate(restaurantId, date);
    }

    @Override
    public boolean delete(int id) {
        return dishDataJpaRepository.delete(id) != 0;
    }
}
