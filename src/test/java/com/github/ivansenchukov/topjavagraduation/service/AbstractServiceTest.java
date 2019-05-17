package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.configuration.RootApplicationConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.getRootCause;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TODO - make normal context loading here and loading of another context in InMemoryService tests if it is possible
@SpringJUnitConfig(classes = {
        RootApplicationConfig.class
})
@Transactional
abstract class AbstractServiceTest {

    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }

    //  Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }
}