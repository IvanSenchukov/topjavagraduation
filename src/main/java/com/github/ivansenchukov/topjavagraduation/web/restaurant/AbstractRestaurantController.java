package com.github.ivansenchukov.topjavagraduation.web.restaurant;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.service.DishService;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.to.RestaurantOfferTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.assureIdConsistent;
import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DishService dishService;

    @Autowired
    private VoteService voteService;

    //<editor-fold desc="GET">
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantService.getAll();
    }

    public Restaurant get(int id) {
        log.info("get restaurant by id=|{}|", id);
        return restaurantService.get(id);
    }

    @Transactional(readOnly = true)
    public RestaurantOfferTo get(int id, String requestDateStr) {
        log.info("get restaurantOfferTo by id=|{}|", id);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        Restaurant restaurant = restaurantService.get(id);
        List<Dish> dishes = dishService.get(restaurant, date);
        List<Vote> votes = voteService.get(restaurant, date);


        RestaurantOfferTo restaurantOffer = new RestaurantOfferTo(
                date,
                restaurant,
                dishes,
                votes
        );

        return restaurantOffer;
    }
    //</editor-fold>

    //<editor-fold desc="CREATE">
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant |{}|", restaurant);
        checkNew(restaurant);
        return restaurantService.create(restaurant);
    }
    //</editor-fold>

    //<editor-fold desc="UPDATE">
    public void update(Restaurant restaurant, int id) {
        log.info("update restaurant |{}| with id=|{}|", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">
    public void delete(int id) {
        log.info("delete restaurant with id=|{}|", id);
        restaurantService.delete(id);
    }
    //</editor-fold>


}
