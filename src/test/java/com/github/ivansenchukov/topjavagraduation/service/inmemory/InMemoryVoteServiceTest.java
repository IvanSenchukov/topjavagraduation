package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.configuration.InMemoryAppConfig;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryVoteRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractVoteServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(value = InMemoryAppConfig.class)
public class InMemoryVoteServiceTest extends AbstractVoteServiceTest {

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    public void resetRepository() {
        ((InMemoryVoteRepositoryImpl) voteRepository).refreshTestRepository();
    }

}
