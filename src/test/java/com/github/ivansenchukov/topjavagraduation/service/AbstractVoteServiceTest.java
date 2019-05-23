package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.assertMatch;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.*;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public abstract class AbstractVoteServiceTest extends AbstractServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AbstractVoteServiceTest.class.getName());

    @Autowired
    protected LocalTime stopVotingTime;

    @Autowired
    protected VoteService service;


    //<editor-fold desc="MAKE VOTE">
    @Test
    void makeVote() throws Exception {
        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime).plusDays(1));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);
        newVote.setId(created.getId());
        assertMatch(newVote, created);
        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE.plusDays(1)), newVote);
    }

    @Test
    void makeVoteIfPresentAndTimeIsUp() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, TEST_DATE.atTime(stopVotingTime).plusMinutes(1));
        assertThrows(RestrictedOperationException.class, () ->
                service.makeVote(new Vote(newVote), USER_FIRST));
    }

    @Test
    void makeVoteIfPresent() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, getTestDateTimeAllowed(stopVotingTime));
        Vote updated = service.makeVote(new Vote(newVote), USER_FIRST);
        newVote.setId(updated.getId());
        assertMatch(newVote, updated);
        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.VABI_VOBBLE, TEST_DATE), updated, SECOND_USER_VOTE);

    }

    @Test
    void makeVoteByAdmin() throws Exception {
        Vote newVote = new Vote(UserTestData.ADMIN, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        assertThrows(RestrictedOperationException.class, () ->
                service.makeVote(new Vote(newVote), ADMIN));
    }

    @Test
    void makeVoteWithEmptyRestaurant() throws Exception {
        Vote newVote = new Vote(USER_SECOND, null, getTestDateTimeAllowed(stopVotingTime));
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote), USER_SECOND));
    }

    @Test
    void makeVoteWithEmptyDate() throws Exception {
        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, null);
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote), USER_SECOND));
    }
    //</editor-fold>


    //<editor-fold desc="GET">
    @Test
    void get() throws Exception {
        Vote vote = service.get(FIRST_USER_VOTE_ID);
        assertMatch(vote, FIRST_USER_VOTE);
    }

    @Test
    void getByRestaurantAndDate() throws Exception {
        List<Vote> votes = service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE);
        assertMatch(votes, FIRST_USER_VOTE);
    }

    @Test
    void getByRestaurantIdAndDate() throws Exception {
        List<Vote> votes = service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS_ID, TEST_DATE);
        assertMatch(votes, FIRST_USER_VOTE);
    }

    @Test
    void getByUser() throws Exception {
        List<Vote> votes = service.getByUser(UserTestData.USER_FIRST_ID);
        assertMatch(votes, FIRST_USER_VOTE);
    }

    @Test
    void getEmptyListByRestaurantAndDate() throws Exception {
        List<Vote> votes = service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE.minusDays(1));
        assertMatch(votes, Collections.emptyList());
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getEmptyUser() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(VoteTestData.BAD_USER_VOTE_ID));
    }

    @Test
    void getWithEmptyRestaurant() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(VoteTestData.BAD_RESTAURANT_VOTE_ID));
    }

    @Test
    void getEmptyDate() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(VoteTestData.BAD_DATE_VOTE_ID));
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    @Test
    void delete() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);

        service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, getTestDateTimeAllowed(stopVotingTime));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteByAdmin() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);

        service.delete(FIRST_USER_VOTE_ID, UserTestData.ADMIN, getTestDateTimeAllowed(stopVotingTime));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteByAdminWhenTimeIsUp() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);

        service.delete(FIRST_USER_VOTE_ID, UserTestData.ADMIN, getTestDateTimeTooLate(stopVotingTime));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteWhenTimeIsUp() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);

        assertThrows(RestrictedOperationException.class, () ->
                service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, getTestDateTimeTooLate(stopVotingTime)));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), FIRST_USER_VOTE, created);
    }

    @Test
    void deleteByWrongUser() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);

        assertThrows(RestrictedOperationException.class, () ->
                service.delete(FIRST_USER_VOTE_ID, USER_SECOND, getTestDateTimeTooLate(stopVotingTime)));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), FIRST_USER_VOTE, created);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, UserTestData.ADMIN, getTestDateTimeAllowed(stopVotingTime)));
    }
    //</editor-fold>

    // todo - implement this functional
    @Test
    @Disabled
    void getVotesCount() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime));
        Vote created = service.makeVote(new Vote(newVote), USER_SECOND);

        Map<Restaurant, Integer> expected = new HashMap<>(2);
        expected.put(RestaurantTestData.MCDONNELS, 2);
        expected.put(RestaurantTestData.VABI_VOBBLE, 0);

        Map<Restaurant, Integer> restaurantVotesCount = service.getVotesCount(TEST_DATE, RestaurantTestData.MCDONNELS, RestaurantTestData.VABI_VOBBLE);
        assertMatch(restaurantVotesCount, expected);
    }

    // todo - implement this functional
    @Test
    @Disabled
    void getVotesCountWithZeroes() throws Exception {

        Map<Restaurant, Integer> expected = new HashMap<>(2);
        expected.put(RestaurantTestData.MCDONNELS, 0);
        expected.put(RestaurantTestData.VABI_VOBBLE, 0);

        Map<Restaurant, Integer> restaurantVotesCount = service.getVotesCount(TEST_DATE.minusDays(1), RestaurantTestData.MCDONNELS, RestaurantTestData.VABI_VOBBLE);
        assertMatch(restaurantVotesCount, expected);
    }
}