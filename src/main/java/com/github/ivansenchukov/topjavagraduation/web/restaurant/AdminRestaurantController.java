package com.github.ivansenchukov.topjavagraduation.web.restaurant;


import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// todo - may change example values in future
@Api(description = "Endpoint for admins to work with Restaurants")
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController extends AbstractRestaurantController {

    public static final String REST_URL = WebUtil.ADMIN_URL + "/restaurants";


    //<editor-fold desc="GET">

    /**
     * Returns list of all restaurants that are in the repository
     *
     * @return List of all restaurants.
     */
    @ApiOperation(value = "Returns list of all Restaurants that are in the repository")
    @Override
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return super.getAllRestaurants();
    }

    /**
     * Returns restaurant by given id
     *
     * @param id - id of wanted restaurant
     * @return - restaurant by id
     */
    @ApiOperation(value = "Returns Restaurant by given ID")
    @Override
    @GetMapping("/{id}")
    public Restaurant getRestaurant(
            @ApiParam(required = true, value = "ID of wanted Restaurant") @PathVariable int id
    ) {
        return super.getRestaurant(id);
    }
    //</editor-fold>


    //<editor-fold desc="CREATE">

    /**
     * Create given restaurant
     *
     * @param restaurant - restaurant to create. id property must be NULL
     * @return - created restaurant object
     */
    @ApiOperation(value = "Saves given Restaurant to repository")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurant(
            @ApiParam(required = true, value = "Restaurant to create. ID property must be NULL") @RequestBody Restaurant restaurant
    ) {
        Restaurant created = super.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    /**
     * Update given restaurant.
     *
     * @param restaurant - Restaurant to update
     * @param id         - id of updated restaurant
     */
    @ApiOperation(value = "Update given Restaurant in repository")
    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(
            @ApiParam(value = "Restaurant to update") @RequestBody Restaurant restaurant,
            @ApiParam(value = "ID of Restaurant to update") @PathVariable int id
    ) {
        super.update(restaurant, id);
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">

    /**
     * Delete restaurant by given id
     *
     * @param id - id of restaurant to delete
     */
    @ApiOperation(value = "Delete Restaurant from repository by given ID")
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @ApiParam(value = "ID of Restaurant to delete") @PathVariable int id
    ) {
        super.delete(id);
    }
    //</editor-fold>


}
