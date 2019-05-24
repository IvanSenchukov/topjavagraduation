package com.github.ivansenchukov.topjavagraduation.web.vote;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.TestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.MCDONNELS;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData.MCDONNELS_ID;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.*;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// todo remake all tests in scope of voting dates
@SpringJUnitConfig(DbConfig.class)
class CommonVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = CommonVoteController.REST_URL + '/';

    @Autowired
    protected LocalTime stopVotingTime;

    @Autowired
    protected VoteService voteService;

    //<editor-fold desc="GET">
    @Test
    void getByRestaurantIdAndDate() throws Exception {

        mockMvc.perform(get(REST_URL + "by_restaurant")
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", TEST_DATE.toString())
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VoteTestData.contentJson(FIRST_USER_VOTE));
    }

    @Test
    void getByRestaurantIdAndDateToday() throws Exception {

        LocalDate testDate = LocalDate.now();
        Vote expected = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, testDate);
        Vote returned = voteService.makeVote(expected, testDate.atStartOfDay());

        mockMvc.perform(get(REST_URL + "by_restaurant")
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VoteTestData.contentJson(returned));
    }

    @Test
    void getByUser() throws Exception {

        mockMvc.perform(get(REST_URL + "by_user")
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VoteTestData.contentJson(YESTERDAY_FIRST_USER_VOTE, FIRST_USER_VOTE));
    }

    @Test
    void getEmptyListByRestaurantAndDate() throws Exception {

        LocalDate requestDate = TEST_DATE;

        mockMvc.perform(get(REST_URL + "by_restaurant")
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", TEST_DATE.minusDays(1).toString())
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VoteTestData.contentJson());
    }

    //</editor-fold>


    //<editor-fold desc="MAKE VOTE">
    @Test
    void makeVote() throws Exception {

        LocalDate allowedDate = getDateAllowed();
        Vote expected = new Vote(USER_SECOND, RestaurantTestData.MCDONNELS, allowedDate);

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
    @Disabled
        // todo - implement this. Got troubles with creating test vote. It is needed, because on this level we don't have control on stopVotingTime
    void makeVoteIfPresent() throws Exception {

//        LocalDate allowedDate = getDateAllowed();
//
//        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.VABI_VOBBLE, allowedDate.atTime(stopVotingTime));
//        new TestUtil().createTestVote(newVote, USER_FIRST, voteService); // todo - refactor this. Make a bean or something like that!

        LocalDate testDate = getDateAllowed();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.MCDONNELS, testDate);
        Vote created = voteService.makeVote(new Vote(newVote), testDate.atStartOfDay());

        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);

        ResultActions action = mockMvc.perform(post(REST_URL)
                .param("restaurantId", String.valueOf(MCDONNELS_ID))
                .param("requestDate", testDate.toString())
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote returned = TestUtil.readFromJson(action, Vote.class);

        assertMatch(returned, created);
        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), returned);  // todo - error in this linr
    }

    @Test
    void makeVoteByAdmin() throws Exception {

        LocalDate dateAllowed = getDateAllowed();

        assertThrows(NestedServletException.class/* todo - think, how to change to this -> RestrictedOperationException.class*/, () -> {
            ResultActions action = mockMvc.perform(post(REST_URL)
                    .param("restaurantId", String.valueOf(MCDONNELS_ID))
                    .param("requestDate", dateAllowed.toString())
                    .with(TestUtil.userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isCreated());

        });
    }

    @Test
    void makeVoteWithAbsentRestaurant() throws Exception {

        LocalDate dateAllowed = getDateAllowed();

        assertThrows(NestedServletException.class/* todo - think, how to change to this -> RestrictedOperationException.class*/, () -> {
            ResultActions action = mockMvc.perform(post(REST_URL)
                    .param("restaurantId", String.valueOf(1))
                    .param("requestDate", dateAllowed.toString())
                    .with(TestUtil.userHttpBasic(USER_FIRST)))
                    .andDo(print())
                    .andExpect(status().isCreated());

        });
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">
    @Test
    void testDelete() throws Exception {

        LocalDate testDate = getDateAllowed();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.MCDONNELS, testDate);
        Vote created = voteService.makeVote(new Vote(newVote), testDate.atStartOfDay());

        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);


        mockMvc.perform(delete(REST_URL + created.getId())
                .with(TestUtil.userHttpBasic(USER_FIRST)))
                .andDo(print())
                .andExpect(status().isNoContent());


        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), Collections.emptyList());
    }

    @Test
    void testDeleteByAdmin() throws Exception {

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

    @Test
    void testDeleteByAdminWhenTimeIsUp() throws Exception {

        LocalDate testDate = getDateTooLate();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.MCDONNELS, testDate);
        Vote created = voteService.makeVote(new Vote(newVote), testDate.atStartOfDay());

        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);


        mockMvc.perform(delete(REST_URL + created.getId())
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());


        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), Collections.emptyList());
    }

    @Test
    void testDeleteWhenTimeIsUp() throws Exception {

        LocalDate testDate = getDateTooLate();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.MCDONNELS, testDate);
        Vote created = voteService.makeVote(new Vote(newVote), testDate.atStartOfDay());

        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);

        assertThrows(NestedServletException.class/* todo - think, how to change to this -> RestrictedOperationException.class*/, () -> {
            mockMvc.perform(delete(REST_URL + created.getId())
                    .with(TestUtil.userHttpBasic(USER_FIRST)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        });


        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);
    }

    @Test
    void testDeleteByWrongUser() throws Exception {

        LocalDate testDate = getDateAllowed();

        Vote newVote = new Vote(USER_FIRST, RestaurantTestData.MCDONNELS, testDate);
        Vote created = voteService.makeVote(new Vote(newVote), testDate.atStartOfDay());

        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);

        assertThrows(NestedServletException.class/* todo - think, how to change to this -> RestrictedOperationException.class*/, () -> {
            mockMvc.perform(delete(REST_URL + created.getId())
                    .with(TestUtil.userHttpBasic(USER_SECOND)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        });


        assertMatch(voteService.getByRestaurantAndDate(MCDONNELS, testDate), created);
    }

    @Test
    void testDeleteNotFound() throws Exception {

        assertThrows(NestedServletException.class/* todo - think, how to change to this -> RestrictedOperationException.class*/, () -> {
            mockMvc.perform(delete(REST_URL + 1)
                    .with(TestUtil.userHttpBasic(USER_SECOND)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        });

    }
    //</editor-fold>
}