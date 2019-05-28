package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.DishTestData.*;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.TEST_DATE;
import static org.junit.jupiter.api.Assertions.assertThrows;


public abstract class AbstractDishServiceTest extends AbstractServiceTest {


    @Autowired
    protected DishService service;

    //<editor-fold desc="CREATE">
    @Test
    void create() throws Exception {
        Dish newDish = new Dish("Mc'Donnels HUGE Burger", new BigDecimal(300), RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10));
        Dish created = service.create(new Dish(newDish));
        newDish.setId(created.getId());
        assertMatch(newDish, created);
        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10)), newDish, MCDONNELS_BURGER, MCDONNELS_FRIES);
    }

    @Test
    void createWithEmptyRestaurant() throws Exception {
        Dish newDish = new Dish("Mc'Donnels HUGE Burger", new BigDecimal(300), null, LocalDate.of(2019, 5, 10));
        assertThrows(IllegalArgumentException.class, () ->
                service.create(new Dish(newDish)));
    }

    @Test
    void createWithEmptyDate() throws Exception {
        Dish newDish = new Dish("Mc'Donnels HUGE Burger", new BigDecimal(300), RestaurantTestData.MCDONNELS, null);
        assertThrows(IllegalArgumentException.class, () ->
                service.create(new Dish(newDish)));
    }
    //</editor-fold>


    //<editor-fold desc="GET">
    @Test
    void get() throws Exception {
        Dish dish = service.get(MCDONNELS_BURGER_ID);
        assertMatch(dish, MCDONNELS_BURGER);
    }

    @Test
    void getByRestaurantAndDate() throws Exception {
        List<Dish> dishes = service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10));
        assertMatch(dishes, MCDONNELS_BURGER, MCDONNELS_FRIES);
    }

    @Test
    void getByRestaurantAndDateEmptyList() throws Exception {
        List<Dish> dishes = service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE.minusYears(1));
        assertMatch(dishes, Collections.EMPTY_LIST);
    }

    @Test
    void getByRestaurantIdAndDate() throws Exception {
        List<Dish> dishes = service.getByRestaurantIdAndDate(RestaurantTestData.MCDONNELS_ID, LocalDate.of(2019, 5, 10));
        assertMatch(dishes, MCDONNELS_BURGER, MCDONNELS_FRIES);
    }

    @Test
    void getByRestaurantIdAndDateEmptyList() throws Exception {
        List<Dish> dishes = service.getByRestaurantIdAndDate(RestaurantTestData.MCDONNELS_ID, TEST_DATE.minusYears(1));
        assertMatch(dishes, Collections.EMPTY_LIST);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getWithEmptyRestaurant() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(BAD_RESTAURANT_DISH_ID));
    }

    @Test
    void getEmptyDate() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(VABI_VOBBLE_BAD_DATE_ID));
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    @Test
    void update() throws Exception {
        Dish updated = new Dish(VABI_VOBBLE_SUSHI);
        updated.setName("UPDATED Vabi-Vobble Sushi");
        service.update(new Dish(updated));
        assertMatch(service.get(VABI_VOBBLE_SUSHI_ID), updated);
    }

    @Test
    void updateWithEmptyRestaurant() throws Exception {
        Dish updated = new Dish(VABI_VOBBLE_SUSHI);
        updated.setName("UPDATED Vabi-Vobble Sushi");
        updated.setRestaurant(null);
        assertThrows(IllegalArgumentException.class, () ->
                service.update(new Dish(updated)));
    }

    @Test
    void updateWithEmptyDate() throws Exception {
        Dish updated = new Dish(VABI_VOBBLE_SUSHI);
        updated.setName("UPDATED Vabi-Vobble Sushi");
        updated.setDate(null);
        assertThrows(IllegalArgumentException.class, () ->
                service.update(new Dish(updated)));
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    @Test
    void delete() throws Exception {
        service.delete(VABI_VOBBLE_SASHIMI_ID);
        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 5, 10)), VABI_VOBBLE_SUSHI);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }
    //</editor-fold>
}