package com.github.ivansenchukov.topjavagraduation;

import com.github.ivansenchukov.topjavagraduation.model.Role;
import com.github.ivansenchukov.topjavagraduation.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {

    private static final Logger log = LoggerFactory.getLogger(UserTestData.class);

    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_FIRST_ID = START_SEQ + 1;
    public static final int USER_SECOND_ID = START_SEQ + 2;

    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", true, new Date(), Role.ROLE_ADMIN);
    public static final User USER_FIRST = new User(USER_FIRST_ID, "First User", "firstuser@yandex.ru", "password", true, new Date(), Role.ROLE_USER);
    public static final User USER_SECOND = new User(USER_SECOND_ID, "Second User", "seconduser@yandex.ru", "password", true, new Date(), Role.ROLE_USER);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered");
        log.info(String.format("Users are match. %s", actual.toString()));
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered").isEqualTo(expected);
    }
}
