package com.github.ivansenchukov.topjavagraduation.service.jpa;

import com.github.ivansenchukov.topjavagraduation.configuration.JpaDbConfig;
import com.github.ivansenchukov.topjavagraduation.service.AbstractRestaurantServiceTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(JpaDbConfig.class)
public class JpaRestaurantServiceTest extends AbstractRestaurantServiceTest {

}
