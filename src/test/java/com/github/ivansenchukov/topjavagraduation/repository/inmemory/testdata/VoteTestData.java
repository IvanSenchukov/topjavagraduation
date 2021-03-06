package com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.github.ivansenchukov.topjavagraduation.model.AbstractBaseEntity.START_SEQ;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.readListFromJsonMvcResult;
import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {

    private static final Logger log = LoggerFactory.getLogger(VoteTestData.class.getName());

    public static final int FIRST_USER_VOTE_ID = 100009;
    public static final int YESTERDAY_FIRST_USER_VOTE_ID = 100010;
    public static final int SECOND_USER_VOTE_ID = 100011;
    public static final int BAD_USER_VOTE_ID = 100012;
    public static final int BAD_RESTAURANT_VOTE_ID = 100013;
    public static final int BAD_DATE_VOTE_ID = 100014;

    public static final Vote FIRST_USER_VOTE = new Vote(FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 10));
    public static final Vote YESTERDAY_FIRST_USER_VOTE = new Vote(YESTERDAY_FIRST_USER_VOTE_ID, UserTestData.USER_FIRST, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 9));
    public static final Vote SECOND_USER_VOTE = new Vote(SECOND_USER_VOTE_ID, UserTestData.USER_SECOND, RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 05, 10));
    public static final Vote BAD_USER_VOTE = new Vote(BAD_USER_VOTE_ID, null, RestaurantTestData.MCDONNELS, LocalDate.of(2019, 05, 10));
    public static final Vote BAD_RESTAURANT_VOTE = new Vote(BAD_RESTAURANT_VOTE_ID, UserTestData.USER_FIRST, null, LocalDate.of(2019, 05, 10));
    public static final Vote BAD_DATE_VOTE = new Vote(BAD_DATE_VOTE_ID, UserTestData.USER_FIRST, RestaurantTestData.MCDONNELS, null);


    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualTo(expected);
        log.info(String.format("Votes are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Map<Restaurant, Integer> actual, Map<Restaurant, Integer> expected) {
        assertThat(actual).containsAllEntriesOf(expected).hasSameSizeAs(expected);
    }

    public static ResultMatcher contentJson(Vote... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Vote.class), List.of(expected));
    }
}
