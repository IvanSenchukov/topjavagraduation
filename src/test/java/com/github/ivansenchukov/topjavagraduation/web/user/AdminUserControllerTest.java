package com.github.ivansenchukov.topjavagraduation.web.user;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.model.Role;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.ResultActions;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;

import java.util.Collections;
import java.util.Date;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.readFromJson;
import static com.github.ivansenchukov.topjavagraduation.web.TestUtil.userHttpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(DbConfig.class)
// todo - make tests for common users, that try to send request on admin servlet
class AdminUserControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminUserController.REST_URL + '/';

    //<editor-fold desc="CREATE">
    @Test
    void testCreate() throws Exception {
        User expected = new User(new User(null, "New", "new@example.com", "newPass", true, new Date(), Collections.singleton(Role.ROLE_USER)));

        AdminUserController.CreateNewUserRequestTO createRequestTO = new AdminUserController.CreateNewUserRequestTO();
        createRequestTO.name = expected.getName();
        createRequestTO.eMail = expected.getEmail();
        createRequestTO.password = expected.getPassword();

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(createRequestTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        User returned = readFromJson(action, User.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(userService.getAll(), ADMIN, USER_FIRST, expected, USER_SECOND);
    }
    //</editor-fold>

    //<editor-fold desc="GET">
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN));
    }

    @Test
    void testGetByEmail() throws Exception {
        mockMvc.perform(get(REST_URL + "by?email=" + ADMIN.getEmail())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN, USER_FIRST, USER_SECOND));
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
                .with(userHttpBasic(USER_FIRST)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    //</editor-fold>

    //<editor-fold desc="UPDATE">
    @Test
    void testEnabled() throws Exception {
        User expected = new User(USER_FIRST);
        expected.setEnabled(false);

        mockMvc.perform(patch(REST_URL + "enable/" + USER_FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("enabled", "false")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());

        assertMatch(userService.get(USER_FIRST_ID), expected);
    }

    @Test
    void testUpdateRoles() throws Exception {
        User expected = new User(USER_FIRST);
        expected.setRoles(Collections.singletonList(Role.ROLE_ADMIN));

        mockMvc.perform(patch(REST_URL + "roles/" + USER_FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("enabled", "false")
                .content(JsonUtil.writeValue(Collections.singletonList(Role.ROLE_ADMIN)))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());

        assertMatch(userService.get(USER_FIRST_ID), expected);
    }
    //</editor-fold>

    //<editor-fold desc="DELETE">
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_FIRST_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN, USER_SECOND);
    }
    //</editor-fold>
}