package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.DuplicateException;
import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Role;
import com.github.ivansenchukov.topjavagraduation.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;


    //<editor-fold desc="CREATE">
    @Test
    void create() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = service.create(new User(newUser));
        newUser.setId(created.getId());
        assertMatch(newUser, created);
        assertMatch(service.getAll(), ADMIN, USER_FIRST, newUser, USER_SECOND);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DuplicateException.class, () ->
                service.create(new User(null, "Duplicate", "firstuser@yandex.ru", "newPass", true, new Date(), Role.ROLE_USER)));
    }
    //</editor-fold>


    //<editor-fold desc="GET">
    @Test
    void get() throws Exception {
        User user = service.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        assertMatch(user, ADMIN);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = service.getAll();
        assertMatch(all, ADMIN, USER_FIRST, USER_SECOND);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">
    @Test
    void update() throws Exception {
        User updated = new User(USER_FIRST);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        service.update(new User(updated));
        assertMatch(service.get(USER_FIRST_ID), updated);
    }

    @Test
    void enable() {
        service.enable(USER_FIRST_ID, false);
        assertFalse(service.get(USER_FIRST_ID).isEnabled());
        service.enable(USER_FIRST_ID, true);
        assertTrue(service.get(USER_FIRST_ID).isEnabled());
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">
    @Test
    void delete() throws Exception {
        service.delete(USER_FIRST_ID);
        assertMatch(service.getAll(), ADMIN, USER_SECOND);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }
    //</editor-fold>
}