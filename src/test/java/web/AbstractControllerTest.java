package web;

import com.github.ivansenchukov.topjavagraduation.configuration.RootApplicationConfig;
import com.github.ivansenchukov.topjavagraduation.configuration.web.WebConfig;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

// TODO - make normal context loading here and loading of another context in InMemoryService tests if it is possible
@SpringJUnitWebConfig(classes = {
        RootApplicationConfig.class,
        WebConfig.class
})
@Transactional
abstract public class AbstractControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    protected MockMvc mockMvc;

//    todo - add for caching
//    @Autowired
//    private CacheManager cacheManager;

//    todo - add for caching
//    @Autowired(required = false)
//    private JpaUtil jpaUtil;

    @Autowired
    protected UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
//                .apply(springSecurity())
                .build();
    }

//    @BeforeEach
//    void setUp() {
//        cacheManager.getCache("users").clear();
//        if (jpaUtil != null) {
//            jpaUtil.clear2ndLevelHibernateCache();
//        }
//    }
}
