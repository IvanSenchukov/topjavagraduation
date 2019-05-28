package com.github.ivansenchukov.topjavagraduation.web.dish;


import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.assureIdConsistent;
import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNew;

public abstract class AbstractDishController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishService dishService;

    //<editor-fold desc="GET">
    //todo - make documentation
    public Dish get(int id) {
        log.info("get dish with id |{}|", id);
        return dishService.get(id);
    }

    //todo - make documentation
    public List<Dish> getByRestaurantAndDate(int restaurantId, String requestDateStr) {
        log.info("get dish by restaurantId |{}| and date |{}|", restaurantId, requestDateStr);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        return dishService.getByRestaurantIdAndDate(restaurantId, date);
    }
    //</editor-fold>


    //<editor-fold desc="CREATE">
    //todo - make documentation
    public Dish create(Dish dish) {

        log.info("create dish |{}|", dish);

        checkNew(dish);
        return dishService.create(dish);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    //todo - make documentation
    public void update(Dish dish, int id) {
        log.info("update dish |{}| with id=|{}|", dish, id);
        assureIdConsistent(dish, id);
        dishService.update(dish);
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    //todo - make documentation
    public void delete(int id) {
        log.info("delete dish with id=|{}|", id);
        dishService.delete(id);
    }
    //</editor-fold>
}