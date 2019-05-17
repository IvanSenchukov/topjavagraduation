package com.github.ivansenchukov.topjavagraduation.service.jpa;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.service.AbstractUserServiceTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(DbConfig.class)
public class JpaUserServiceTestImpl extends AbstractUserServiceTest {

}
