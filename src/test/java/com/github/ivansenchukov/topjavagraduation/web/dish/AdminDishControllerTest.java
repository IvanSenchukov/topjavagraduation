package com.github.ivansenchukov.topjavagraduation.web.dish;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.DishTestData;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import com.github.ivansenchukov.topjavagraduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.DishTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.DishTestData.assertMatch;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.DishTestData.contentJson;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.ADMIN;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.USER_FIRST;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.TEST_DATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(DbConfig.class)
class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminDishController.REST_URL + '/';


    //<editor-fold desc="CREATE">
    @Test
    void testCreate() throws Exception {
        Dish expected = new Dish("Latte", new BigDecimal(100), VABI_VOBBLE, TEST_DATE);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish returned = TestUtil.readFromJson(action, Dish.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(dishService.getByRestaurantAndDate(VABI_VOBBLE, TEST_DATE), expected, VABI_VOBBLE_SASHIMI, VABI_VOBBLE_SUSHI);
    }
    //</editor-fold>


    //<editor-fold desc="GET">
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MCDONNELS_BURGER_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MCDONNELS_BURGER));
    }

    @Test
    void testGetByRestaurantAndDate() throws Exception {
        mockMvc.perform(get(REST_URL + "by_restaurant")
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", TEST_DATE.toString())
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(DishTestData.contentJsonArray(MCDONNELS_BURGER, MCDONNELS_FRIES));
    }

    @Test
    void testGetByRestaurantToday() throws Exception {

        Dish expected = new Dish("Latte", new BigDecimal(100), VABI_VOBBLE, LocalDate.now());
        dishService.create(expected);

        mockMvc.perform(get(REST_URL + "by_restaurant")
                .param("restaurantId", String.valueOf(VABI_VOBBLE_ID))
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentJsonArray(expected));
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    @Test
    void testUpdate() throws Exception {
        Dish updated = new Dish(MCDONNELS_BURGER);
        updated.setName("UpdatedName");
        mockMvc.perform(put(REST_URL + MCDONNELS_BURGER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(dishService.get(MCDONNELS_BURGER_ID), updated);
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MCDONNELS_BURGER_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(dishService.getByRestaurantAndDate(MCDONNELS, TEST_DATE), MCDONNELS_FRIES);
    }
    //</editor-fold>
}