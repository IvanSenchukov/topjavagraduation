package com.github.ivansenchukov.topjavagraduation.web.vote;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.util.SecurityUtil;
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
// todo - add tests for new methods
@Api(description = "Endpoint for common users to work with Votes")
@RestController
@RequestMapping(value = CommonVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonVoteController extends AbstractVoteController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/votes";

    //<editor-fold desc="GET">

    @Override
    @ApiOperation(value = "Returns list of Votes for given Restaurant on given Date")
    @GetMapping("/by_restaurant")
    public List<Vote> getByRestaurantAndDate(
            @ApiParam(required = true, value = "ID of Restaurant")
            @RequestParam
                    int restaurantId,
            @ApiParam(required = false, value = "Date on which we want to see the votes. Today if absent")
            @RequestParam(required = false, name = "requestDate")
                    String requestDateStr
    ) {
        return super.getByRestaurantAndDate(restaurantId, requestDateStr);
    }

    @Override
    @ApiOperation(value = "Returns list of all Votes on a given Date")
    @GetMapping("/by_date")
    public List<Vote> getByDate(
            @ApiParam(required = false, value = "Date on which we want to see the votes. Today if absent")
            @RequestParam(required = false, name = "requestDate")
                    String requestDateStr
    ) {
        return super.getByDate(requestDateStr);
    }

    @GetMapping("/my_votes")
    @ApiOperation(value = "Returns list of all User's Votes")
    public List<Vote> getMyVotes() {
        return super.getByUserId(SecurityUtil.authUserId());
    }

    //</editor-fold>


    //<editor-fold desc="MAKE VOTE">

    @ApiOperation(value = "Make new Vote for given Restaurant on a given Date. " +
            "Be careful - you can make only one Vote in a day! " +
            "Also, if you change your mind, you must make your vote before 11:00!")
    // todo - change this hardcode - use LocalTime stopTime bean or corresponding property
    @PostMapping
    public ResponseEntity<Vote> makeVote(
            @ApiParam(required = true, value = "ID of Restaurant")
            @RequestParam int restaurantId,
            @ApiParam(required = false, value = "Date on which we want to make our Vote. Today if absent")
            @RequestParam(required = false) String requestDate
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
    @ApiOperation(value = "Delete Vote by given ID." +
            "User can delete Their own Votes." +
            "If time is after 11:00 your vote won't be deleted!")
    // todo - change this hardcode - use LocalTime stopTime bean or corresponding property
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @ApiParam(required = true, value = "ID of Vote to Delete")
            @PathVariable
                    int id
    ) throws RestrictedOperationException {

        super.delete(id);

    }

    //</editor-fold>
}
