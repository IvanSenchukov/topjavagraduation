package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryUserRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractUserServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import org.junit.jupiter.api.BeforeEach;

public class InmemoryUserServiceTestImpl extends AbstractUserServiceTest {

    @BeforeEach
//    TODO - try Autowired
    public void resetRepository() {
        service = new UserService(new InMemoryUserRepositoryImpl());
    }
}
