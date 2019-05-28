package com.github.ivansenchukov.topjavagraduation.web.restaurant;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.service.DishService;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.to.RestaurantOfferTo;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.USER_FIRST;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.TEST_DATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(DbConfig.class)
class CommonRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DishService dishService;

    @Autowired
    private VoteService voteService;


    private static final String REST_URL = CommonRestaurantController.REST_URL + '/';

    //<editor-fold desc="GET">
    @Test
    void testGetOffer() throws Exception {

        LocalDate requestDate = TEST_DATE;

        Restaurant restaurant = MCDONNELS;
        List<Dish> dishes = dishService.getByRestaurantAndDate(restaurant, requestDate);
        List<Vote> votes = voteService.getByRestaurantAndDate(restaurant, requestDate);

        RestaurantOfferTo expected = new RestaurantOfferTo(requestDate, restaurant, dishes, votes);

        mockMvc.perform(get(REST_URL + "offer")
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", "2019-05-10")
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJson(expected));
    }

    @Test
    void testGetTodayOffer() throws Exception {

        LocalDate requestDate = LocalDate.now();

        Restaurant restaurant = MCDONNELS;
        List<Dish> dishes = dishService.getByRestaurantAndDate(restaurant, requestDate);
        List<Vote> votes = voteService.getByRestaurantAndDate(restaurant, requestDate);

        RestaurantOfferTo expected = new RestaurantOfferTo(requestDate, restaurant, dishes, votes);

        mockMvc.perform(get(REST_URL + "offer")
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJson(expected));
    }


    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.contentJson(MCDONNELS, VABI_VOBBLE));
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
    //</editor-fold>
}