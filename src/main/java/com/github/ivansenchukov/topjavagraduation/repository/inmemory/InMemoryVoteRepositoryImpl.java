package com.github.ivansenchukov.topjavagraduation.repository.inmemory;

import com.github.ivansenchukov.topjavagraduation.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.UserTestData;
import com.github.ivansenchukov.topjavagraduation.VoteTestData;
import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static com.github.ivansenchukov.topjavagraduation.DishTestData.*;
import static com.github.ivansenchukov.topjavagraduation.VoteTestData.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.summingLong;

public class InMemoryVoteRepositoryImpl extends InMemoryBaseRepositoryImpl<Vote> implements VoteRepository {

    public InMemoryVoteRepositoryImpl() {
        refreshTestRepository();
    }


    public void refreshTestRepository() {
        entryMap.clear();
        entryMap.put(ADMIN_VOTE_ID, ADMIN_VOTE);
        entryMap.put(FIRST_USER_VOTE_ID, FIRST_USER_VOTE);
        entryMap.put(BAD_USER_VOTE_ID, BAD_USER_VOTE);
        entryMap.put(BAD_RESTAURANT_VOTE_ID, BAD_RESTAURANT_VOTE);
        entryMap.put(BAD_DATE_VOTE_ID, BAD_DATE_VOTE);
    }


    @Override
    public Vote save(Vote vote) {
        Assert.notNull(vote, "Vote entry must not be null");
        Assert.notNull(vote.getUser(), "Vote 'user' property must not be null");
        Assert.notNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null");
        Assert.notNull(vote.getDate(), "Vote 'date' property must not be null");

        return super.save(vote);
    }

    @Override
    public Vote get(int id) {
        Vote vote = super.get(id);

        if (Objects.isNull(vote)) return vote;

        Objects.requireNonNull(vote.getUser(), "Vote 'user' property must not be null. Inconsistent Data in repository!");
        Objects.requireNonNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null. Inconsistent Data in repository!");
        Objects.requireNonNull(vote.getDate(), "Vote 'date' property must not be null. Inconsistend Data in repository!");

        return vote;
    }

    @Override
    public List<Vote> get(Restaurant restaurant, LocalDate dateTime) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                                && Objects.nonNull(vote.getDate()) && dateTime.equals(vote.getDate().toLocalDate())
                                && Objects.nonNull(vote.getRestaurant()) && Objects.equals(vote.getRestaurant(), restaurant))
                .sorted(Comparator.comparing(Vote::getRestaurant))
                .collect(Collectors.toList());
    }

    @Override
    public Vote get(LocalDate dateTime, User user) {

        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser()) && Objects.equals(user, vote.getUser())
                                && Objects.nonNull(vote.getDate()) && dateTime.equals(vote.getDate().toLocalDate()))
                .findFirst()
                .orElse(null);
    }

    // TODO - fix this
    @Override
    public Map<Restaurant, Integer> getVotesCount(LocalDate date, List<Restaurant> restaurants) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                        && Objects.nonNull(vote.getDate()) && date.equals(vote.getDate().toLocalDate())
                        && Objects.nonNull(vote.getRestaurant())
                                && restaurants.contains(vote.getRestaurant()))
                .collect(groupingBy(Vote::getRestaurant, summingInt((ToIntFunction<Object>) value -> 1)));
    }
}
