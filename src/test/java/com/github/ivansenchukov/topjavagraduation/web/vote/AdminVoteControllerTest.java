package com.github.ivansenchukov.topjavagraduation.web.vote;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.MCDONNELS;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.MCDONNELS_ID;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.assertMatch;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.*;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.TEST_DATE;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.getDateAllowed;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// todo - make tests for common users, that try to send request on admin servlet
@SpringJUnitConfig(DbConfig.class)
class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminVoteController.REST_URL + '/';


    @Autowired
    protected LocalTime stopVotingTime;

    @Autowired
    protected VoteService voteService;


    //<editor-fold desc="GET">
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_USER_VOTE_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VoteTestData.contentJson(FIRST_USER_VOTE));
    }


    @Test
    void getByUser() throws Exception {

        mockMvc.perform(get(REST_URL + "by_user")
                .param("userId", String.valueOf(USER_FIRST_ID))
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VoteTestData.contentJson(YESTERDAY_FIRST_USER_VOTE, FIRST_USER_VOTE));
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


//
//    //<editor-fold desc="DELETE">

    @Test
    void testDelete() throws Exception {

        LocalDate testDate = getDateAllowed();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.MCDONNELS, testDate);
        Vote created = voteService.makeVote(new Vote(newVote), testDate.atStartOfDay());

        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);


        mockMvc.perform(delete(REST_URL + created.getId())
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());


        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), Collections.emptyList());
    }


    //</editor-fold>
}