package com.github.ivansenchukov.topjavagraduation;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {

    private static final Logger log = LoggerFactory.getLogger(RestaurantTestData.class.getName());

    public static final int DUKE_SUSHI_ID = START_SEQ;
    public static final int MCDONNELS_ID = START_SEQ + 1;
    public static final int PILZNER_ID = START_SEQ + 2;
    public static final int VABI_VOBBLE_ID = START_SEQ + 3;
    public static final int VOGUER_ID = START_SEQ + 4;

    public static final Restaurant DUKE_SUSHI = new Restaurant(DUKE_SUSHI_ID, "Duke Sushi");
    public static final Restaurant MCDONNELS = new Restaurant(MCDONNELS_ID, "McDonnel's");
    public static final Restaurant PILZNER = new Restaurant(PILZNER_ID, "Pilzner");
    public static final Restaurant VABI_VOBBLE = new Restaurant(VABI_VOBBLE_ID, "Vabi-Vobble");
    public static final Restaurant VOGUER = new Restaurant(VOGUER_ID, "Voguer");


    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualTo(expected); // TODO - upgrade this assertion after implementing model layer
        log.info(String.format("Users are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).isEqualTo(expected); // TODO - upgrade this assertion after implementing model layer
    }


}
