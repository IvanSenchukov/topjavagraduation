package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {

    private DishRepository repository;

    @Autowired
    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public Dish create(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish);
    }


    public Dish get(Integer id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Dish> get(Restaurant restaurant, LocalDate date) {
        return repository.get(restaurant, date);
    }

    public void update(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
//        Assert.notNull(dish.getDate(), "dish date property must not be null");
//        Assert.notNull(dish.getRestaurant(), "dish restaurant property must not be null");

        checkNotFoundWithId(repository.save(dish), dish.getId());
    }

    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
