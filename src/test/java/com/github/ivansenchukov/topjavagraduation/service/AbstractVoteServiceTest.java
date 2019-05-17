package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData;
import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public abstract class AbstractVoteServiceTest extends AbstractServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AbstractVoteServiceTest.class.getName());

    @Autowired
    protected VoteService service;

    public static final LocalDate TEST_DATE = LocalDate.of(2019, 05, 10);


    //<editor-fold desc="CREATE">
    @Test
    void makeVote() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed().plusDays(1));
        Vote created = service.makeVote(new Vote(newVote));
        newVote.setId(created.getId());
        assertMatch(newVote, created);
        assertMatch(service.get(RestaurantTestData.MCDONNELS, TEST_DATE.plusDays(1)), newVote);
    }

    @Test
    void makeVoteIfPresentAndTimeIsUp() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, getTestDateTimeTooLate());
        assertThrows(RestrictedOperationException.class, () ->
                service.makeVote(new Vote(newVote)));
    }

    @Test
    void makeVoteIfPresent() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, getTestDateTimeAllowed());
        Vote updated = service.makeVote(new Vote(newVote));
        newVote.setId(updated.getId());
        assertMatch(newVote, updated);
        assertMatch(service.get(RestaurantTestData.VABI_VOBBLE, TEST_DATE), updated, SECOND_USER_VOTE);

    }

    @Test
    void makeVoteByAdmin() throws Exception {
        Vote newVote = new Vote(UserTestData.ADMIN, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        assertThrows(RestrictedOperationException.class, () ->
                service.makeVote(new Vote(newVote)));
    }

    @Test
    void makeVoteWithEmptyRestaurant() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_SECOND, null, getTestDateTimeAllowed());
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote)));
    }

    @Test
    void makeVoteWithEmptyDate() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, null);
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote)));
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
        List<Vote> votes = service.get(RestaurantTestData.MCDONNELS, TEST_DATE);
        assertMatch(votes, FIRST_USER_VOTE);
    }

    @Test
    void getEmptyListByRestaurantAndDate() throws Exception {
        List<Vote> votes = service.get(RestaurantTestData.MCDONNELS, TEST_DATE.minusDays(1));
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

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        Vote created = service.makeVote(new Vote(newVote));

        service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, getTestDateTimeAllowed());

        assertMatch(service.get(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteByAdmin() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        Vote created = service.makeVote(new Vote(newVote));

        service.delete(FIRST_USER_VOTE_ID, UserTestData.ADMIN, getTestDateTimeAllowed());

        assertMatch(service.get(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteByAdminWhenTimeIsUp() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        Vote created = service.makeVote(new Vote(newVote));

        service.delete(FIRST_USER_VOTE_ID, UserTestData.ADMIN, getTestDateTimeTooLate());

        assertMatch(service.get(RestaurantTestData.MCDONNELS, TEST_DATE), created);
    }

    @Test
    void deleteWhenTimeIsUp() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        Vote created = service.makeVote(new Vote(newVote));

        assertThrows(RestrictedOperationException.class, () ->
                service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, getTestDateTimeTooLate()));

        assertMatch(service.get(RestaurantTestData.MCDONNELS, TEST_DATE), FIRST_USER_VOTE, created);
    }

    @Test
    void deleteByWrongUser() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        Vote created = service.makeVote(new Vote(newVote));

        assertThrows(RestrictedOperationException.class, () ->
                service.delete(FIRST_USER_VOTE_ID, UserTestData.USER_SECOND, getTestDateTimeTooLate()));

        assertMatch(service.get(RestaurantTestData.MCDONNELS, TEST_DATE), FIRST_USER_VOTE, created);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, UserTestData.ADMIN, getTestDateTimeAllowed()));
    }
    //</editor-fold>

    @Test
    void getVotesCount() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
        Vote created = service.makeVote(new Vote(newVote));

        Map<Restaurant, Integer> expected = new HashMap<>(2);
        expected.put(RestaurantTestData.MCDONNELS, 2);
        expected.put(RestaurantTestData.VABI_VOBBLE, 0);

        Map<Restaurant, Integer> restaurantVotesCount = service.getVotesCount(TEST_DATE, RestaurantTestData.MCDONNELS, RestaurantTestData.VABI_VOBBLE);
        assertMatch(restaurantVotesCount, expected);
    }

    @Test
    void getVotesCountWithZeroes() throws Exception {

        Map<Restaurant, Integer> expected = new HashMap<>(2);
        expected.put(RestaurantTestData.MCDONNELS, 0);
        expected.put(RestaurantTestData.VABI_VOBBLE, 0);

        Map<Restaurant, Integer> restaurantVotesCount = service.getVotesCount(TEST_DATE.minusDays(1), RestaurantTestData.MCDONNELS, RestaurantTestData.VABI_VOBBLE);
        assertMatch(restaurantVotesCount, expected);
    }


    private LocalDateTime getTestDateTimeAllowed() {
        int hour = (int) (Math.random() * service.getStopVotingTime().getHour());
        int min = (int) (Math.random() * 60);

        LocalDateTime testDateTimeAllowed = TEST_DATE.atTime(hour, min);
        log.debug(testDateTimeAllowed.toString());
        return testDateTimeAllowed;
    }

    private LocalDateTime getTestDateTimeTooLate() {
        int hour = (int) Math.abs((Math.random() * 24 - service.getStopVotingTime().getHour())) + service.getStopVotingTime().getHour();
        int min = (int) (Math.random() * 60);

        LocalDateTime testDateTimeTooLate = TEST_DATE.atTime(hour, min);
        log.debug(testDateTimeTooLate.toString());
        return testDateTimeTooLate;
    }
}