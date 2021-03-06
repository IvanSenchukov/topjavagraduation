package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.configuration.InMemoryAppConfig;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryUserRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractUserServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(value = InMemoryAppConfig.class)
public class InMemoryUserServiceTestImpl extends AbstractUserServiceTest {

    @Autowired
    public UserRepository userRepository;

    @BeforeEach
    public void resetRepository() {
        ((InMemoryUserRepositoryImpl) userRepository).refreshRepository();
    }
}
