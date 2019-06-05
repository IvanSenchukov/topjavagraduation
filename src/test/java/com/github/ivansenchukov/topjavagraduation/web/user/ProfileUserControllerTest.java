package com.github.ivansenchukov.topjavagraduation.web.user;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.to.UserTo;
import com.github.ivansenchukov.topjavagraduation.util.UserUtil;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;
import com.github.ivansenchukov.topjavagraduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.Date;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.readFromJson;
import static com.github.ivansenchukov.topjavagraduation.web.user.ProfileUserController.REST_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.userHttpBasic;

@SpringJUnitConfig(DbConfig.class)
class ProfileUserControllerTest extends AbstractControllerTest {

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER_FIRST)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(USER_FIRST));
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(userHttpBasic(USER_FIRST)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN, USER_SECOND);
    }

    @Test
    void testUpdateEmail() throws Exception {
        User expected = new User(USER_FIRST);
        expected.setEmail("newEmail@example.com");

        ProfileUserController.UpdateEmailRequestTO updateEmailRequestTO = new ProfileUserController.UpdateEmailRequestTO();
        updateEmailRequestTO.eMail = expected.getEmail();
        updateEmailRequestTO.password = expected.getPassword();

        ResultActions action = mockMvc.perform(patch(REST_URL + "email")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER_FIRST))
                .content(JsonUtil.writeValue(updateEmailRequestTO)))
                .andDo(print())
                .andExpect(status().isOk());

        assertMatch(userService.get(USER_FIRST_ID), expected);
        assertMatch(userService.getAll(), ADMIN, expected, USER_SECOND);
    }
    @Test
    void testUpdateName() throws Exception {
        User expected = new User(USER_FIRST);
        expected.setName("newName");

        ProfileUserController.UpdateNameRequestTO updateNameRequestTO = new ProfileUserController.UpdateNameRequestTO();
        updateNameRequestTO.name = expected.getName();
        updateNameRequestTO.password = expected.getPassword();

        ResultActions action = mockMvc.perform(patch(REST_URL + "name")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER_FIRST))
                .content(JsonUtil.writeValue(updateNameRequestTO)))
                .andDo(print())
                .andExpect(status().isOk());

        assertMatch(userService.get(USER_FIRST_ID), expected);
        assertMatch(userService.getAll(), ADMIN, USER_SECOND, expected);
    }
    @Test
    void testUpdatePassword() throws Exception {
        User expected = new User(USER_FIRST);
        expected.setPassword("newPassword");

        ProfileUserController.UpdatePasswordRequestTO updatePasswordRequestTO = new ProfileUserController.UpdatePasswordRequestTO();
        updatePasswordRequestTO.newPassword = expected.getEmail();
        updatePasswordRequestTO.oldPassword = USER_FIRST.getPassword();

        ResultActions action = mockMvc.perform(patch(REST_URL + "password")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER_FIRST))
                .content(JsonUtil.writeValue(updatePasswordRequestTO)))
                .andDo(print())
                .andExpect(status().isOk());

        assertMatch(userService.get(USER_FIRST_ID), expected);
        assertMatch(userService.getAll(), ADMIN, expected, USER_SECOND);
    }
}