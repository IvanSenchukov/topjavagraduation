package com.github.ivansenchukov.topjavagraduation.web.restaurant;


import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = CommonRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonRestaurantController extends AbstractRestaurantController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/restaurants";


    //<editor-fold desc="GET">
    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return super.get(id);
    }
    //</editor-fold>
}
