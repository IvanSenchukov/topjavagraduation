package com.github.ivansenchukov.topjavagraduation.web.dish;


import com.github.ivansenchukov.topjavagraduation.model.Dish;
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

    /**
     * Returns dish by given ID
     *
     * @param id - ID of wanted dish
     * @return - dish by ID
     */
    public Dish get(int id) {
        log.info("get dish with id |{}|", id);
        return dishService.get(id);
    }

    /**
     * Returns Restaurant menu by restaurantId and date
     *
     * @param restaurantId   - ID of the Restaurant, which menu wants to see user
     * @param requestDateStr - date, on which user wants to see menu. If absent - today
     * @return - List of Dish objects, ordered by name
     */
    public List<Dish> getByRestaurantAndDate(int restaurantId, String requestDateStr) {
        log.info("get dish by restaurantId |{}| and date |{}|", restaurantId, requestDateStr);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        return dishService.getByRestaurantIdAndDate(restaurantId, date);
    }
    //</editor-fold>


    //<editor-fold desc="CREATE">

    /**
     * Create given dish
     * Returns location of created Dish object
     *
     * @param dish - Dish object to create. ID property must be NULL
     * @return - location of created Dish Object
     */
    public Dish create(Dish dish) {

        log.info("create dish |{}|", dish);

        checkNew(dish);
        return dishService.create(dish);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    /**
     * Update given dish
     *
     * @param dish  - Dish object to update.
     * @param id    - ID of dish, that must be updated.
     */
    public void update(Dish dish, int id) {
        log.info("update dish |{}| with id=|{}|", dish, id);
        assureIdConsistent(dish, id);
        dishService.update(dish);
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    /**
     * Delete Dish by given ID
     *
     * @param id - ID of dish to delete
     */
    public void delete(int id) {
        log.info("delete dish with id=|{}|", id);
        dishService.delete(id);
    }
    //</editor-fold>
}