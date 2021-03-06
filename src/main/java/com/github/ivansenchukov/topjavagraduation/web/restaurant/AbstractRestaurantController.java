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
    /**
     * Returns list of all restaurants that are in the repository
     *
     * @return List of all restaurants.
     */
    public List<Restaurant> getAllRestaurants() {
        log.info("get all restaurants");
        return restaurantService.getAll();
    }

    /**
     * Returns restaurant by given id
     *
     * @param id - id of wanted restaurant
     * @return - restaurant by id
     */
    public Restaurant getRestaurant(int id) {
        log.info("get restaurant by id=|{}|", id);
        return restaurantService.get(id);
    }

    /**
     * Returns RestaurantOffer object, that contains
     * restaurant name, menu(list of dishes), list of voters for that restaurant
     * for current date
     *
     * @param restaurantId          - ID of restaurant, which offer user want to see
     * @param requestDateStr     - Date of offer. If absent - method return restaurant offer for today
     * @return
     */
    @Transactional(readOnly = true)
    public RestaurantOfferTo get(int restaurantId, String requestDateStr) {
        log.info("get restaurantOfferTo by restaurantId=|{}| and date=|{}|", restaurantId, requestDateStr);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        Restaurant restaurant = restaurantService.get(restaurantId);
        List<Dish> dishes = dishService.getByRestaurantAndDate(restaurant, date);
        List<Vote> votes = voteService.getByRestaurantAndDate(restaurant, date);


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
    /**
     * Create given restaurant
     *
     * @param restaurant - restaurant to create. id property must be NULL
     * @return - created restaurant object
     */
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant |{}|", restaurant);
        checkNew(restaurant);
        return restaurantService.create(restaurant);
    }
    //</editor-fold>

    //<editor-fold desc="UPDATE">
    /**
     * Update given restaurant.
     *
     * @param restaurant - Restaurant to update
     * @param id         - id of updated restaurant
     */
    public void update(Restaurant restaurant, int id) {
        log.info("update restaurant |{}| with id=|{}|", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">
    /**
     * Delete restaurant by given id
     *
     * @param id - id of restaurant to delete
     */
    public void delete(int id) {
        log.info("delete restaurant with id=|{}|", id);
        restaurantService.delete(id);
    }
    //</editor-fold>


}
