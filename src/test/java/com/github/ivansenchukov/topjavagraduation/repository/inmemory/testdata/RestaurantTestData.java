package com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {

    private static final Logger log = LoggerFactory.getLogger(RestaurantTestData.class.getName());

    public static final int MCDONNELS_ID = START_SEQ + 0;
    public static final int VABI_VOBBLE_ID = START_SEQ + 1;

    public static final Restaurant MCDONNELS = new Restaurant(MCDONNELS_ID, "McDonnel's");
    public static final Restaurant VABI_VOBBLE = new Restaurant(VABI_VOBBLE_ID, "Vabi-Vobble");


    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualTo(expected);
        log.info(String.format("Restaurants are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).isEqualTo(expected);
    }


}
