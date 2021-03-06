package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "dishes", allEntries = true)
    public Dish create(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
        Assert.notNull(dish.getDate(), "dish date property must not be null");
        Assert.notNull(dish.getRestaurant(), "dish restaurant property must not be null");

        return repository.save(dish);
    }


    public Dish get(Integer id) {
        return checkNotFoundWithId(repository.get(id), Dish.class, id);
    }

    @Cacheable(value = "dishes")
    public List<Dish> getByRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return repository.getByRestaurantAndDate(restaurant, date);
    }

    @Cacheable(value = "dishes")
    public List<Dish> getByRestaurantIdAndDate(Integer restaurantId, LocalDate date) {
        return repository.getByRestaurantIdAndDate(restaurantId, date);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void update(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
        Assert.notNull(dish.getDate(), "dish date property must not be null");
        Assert.notNull(dish.getRestaurant(), "dish restaurant property must not be null");

        checkNotFoundWithId(repository.save(dish), Dish.class, dish.getId());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), Dish.class, id);
    }
}
