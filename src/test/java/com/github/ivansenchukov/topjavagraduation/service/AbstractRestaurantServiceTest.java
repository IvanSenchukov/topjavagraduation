package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TODO - upgrade all tests after implementing model layer
public class AbstractRestaurantServiceTest extends AbstractServiceTest {


    @Autowired
    protected RestaurantService service;

    //<editor-fold desc="CREATE">
    @Test
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant("Zoo Beer");  // TODO - upgrade this after implementing domain model
        Restaurant created = service.create(new Restaurant(newRestaurant));
        newRestaurant.setId(created.getId());
        assertMatch(newRestaurant, created);
        assertMatch(service.getAll(), DUKE_SUSHI, MCDONNELS, PILZNER, VABI_VOBBLE, VOGUER, newRestaurant);
    }
    //</editor-fold>


    //<editor-fold desc="GET">
    @Test
    // TODO - try to find something like DataProvider in testng and use it here
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
        assertMatch(all, DUKE_SUSHI, MCDONNELS, PILZNER, VABI_VOBBLE, VOGUER);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(VOGUER);
        updated.setName("UPDATED Voguer");
        service.update(new Restaurant(updated));
        assertMatch(service.get(VOGUER_ID), updated);
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    // TODO - try to use something like Testng Dataprovider here
    @Test
    void delete() throws Exception {
        service.delete(VOGUER_ID);
        assertMatch(service.getAll(), DUKE_SUSHI, MCDONNELS, PILZNER, VABI_VOBBLE);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }
    //</editor-fold>
}