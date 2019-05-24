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
            @RequestParam int restaurantId,
            @RequestParam (required = false, name = "requestDate") String requestDateStr
    ) {
        return super.getByRestaurantAndDate(restaurantId, requestDateStr);
    }

    @Override
    @GetMapping("/by_user")
    public List<Vote> getByUserId(@RequestParam int userId) {
        return super.getByUserId(userId);
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
