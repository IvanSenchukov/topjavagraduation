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

import java.time.LocalDate;
import java.time.LocalDateTime;
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

// todo remake all tests in scope of voting dates
// todo - make test, when one user make vote for another one
public abstract class AbstractVoteServiceTest extends AbstractServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AbstractVoteServiceTest.class.getName());

    @Autowired
    protected LocalTime stopVotingTime;

    @Autowired
    protected VoteService service;


    //<editor-fold desc="MAKE VOTE">
    @Test
    void makeVote() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, TEST_DATE.plusDays(1));
        Vote created = service.makeVote(new Vote(newVote), TEST_DATE.plusDays(1).atStartOfDay());
        newVote.setId(created.getId());

        assertMatch(newVote, created);
        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE.plusDays(1)), newVote);
    }

    @Test
    void makeVoteIfPresentAndTimeIsUp() throws Exception {

        LocalDateTime testDateTime = TEST_DATE.atTime(stopVotingTime).plusMinutes(1);

        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, testDateTime.toLocalDate());
        assertThrows(RestrictedOperationException.class, () ->
                service.makeVote(new Vote(newVote), testDateTime));
    }

    @Test
    void makeVoteIfPresent() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, TEST_DATE);
        Vote updated = service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay());
        newVote.setId(updated.getId());
        assertMatch(newVote, updated);
        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.VABI_VOBBLE, TEST_DATE), updated, SECOND_USER_VOTE);
    }

    @Test
    void makeVoteByAdmin() throws Exception {

        Vote newVote = new Vote(UserTestData.ADMIN, RestaurantTestData.MCDONNELS, TEST_DATE);
        assertThrows(RestrictedOperationException.class, () ->
                service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay()));
    }

    @Test
    void makeVoteWithEmptyRestaurant() throws Exception {

        Vote newVote = new Vote(USER_SECOND, null, TEST_DATE);
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay()));
    }

    @Test
    void makeVoteWithEmptyDate() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, null);
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay()));
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
    void getByDate() throws Exception {
        List<Vote> votes = service.getByDate(TEST_DATE);
        assertMatch(votes, FIRST_USER_VOTE, SECOND_USER_VOTE);
    }

    @Test
    void getByUser() throws Exception {
        LocalDateTime testDateTime = getTestDateTimeAllowed(stopVotingTime);

        List<Vote> votes = service.getByUser(UserTestData.USER_FIRST_ID);
        assertMatch(votes, YESTERDAY_FIRST_USER_VOTE, FIRST_USER_VOTE);
    }

    @Test
    void getEmptyListByRestaurantAndDate() throws Exception {
        List<Vote> votes = service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE.minusDays(2));
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

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, TEST_DATE);
        Vote created = service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay());

        service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, getTestDateTimeAllowed(stopVotingTime));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteByAdmin() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, TEST_DATE);
        Vote created = service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay());

        service.delete(FIRST_USER_VOTE_ID, UserTestData.ADMIN, getTestDateTimeAllowed(stopVotingTime));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteByAdminWhenTimeIsUp() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, TEST_DATE);
        Vote created = service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay());

        service.delete(FIRST_USER_VOTE_ID, UserTestData.ADMIN, TEST_DATE.atTime(stopVotingTime));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteWhenTimeIsUp() throws Exception {

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, TEST_DATE);
        Vote created = service.makeVote(new Vote(newVote), TEST_DATE.atStartOfDay());

        assertThrows(RestrictedOperationException.class, () ->
                service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, TEST_DATE.atTime(stopVotingTime).plusMinutes(1)));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), FIRST_USER_VOTE, created);
    }

    @Test
    void deleteByWrongUser() throws Exception {

        LocalDateTime testDateTime = getTestDateTimeAllowed(stopVotingTime);

        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, TEST_DATE);
        Vote created = service.makeVote(new Vote(newVote), testDateTime);

        assertThrows(RestrictedOperationException.class, () ->
                service.delete(FIRST_USER_VOTE_ID, USER_SECOND, TEST_DATE.atTime(stopVotingTime)));

        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE), FIRST_USER_VOTE, created);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, UserTestData.ADMIN, getTestDateTimeAllowed(stopVotingTime)));
    }
    //</editor-fold>
}