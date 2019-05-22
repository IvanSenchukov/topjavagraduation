package com.github.ivansenchukov.topjavagraduation.web.restaurant;


import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.to.RestaurantOfferTo;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = CommonRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonRestaurantController extends AbstractRestaurantController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/restaurants";


    //<editor-fold desc="GET">
    //todo - make documentation
    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }


    //todo - make documentation
    @Override
    @GetMapping(value = "/offer")
    public RestaurantOfferTo get(
            @RequestParam(required = true) int restaurantId,
            @RequestParam(required = false, name = "requestDate") String requestDateString // todo - map this right
    ) {

        return super.get(restaurantId, requestDateString);
    }
    //</editor-fold>
}
