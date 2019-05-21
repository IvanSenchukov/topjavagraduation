package com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.to.RestaurantOfferTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.readFromJsonMvcResult;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.readListFromJsonMvcResult;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {

    private static final Logger log = LoggerFactory.getLogger(RestaurantTestData.class.getName());

    public static final int MCDONNELS_ID = 100003;
    public static final int VABI_VOBBLE_ID = 100004;

    public static final Restaurant MCDONNELS = new Restaurant(MCDONNELS_ID, "McDonnel's");
    public static final Restaurant VABI_VOBBLE = new Restaurant(VABI_VOBBLE_ID, "Vabi-Vobble");


    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualTo(expected);
        log.info(String.format("Restaurants are match. %s", actual.toString()));
    }

    public static void assertMatch(RestaurantOfferTo actual, RestaurantOfferTo expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
        log.info(String.format("Restaurant offers are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).isEqualTo(expected);
    }


    public static ResultMatcher contentJson(Restaurant... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Restaurant.class), List.of(expected));
    }

    public static ResultMatcher contentJson(Restaurant expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Restaurant.class), expected);
    }

    public static ResultMatcher contentJson(RestaurantOfferTo expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, RestaurantOfferTo.class), expected);
    }
}
