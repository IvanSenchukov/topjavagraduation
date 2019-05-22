package com.github.ivansenchukov.topjavagraduation.web.vote;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController extends AbstractVoteController {

    public static final String REST_URL = WebUtil.ADMIN_URL + "/votes";


    //<editor-fold desc="GET">

    //todo - make documentation
    @Override
    @GetMapping("/{voteId}")
    public Vote get(@PathVariable int voteId) {
        return super.get(voteId);
    }

    @Override
    @GetMapping("/by_restaurant")
    public List<Vote> getByRestaurantAndDate(
            @RequestBody int restaurantId,
            @RequestBody String requestDateStr
    ) {
        return super.getByRestaurantAndDate(restaurantId, requestDateStr);
    }

    @Override
    @GetMapping("/by_user")
    public List<Vote> getByUserId(@RequestBody int userId) {
        return super.getByUserId(userId);
    }

    //</editor-fold>


    //<editor-fold desc="MAKE VOTE">

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Vote makeVote(
            @RequestParam int restaurantId,
            @RequestParam String requestDateStr
    ) throws RestrictedOperationException {
        throw new RestrictedOperationException("Admins not allowed to make votes!");
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
