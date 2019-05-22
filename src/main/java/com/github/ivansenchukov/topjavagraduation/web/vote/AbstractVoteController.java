package com.github.ivansenchukov.topjavagraduation.web.vote;

import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public abstract class AbstractVoteController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    //<editor-fold desc="GET">
    //todo - make documentation
    // todo - make decision delete this or not
    @Deprecated
    public Vote get(int voteId) {
        log.info("get vote by id=|{}|", voteId);
        return voteService.get(voteId);
    }

    //todo - make documentation
    public List<Vote> getByRestaurantAndDate(int restaurantId, String requestDateStr) {
        log.info("get votes by restaurantId=|{}| and date=|{}|", restaurantId, requestDateStr);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        return voteService.getByRestaurantAndDate(restaurantId, date);
    }

    //todo - make documentation
    public List<Vote> getByUserId(int userId) {
        log.info("get votes by userId=|{}|", userId);

        return voteService.getByUser(userId);
    }
    //</editor-fold>

    //<editor-fold desc="CREATE">\
    //todo - make documentation
    // todo - think how handle RestrictedOperationException well
    // todo - think how it is best to make vote.
    @Transactional
    public Vote createVote(int restaurantId, String requestDateStr) throws RestrictedOperationException {

        User voteMaker = userService.get(SecurityUtil.authUserId());
        Restaurant restaurant = restaurantService.get(restaurantId);
        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();
        LocalTime now = LocalTime.now();

        Vote vote = new Vote(voteMaker, restaurant, date.atTime(now));


        log.info("User |{}| making vote |{}|...", voteMaker, vote);


        // todo - reengineer makeVote to take date input.
        return voteService.makeVote(vote, voteMaker);
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">
    //todo - make documentation
    @Transactional
    public void delete(int voteId) throws RestrictedOperationException {
        User voteMaker = userService.get(SecurityUtil.authUserId());
        LocalDateTime deletionDateTime = LocalDateTime.now();
        log.info("User |{}| deleting vote with id=|{}| at |{}|...", voteMaker, voteId, deletionDateTime);
        voteService.delete(voteId, voteMaker, deletionDateTime);
    }

    //</editor-fold>


}
