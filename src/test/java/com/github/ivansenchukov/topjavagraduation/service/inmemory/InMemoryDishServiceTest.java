package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryDishRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractDishServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class InMemoryDishServiceTest extends AbstractDishServiceTest {

    @Autowired
    public DishRepository dishRepository;

    @BeforeEach
    public void resetRepository() {
        ((InMemoryDishRepositoryImpl) dishRepository).refreshRepository();
    }

}
