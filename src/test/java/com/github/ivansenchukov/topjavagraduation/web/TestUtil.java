package com.github.ivansenchukov.topjavagraduation.web;

import com.github.ivansenchukov.topjavagraduation.AuthorizedUser;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TestUtil {

    private static final Logger log = LoggerFactory.getLogger(TestUtil.class.getName());
    public static final LocalDate TEST_DATE = LocalDate.of(2019, 05, 10);


    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static void mockAuthorize(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new AuthorizedUser(user), null, user.getRoles()));
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }

    public static LocalDateTime getTestDateTimeAllowed(LocalTime stopVotingTime) {
        int hour = (int) (Math.random() * stopVotingTime.getHour());
        int min = (int) (Math.random() * 60);

        LocalDateTime testDateTimeAllowed = TEST_DATE.atTime(hour, min);
        log.debug(testDateTimeAllowed.toString());
        return testDateTimeAllowed;
    }

    public static LocalDateTime getTestDateTimeTooLate(LocalTime stopVotingTime) {
        int hour = (int) Math.abs((Math.random() * 24 - stopVotingTime.getHour())) + stopVotingTime.getHour();
        int min = (int) (Math.random() * 60);

        LocalDateTime testDateTimeTooLate = TEST_DATE.atTime(hour, min);
        log.debug(testDateTimeTooLate.toString());
        return testDateTimeTooLate;
    }

    public static LocalDate getDateAllowed() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return tomorrow;
    }

    public static LocalDate getDateTooLate() {
        return TEST_DATE;
    }

}
