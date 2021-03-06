package com.github.ivansenchukov.topjavagraduation.service.inmemory;

import com.github.ivansenchukov.topjavagraduation.configuration.InMemoryAppConfig;
import com.github.ivansenchukov.topjavagraduation.repository.RestaurantRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryRestaurantRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.service.AbstractRestaurantServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(value = InMemoryAppConfig.class)
public class InMemoryRestaurantServiceTest extends AbstractRestaurantServiceTest {

    @Autowired
    public RestaurantRepository restaurantRepository;

    @BeforeEach
    public void resetRepository() {
        ((InMemoryRestaurantRepositoryImpl) restaurantRepository).refreshRepository();
    }

}
