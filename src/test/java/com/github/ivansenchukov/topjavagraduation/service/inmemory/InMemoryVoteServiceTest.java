package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryVoteRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractVoteServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.VoteService;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryVoteServiceTest extends AbstractVoteServiceTest {

    @BeforeEach
    public void resetRepository() {
        service = new VoteService(new InMemoryVoteRepositoryImpl());
    }

}
