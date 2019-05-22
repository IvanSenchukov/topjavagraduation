package com.github.ivansenchukov.topjavagraduation.web.vote;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.util.SecurityUtil;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = CommonVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonVoteController extends AbstractVoteController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/votes";

    //<editor-fold desc="GET">

    //todo - make documentation
    @Override
    @GetMapping("/by_restaurant")
    public List<Vote> getByRestaurantAndDate(
            @RequestBody int restaurantId,
            @RequestBody String requestDateStr
    ) {
        return super.getByRestaurantAndDate(restaurantId, requestDateStr);
    }

    @GetMapping("/by_user")
    public List<Vote> getByUserId() {
        return super.getByUserId(SecurityUtil.authUserId());
    }

    //</editor-fold>


    //<editor-fold desc="MAKE VOTE">

    @PostMapping
    public ResponseEntity<Vote> makeVote(
            @RequestParam int restaurantId,
            @RequestParam String requestDate
    ) throws RestrictedOperationException {
        Vote created = super.createVote(restaurantId, requestDate);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //</editor-fold>


    //<editor-fold desc="DELETE">

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) throws RestrictedOperationException {
        super.delete(id);
    }

    //</editor-fold>
}
