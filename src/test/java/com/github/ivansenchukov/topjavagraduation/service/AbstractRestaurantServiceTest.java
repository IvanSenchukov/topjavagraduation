package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public abstract class AbstractRestaurantServiceTest extends AbstractServiceTest {


    @Autowired
    protected RestaurantService service;

    //<editor-fold desc="CREATE">
    @Test
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant("Zoo Beer");
        Restaurant created = service.create(new Restaurant(newRestaurant));
        newRestaurant.setId(created.getId());
        assertMatch(newRestaurant, created);
        assertMatch(service.getAll(), MCDONNELS, VABI_VOBBLE, newRestaurant);
    }
    //</editor-fold>


    //<editor-fold desc="GET">
    @Test
    void get() throws Exception {
        Restaurant restaurant = service.get(VABI_VOBBLE_ID);
        assertMatch(restaurant, VABI_VOBBLE);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getAll() throws Exception {
        List<Restaurant> all = service.getAll();
        assertMatch(all, MCDONNELS, VABI_VOBBLE);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(VABI_VOBBLE);
        updated.setName("UPDATED Vabi-Vobble");
        service.update(new Restaurant(updated));
        assertMatch(service.get(VABI_VOBBLE_ID), updated);
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    @Test
    void delete() throws Exception {
        service.delete(VABI_VOBBLE_ID);
        assertMatch(service.getAll(), MCDONNELS);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }
    //</editor-fold>
}