package com.github.ivansenchukov.topjavagraduation.web.restaurant;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import com.github.ivansenchukov.topjavagraduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.ADMIN;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.USER_FIRST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(DbConfig.class)
class CommonRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = CommonRestaurantController.REST_URL + '/';

    //<editor-fold desc="GET">
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MCDONNELS_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MCDONNELS));
    }
    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(ADMIN)))
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