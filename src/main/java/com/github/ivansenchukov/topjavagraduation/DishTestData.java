package com.github.ivansenchukov.topjavagraduation;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class DishTestData {

    private static final Logger log = LoggerFactory.getLogger(DishTestData.class.getName());

    public static final int MCDONNELS_BURGER_ID = START_SEQ + 0;
    public static final int MCDONNELS_FRIES_ID = START_SEQ + 1;
    public static final int VABI_VOBBLE_SUSHI_ID = START_SEQ + 2;
    public static final int VABI_VOBBLE_SASHIMI_ID = START_SEQ + 3;
    public static final int VABI_VOBBLE_BAD_DATE_ID = START_SEQ + 4;
    public static final int BAD_RESTAURANT_DISH_ID = START_SEQ + 5;

    public static final Dish MCDONNELS_BURGER = new Dish(MCDONNELS_BURGER_ID, "McDonnel's Burger", new BigDecimal(200), RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10));
    public static final Dish MCDONNELS_FRIES = new Dish(MCDONNELS_FRIES_ID, "McDonnel's Fries", new BigDecimal(150), RestaurantTestData.MCDONNELS, LocalDate.of(2019, 5, 10));
    public static final Dish VABI_VOBBLE_SUSHI = new Dish(VABI_VOBBLE_SUSHI_ID, "Vabi-Vobble Sushi", new BigDecimal(300), RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 5, 10));
    public static final Dish VABI_VOBBLE_SASHIMI = new Dish(VABI_VOBBLE_SASHIMI_ID, "Vabi-Vobble Sashimi", new BigDecimal(200), RestaurantTestData.VABI_VOBBLE, LocalDate.of(2019, 5, 10));
    public static final Dish VABI_VOBBLE_BAD_DATE = new Dish(VABI_VOBBLE_BAD_DATE_ID, "Vabi-Vobble Bad Date Dish", new BigDecimal(200), RestaurantTestData.VABI_VOBBLE, null);
    public static final Dish BAD_RESTAURANT_DISH = new Dish(BAD_RESTAURANT_DISH_ID, "Bad Restaurant Dish", new BigDecimal(200), null, LocalDate.of(2019, 5, 10));


    public static void assertMatch(Dish actual, Dish expected) {
        assertThat(actual).isEqualTo(expected); // TODO - upgrade this assertion after implementing model layer
        log.info(String.format("Dishes are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<Dish> actual, Dish... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).isEqualTo(expected); // TODO - upgrade this assertion after implementing model layer
    }


}
