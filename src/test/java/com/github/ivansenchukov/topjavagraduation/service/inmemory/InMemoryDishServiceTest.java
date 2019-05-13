package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryDishRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryRestaurantRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractDishServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.AbstractRestaurantServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.DishService;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
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
