package com.github.ivansenchukov.topjavagraduation.web.user;

import com.github.ivansenchukov.topjavagraduation.configuration.InMemoryAppConfig;
import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryUserRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.github.ivansenchukov.topjavagraduation.web.AbstractControllerTest;

import java.util.Collection;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {
        InMemoryAppConfig.class
})
public class InMemoryAdminRestControllerSpringTest extends AbstractControllerTest {

    @Autowired
    private AdminRestController controller;

    @Autowired
    public UserRepository userRepository;

    @BeforeEach
    public void resetRepository() {
        // re-initialize
        ((InMemoryUserRepositoryImpl) userRepository).refreshRepository();
    }

    @Test
    void testDelete() throws Exception {

        controller.delete(UserTestData.USER_FIRST_ID);

        Collection<User> users = controller.getAll();
        assertEquals(2, users.size());
        assertEquals(users.iterator().next(), ADMIN);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                controller.delete(10));
    }
}
