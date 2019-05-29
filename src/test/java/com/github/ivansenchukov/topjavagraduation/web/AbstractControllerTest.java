package com.github.ivansenchukov.topjavagraduation.web;

import com.github.ivansenchukov.topjavagraduation.configuration.RootApplicationConfig;
import com.github.ivansenchukov.topjavagraduation.configuration.security.SecurityConfig;
import com.github.ivansenchukov.topjavagraduation.configuration.web.WebConfig;
import com.github.ivansenchukov.topjavagraduation.repository.JpaUtil;
import com.github.ivansenchukov.topjavagraduation.service.DishService;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

// TODO - make normal context loading here and loading of another context in InMemoryService tests if it is possible
@SpringJUnitWebConfig(classes = {
        RootApplicationConfig.class,
        WebConfig.class,
        SecurityConfig.class
})
@Transactional
abstract public class AbstractControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    protected MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private JpaUtil jpaUtil;

    @Autowired
    protected UserService userService;

    @Autowired
    protected RestaurantService restaurantService;

    @Autowired
    protected DishService dishService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        cacheManager.getCache("users").clear();
        cacheManager.getCache("restaurants").clear();
        cacheManager.getCache("dishes").clear();
        if (jpaUtil != null) {
            jpaUtil.clear2ndLevelHibernateCache();
        }
    }
}
