package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryVoteRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractVoteServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class InMemoryVoteServiceTest extends AbstractVoteServiceTest {

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    public void resetRepository() {
        ((InMemoryVoteRepositoryImpl) voteRepository).refreshTestRepository();
    }

}
