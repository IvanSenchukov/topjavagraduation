package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryUserRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractUserServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class InMemoryUserServiceTestImpl extends AbstractUserServiceTest {

    @Autowired
    public UserRepository userRepository;

    @BeforeEach
    public void resetRepository() {
        ((InMemoryUserRepositoryImpl) userRepository).refreshRepository();
    }
}
