package com.github.ivansenchukov.topjavagraduation.web.dish;


import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController extends AbstractDishController {

    public static final String REST_URL = WebUtil.ADMIN_URL + "/dishes";

    //todo - make documentation
    //todo - make tests
    @Override
    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        return super.get(id);
    }

    //todo - make documentation
    //todo - make tests
    @Override
    @GetMapping("/by_restaurant")
    public List<Dish> getByRestaurantAndDate(
            @RequestParam int restaurantId,
            @RequestParam (required = false, name = "requestDate") String requestDateStr
    ) {
        return super.getByRestaurantAndDate(restaurantId, requestDateStr);
    }


    //todo - make documentation
    //todo - make tests
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@RequestBody Dish dish) {
        Dish created = super.create(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //todo - make documentation
    //todo - make tests
    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id) {
        super.update(dish, id);
    }

    //todo - make documentation
    //todo - make tests
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}