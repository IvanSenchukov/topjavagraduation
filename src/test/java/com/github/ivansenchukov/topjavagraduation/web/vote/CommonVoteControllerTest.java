package com.github.ivansenchukov.topjavagraduation.web.vote;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.MCDONNELS;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.MCDONNELS_ID;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.USER_FIRST;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.USER_SECOND;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.assertMatch;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.getDateAllowed;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.getDateTooLate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(DbConfig.class)
class CommonVoteControllerTest extends AbstractControllerTest {

    @Autowired
    protected LocalTime stopVotingTime;

    @Autowired
    protected VoteService voteService;


    private static final String REST_URL = CommonVoteController.REST_URL + '/';

    //<editor-fold desc="MAKE VOTE">
    @Test
    void makeVote() throws Exception {

        LocalDate allowedDate = getDateAllowed();
        Vote expected = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, allowedDate.atStartOfDay());

        ResultActions action = mockMvc.perform(post(REST_URL)
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", allowedDate.toString())
                .with(TestUtil.userHttpBasic(USER_SECOND)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote returned = TestUtil.readFromJson(action, Vote.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(voteService.getByRestaurantAndDate(RestaurantTestData.MCDONNELS, allowedDate), expected);
    }

    @Test
    void makeVoteIfPresentAndTimeIsUp() throws Exception {

        LocalDate dateTooLate = getDateTooLate();

        assertThrows(NestedServletException.class/* todo - think, how to change to this -> RestrictedOperationException.class*/, () -> {
            ResultActions action = mockMvc.perform(post(REST_URL)
                    .param("restaurantId", String.valueOf(MCDONNELS_ID))
                    .param("requestDate", dateTooLate.toString())
                    .with(TestUtil.userHttpBasic(USER_SECOND)))
                    .andDo(print())
                    .andExpect(status().isCreated());

        });
    }

    @Test
    void makeVoteIfPresent() throws Exception {

        LocalDate allowedDate = getDateAllowed();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.VABI_VOBBLE, allowedDate.atTime(stopVotingTime));
        voteService.makeVote(newVote, USER_FIRST);

        Vote expected = new Vote(newVote);
        expected.setRestaurant(MCDONNELS);

        ResultActions action = mockMvc.perform(post(REST_URL)
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", allowedDate.toString())
                .with(TestUtil.userHttpBasic(USER_SECOND)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote returned = TestUtil.readFromJson(action, Vote.class);
        newVote.setId(returned.getId());

        assertMatch(returned, newVote);
        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, allowedDate), expected);
    }
    //</editor-fold>
}