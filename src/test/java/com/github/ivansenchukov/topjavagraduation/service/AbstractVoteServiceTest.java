package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.UserTestData;
import com.github.ivansenchukov.topjavagraduation.VoteTestData;
import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ivansenchukov.topjavagraduation.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TODO - upgrade all tests after implementing model layer
public abstract class AbstractVoteServiceTest extends AbstractServiceTest {


    @Autowired
    protected VoteService service;

    //<editor-fold desc="CREATE">
    @Test
    void makeVote() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 10).atTime(8, 55));
        Vote created = service.makeVote(new Vote(newVote));
        newVote.setId(created.getId());
        assertMatch(newVote, created);
        assertMatch(service.get(RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10)), FIRST_USER_VOTE, newVote);
    }

    @Test
    void makeVoteIfPresentAndTimeIsUp() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 05, 10).atTime(12, 00));
        assertThrows(IllegalArgumentException.class, () ->
                service.makeVote(new Vote(newVote)));
    }

    @Test
    void makeVoteIfPresent() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 05, 10).atTime(10, 00));
        Vote updated = service.makeVote(new Vote(newVote));
        newVote.setId(updated.getId());
        assertMatch(newVote, updated);
        assertMatch(service.get(RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 5, 10)), updated, ADMIN_VOTE);

    }

    @Test
    void makeVoteWithEmptyRestaurant() throws Exception {
        Vote newVote = new Vote(UserTestData.USER_SECOND, null, LocalDate.of(2019, 05, 10).atTime(7,15));
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
    // TODO - try to find something like DataProvider in testng and use it here
    void get() throws Exception {
        Vote vote = service.get(FIRST_USER_VOTE_ID);
        assertMatch(vote, FIRST_USER_VOTE);
    }

    @Test
    void getByRestaurantAndDate() throws Exception {
        List<Vote> votes = service.get(RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10));
        assertMatch(votes, FIRST_USER_VOTE);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getEmptyUser() throws Exception {
        assertThrows(NullPointerException.class, () ->
                service.get(VoteTestData.BAD_USER_VOTE_ID));
    }

    @Test
    void getWithEmptyRestaurant() throws Exception {
        assertThrows(NullPointerException.class, () ->
                service.get(VoteTestData.BAD_RESTAURANT_VOTE_ID));
    }

    @Test
    void getEmptyDate() throws Exception {
        assertThrows(NullPointerException.class, () ->
                service.get(VoteTestData.BAD_DATE_VOTE_ID));
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    // TODO - implement here makeVote before and after 11 o'clock
    //</editor-fold>


    //<editor-fold desc="DELETE">
    // TODO - try to use something like Testng Dataprovider here
// TODO - implement here delete before and after 11 o'clock
//    @Test
//    void delete() throws Exception {
//        service.delete(VABI_VOBBLE_SASHIMI_ID);
//        assertMatch(service.get(RestaurantTestData.VABI_VOBBLE,LocalDate.of(2019, 5, 10)), VABI_VOBBLE_SUSHI);
//    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }
    //</editor-fold>

    @Test
    void getVotesCount() throws Exception {

        Vote newVote = new Vote(UserTestData.USER_SECOND, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 10).atTime(4, 30));
        Vote created = service.makeVote(new Vote(newVote));

        Map<Restaurant, Integer> expected = new HashMap<>(2);
        expected.put(RestaurantTestData.MCDONNELS, 2);
        expected.put(RestaurantTestData.VABI_VOBBLE, 1);

        Map<Restaurant, Integer> restaurantVotesCount = service.getVotesCount(LocalDate.of(2019, 05, 10), RestaurantTestData.MCDONNELS, RestaurantTestData.VABI_VOBBLE);
        assertMatch(restaurantVotesCount, expected);
    }
}