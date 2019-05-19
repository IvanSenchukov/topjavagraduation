package web.user;

import com.github.ivansenchukov.topjavagraduation.configuration.InMemoryAppConfig;
import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryUserRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData;
import com.github.ivansenchukov.topjavagraduation.web.user.AdminRestController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import web.AbstractControllerTest;

import java.util.Arrays;
import java.util.Collection;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {
        InMemoryAppConfig.class
})
class InMemoryAdminRestControllerTest extends AbstractControllerTest {

    @Autowired
    private AdminRestController controller;

    @Autowired
    public UserRepository userRepository;


    @BeforeEach
    void setUp() throws Exception {
        // re-initialize
        ((InMemoryUserRepositoryImpl) userRepository).refreshRepository();
    }

    @Test
    void delete() throws Exception {
        controller.delete(UserTestData.USER_FIRST_ID);
        Collection<User> users = controller.getAll();
        assertEquals(users.size(), 1);
        assertEquals(users.iterator().next(), ADMIN);
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                controller.delete(10));
    }
}