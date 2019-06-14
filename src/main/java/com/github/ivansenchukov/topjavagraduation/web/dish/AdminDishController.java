package com.github.ivansenchukov.topjavagraduation.web.dish;


import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// todo - should add example values for Swagger in the future
@Api(description = "Endpoint for admins to work with Dishes")
@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController extends AbstractDishController {

    public static final String REST_URL = WebUtil.ADMIN_URL + "/dishes";

    @ApiOperation(value = "Returns dish by given ID")
    @Override
    @GetMapping("/{id}")
    public Dish get(
            @ApiParam(required = true, value = "ID of wanted Dish")
            @PathVariable
                    int id) {
        return super.get(id);
    }


    @ApiOperation(value = "Returns Restaurant menu by restaurantId and date")
    @Override
    @GetMapping("/by_restaurant")
    public List<Dish> getByRestaurantAndDate(
            @ApiParam(required = true, value = "ID of the Restaurant, which menu wants to see user")
            @RequestParam
                    int restaurantId,
            @ApiParam(value = "YYYY-MM-DD - date, on which user wants to see menu. If absent - Today")
            @RequestParam(required = false, name = "requestDate")
                    String requestDateStr) {
        return super.getByRestaurantAndDate(restaurantId, requestDateStr);
    }


    @ApiOperation(value = "Creates a given Dish")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(
            @ApiParam(required = true, value = "Dish object to create. ID property must be NULL")
            @RequestBody
                    Dish dish) {
        Dish created = super.create(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    /**
     * Update given dish
     *
     * @param dish - Dish object to update.
     * @param id   - ID of dish, that must be updated.
     */
    @ApiOperation(value = "Update a given Dish")
    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(
            @ApiParam(required = true, value = "Dish object to update.")
            @RequestBody
                    Dish dish,
            @ApiParam(required = true, value = "ID of Dish, that must be updated.")
            @PathVariable
                    int id) {
        super.update(dish, id);
    }

    @ApiOperation(value = "Delete Dish by given ID")
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @ApiParam(required = true, value = "ID of dish to delete")
            @PathVariable
                    int id) {
        super.delete(id);
    }
}