package com.github.ivansenchukov.topjavagraduation.web.restaurant;


import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.to.RestaurantOfferTo;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// todo - should add example values for Swagger in the future
@Api(description = "Endpoint for common users to work with Restaurants")
@RestController
@RequestMapping(value = CommonRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonRestaurantController extends AbstractRestaurantController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/restaurants";


    //<editor-fold desc="GET">
    @Override
    @ApiOperation(value = "Returns list of all Restaurants that are in the repository")
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return super.getAllRestaurants();
    }


    @Override
    @ApiOperation(value = "Returns RestaurantOffer object, that contains restaurant name, menu(list of dishes), list of voters for that restaurant for current date")
    @GetMapping(value = "/offer")
    public RestaurantOfferTo get(
            @ApiParam(required = true, value = "ID of wanted Restaurant Offer")
            @RequestParam(required = true)
                    int restaurantId,
            @ApiParam(required = false, value = "YYYY-MM-DD - Date, on which user wants to take Restaurant Offer. Today if absent.")
            @RequestParam(required = false, name = "requestDate")
                    String requestDateString // todo - map this right
    ) {

        return super.get(restaurantId, requestDateString);
    }
    //</editor-fold>
}
