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

    /**
     * Return vote by ID
     *
     * @param voteId
     * @return
     */
    public Vote get(int voteId) {
        log.info("get vote by id=|{}|", voteId);
        return voteService.get(voteId);
    }

    /**
     * List of voters for current restaurant.
     *
     * @param restaurantId
     * @param requestDateStr
     * @return
     */
    public List<Vote> getByRestaurantAndDate(int restaurantId, String requestDateStr) {
        log.info("get votes by restaurantId=|{}| and date=|{}|", restaurantId, requestDateStr);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        return voteService.getByRestaurantAndDate(restaurantId, date);
    }


    /**
     * List of voters for current day.
     *
     * @param requestDateStr
     * @return
     */
    //todo - make implementation in admin and common controllers
    public List<Vote> getByDate(String requestDateStr) {
        log.info("get votes by date=|{}|", requestDateStr);

        LocalDate date = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        return voteService.getByDate(date);
    }


    /**
     * Return all user votes for all time.
     * It is a user history of votes.
     *
     * @param userId
     * @return
     */
    public List<Vote> getByUserId(int userId) {
        log.info("get votes by userId=|{}|", userId);

        return voteService.getByUser(userId);
    }
    //</editor-fold>

    //<editor-fold desc="CREATE">\

    /**
     * Make vote by Restaurant ID and Date
     *
     * @param restaurantId
     * @param requestDateStr
     * @return
     * @throws RestrictedOperationException
     */
    @Transactional
    public Vote createVote(int restaurantId, String requestDateStr) throws RestrictedOperationException {

        User voteMaker = userService.get(SecurityUtil.authUserId());
        Restaurant restaurant = restaurantService.get(restaurantId);
        LocalDate votingDate = requestDateStr != null
                ? LocalDate.parse(requestDateStr)   // todo - map this in request parameter.
                : LocalDate.now();

        Vote vote = new Vote(voteMaker, restaurant, votingDate);


        log.info("User |{}| making vote |{}|...", vote.getUser(), vote);

        return voteService.makeVote(vote, LocalDateTime.now());
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">

    /**
     * Delete vote by id
     *
     * @param voteId
     * @throws RestrictedOperationException
     */
    @Transactional
    public void delete(int voteId) throws RestrictedOperationException {
        User voteMaker = userService.get(SecurityUtil.authUserId());
        LocalDateTime deletionDateTime = LocalDateTime.now();
        log.info("User |{}| deleting vote with id=|{}| at |{}|...", voteMaker, voteId, deletionDateTime);
        voteService.delete(voteId, voteMaker, deletionDateTime);
    }

    //</editor-fold>


}
