package com.github.ivansenchukov.topjavagraduation.repository.inmemory;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.ivansenchukov.topjavagraduation.DishTestData.*;

public class InMemoryDishRepositoryImpl extends InMemoryBaseRepositoryImpl<Dish> implements DishRepository {

    public InMemoryDishRepositoryImpl() {
        refreshRepository();
    }

    public void refreshRepository() {
        entryMap.clear();
        entryMap.put(MCDONNELS_BURGER_ID, MCDONNELS_BURGER);
        entryMap.put(MCDONNELS_FRIES_ID, MCDONNELS_FRIES);
        entryMap.put(VABI_VOBBLE_SUSHI_ID, VABI_VOBBLE_SUSHI);
        entryMap.put(VABI_VOBBLE_SASHIMI_ID, VABI_VOBBLE_SASHIMI);
        entryMap.put(VABI_VOBBLE_BAD_DATE_ID, VABI_VOBBLE_BAD_DATE);
        entryMap.put(BAD_RESTAURANT_DISH_ID, BAD_RESTAURANT_DISH);
    }


    @Override
    public Dish save(Dish dish) {
        Assert.notNull(dish, "Dish entry must not be null");
        Assert.notNull(dish.getRestaurant(), "Dish 'restaurant' property must not be null");
        Assert.notNull(dish.getDate(), "Dish 'date' property must not be null");

        return super.save(dish);
    }

    @Override
    public Dish get(int id) {
        Dish dish = super.get(id);

        if (Objects.isNull(dish)) return dish;

        Objects.requireNonNull(dish.getRestaurant(), "Dish 'restaurant' property must not be null. Inconsistent Data in repository!");
        Objects.requireNonNull(dish.getDate(), "Dish 'date' property must not be null. Inconsistend Data in repository!");

        return dish;
    }

    @Override
    public List<Dish> get(Restaurant restaurant, LocalDate date) {
        return getCollection().stream()
                .filter(dish -> date.equals(dish.getDate()) && Objects.equals(dish.getRestaurant(), restaurant))
                .sorted(Comparator.comparing(Dish::getName))
                .collect(Collectors.toList());
    }
}
