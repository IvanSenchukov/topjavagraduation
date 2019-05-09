package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryRestaurantRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractRestaurantServiceTest;
import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryRestaurantServiceTest extends AbstractRestaurantServiceTest {

    @BeforeEach
    public void resetRepository() {
        service = new RestaurantService(new InMemoryRestaurantRepositoryImpl());
    }

}
