package com.github.ivansenchukov.topjavagraduation;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.github.ivansenchukov.topjavagraduation.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {

    private static final Logger log = LoggerFactory.getLogger(VoteTestData.class.getName());

    public static final int ADMIN_VOTE_ID = START_SEQ + 0;
    public static final int FIRST_USER_VOTE_ID = START_SEQ + 1;
    public static final int BAD_USER_VOTE_ID = START_SEQ + 2;
    public static final int BAD_RESTAURANT_VOTE_ID = START_SEQ + 3;
    public static final int BAD_DATE_VOTE_ID = START_SEQ + 4;

    public static final Vote ADMIN_VOTE = new Vote(ADMIN_VOTE_ID, UserTestData.ADMIN, RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 05, 10).atTime(8, 15));
    public static final Vote FIRST_USER_VOTE = new Vote(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 10).atTime(9, 45));
    public static final Vote BAD_USER_VOTE = new Vote(BAD_USER_VOTE_ID, null, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 10).atTime(10, 30));
    public static final Vote BAD_RESTAURANT_VOTE = new Vote(BAD_RESTAURANT_VOTE_ID, UserTestData.USER_FIRST, null, LocalDate.of(2019, 05, 10).atTime(7, 40));
    public static final Vote BAD_DATE_VOTE = new Vote(BAD_DATE_VOTE_ID, UserTestData.USER_FIRST, RestaurantTestData.MCDONNELS, null);


    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualTo(expected); // TODO - upgrade this assertion after implementing model layer
        log.info(String.format("Votes are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).isEqualTo(expected); // TODO - upgrade this assertion after implementing model layer
    }

    public static void assertMatch(Map<Restaurant, Integer> actual, Map<Restaurant, Integer> expected) {
        assertThat(actual).containsAllEntriesOf(expected).hasSameSizeAs(expected);
    }

}