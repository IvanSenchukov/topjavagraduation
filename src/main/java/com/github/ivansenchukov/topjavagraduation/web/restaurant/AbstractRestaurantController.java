package com.github.ivansenchukov.topjavagraduation.web.restaurant;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.assureIdConsistent;
import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService service;

    //<editor-fold desc="GET">
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return service.getAll();
    }

    public Restaurant get(int id) {
        log.info("get restaurant by id=|{}|", id);
        return service.get(id);
    }
    //</editor-fold>

    //<editor-fold desc="CREATE">
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant |{}|", restaurant);
        checkNew(restaurant);
        return service.create(restaurant);
    }
    //</editor-fold>

    //<editor-fold desc="UPDATE">
    public void update(Restaurant restaurant, int id) {
        log.info("update restaurant |{}| with id=|{}|", restaurant, id);
        assureIdConsistent(restaurant, id);
        service.update(restaurant);
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">
    public void delete(int id) {
        log.info("delete restaurant with id=|{}|", id);
        service.delete(id);
    }
    //</editor-fold>


}
