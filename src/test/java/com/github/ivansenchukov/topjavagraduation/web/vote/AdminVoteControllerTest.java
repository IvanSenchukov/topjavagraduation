package com.github.ivansenchukov.topjavagraduation.web.vote;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import com.github.ivansenchukov.topjavagraduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.contentJson;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.assertMatch;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.TEST_DATE;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.getTestDateTimeAllowed;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(DbConfig.class)
class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminVoteController.REST_URL + '/';


    @Autowired
    protected LocalTime stopVotingTime;

    @Autowired
    protected VoteService voteService;


    //<editor-fold desc="MAKE VOTE">

    @Test
    void makeVote() throws Exception {

        Vote expected = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed(stopVotingTime).plusDays(1));

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Vote returned = TestUtil.readFromJson(action, Vote.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(voteService.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, TEST_DATE.plusDays(1)), expected);
    }

//
//    @Test
//    void makeVoteIfPresent() throws Exception {
//        Vote newVote = new Vote(UserTestData.USER_FIRST, RestaurantTestData.VABI_VOBBLE, getTestDateTimeAllowed());
//        Vote updated = service.makeVote(new Vote(newVote), USER_FIRST);
//        newVote.setId(updated.getId());
//        assertMatch(newVote, updated);
//        assertMatch(service.getByRestaurantAndDate(RestaurantTestData.VABI_VOBBLE, TEST_DATE), updated, SECOND_USER_VOTE);
//
//    }
//
//    @Test
//    void makeVoteByAdmin() throws Exception {
//        Vote newVote = new Vote(UserTestData.ADMIN, RestaurantTestData.MCDONNELS, getTestDateTimeAllowed());
//        assertThrows(RestrictedOperationException.class, () ->
//                service.makeVote(new Vote(newVote), ADMIN));
//    }
//
//    @Test
//    void makeVoteWithEmptyRestaurant() throws Exception {
//        Vote newVote = new Vote(USER_SECOND, null, getTestDateTimeAllowed());
//        assertThrows(IllegalArgumentException.class, () ->
//                service.makeVote(new Vote(newVote), USER_SECOND));
//    }
//
//    @Test
//    void makeVoteWithEmptyDate() throws Exception {
//        Vote newVote = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, null);
//        assertThrows(IllegalArgumentException.class, () ->
//                service.makeVote(new Vote(newVote), USER_SECOND));
//    }
//
//    @Test
//    void testCreate() throws Exception {
//        Restaurant expected = new Restaurant("Zoo Beer");
//        ResultActions action = mockMvc.perform(post(REST_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(TestUtil.userHttpBasic(ADMIN))
//                .content(JsonUtil.writeValue(expected)))
//                .andExpect(status().isCreated());
//
//        Restaurant returned = TestUtil.readFromJson(action, Restaurant.class);
//        expected.setId(returned.getId());
//
//        assertMatch(returned, expected);
//        assertMatch(restaurantService.getAll(), MCDONNELS, VABI_VOBBLE, expected);
//    }
    //</editor-fold>


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

    @Test
    void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isForbidden());
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
//    @Test
//    void testUpdate() throws Exception {
//        Restaurant updated = new Restaurant(MCDONNELS);
//        updated.setName("UpdatedName");
//        mockMvc.perform(put(REST_URL + MCDONNELS_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(TestUtil.userHttpBasic(ADMIN))
//                .content(JsonUtil.writeValue(updated)))
//                .andExpect(status().isNoContent());
//
//        assertMatch(restaurantService.get(MCDONNELS_ID), updated);
//    }
//    //</editor-fold>
//
//
//    //<editor-fold desc="DELETE">
//    @Test
//    void testDelete() throws Exception {
//        mockMvc.perform(delete(REST_URL + MCDONNELS_ID)
//                .with(TestUtil.userHttpBasic(ADMIN)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//        assertMatch(restaurantService.getAll(), VABI_VOBBLE);
//    }
    //</editor-fold>
}