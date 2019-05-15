package com.github.ivansenchukov.topjavagraduation.repository.inmemory;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Repository
public class InMemoryVoteRepositoryImpl extends InMemoryBaseRepositoryImpl<Vote> implements VoteRepository {

    public InMemoryVoteRepositoryImpl() {
        refreshTestRepository();
    }


    public void refreshTestRepository() {
        entryMap.clear();
        entryMap.put(FIRST_USER_VOTE_ID, FIRST_USER_VOTE);
        entryMap.put(SECOND_USER_VOTE_ID, SECOND_USER_VOTE);
        entryMap.put(BAD_USER_VOTE_ID, BAD_USER_VOTE);
        entryMap.put(BAD_RESTAURANT_VOTE_ID, BAD_RESTAURANT_VOTE);
        entryMap.put(BAD_DATE_VOTE_ID, BAD_DATE_VOTE);
    }


    @Override
    public Vote save(Vote vote) {
        Assert.notNull(vote, "Vote entry must not be null");
        Assert.notNull(vote.getUser(), "Vote 'user' property must not be null");
        Assert.notNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null");
        Assert.notNull(vote.getDateTime(), "Vote 'date' property must not be null");

        return super.save(vote);
    }

    @Override
    public Vote get(int id) {
        Vote vote = super.get(id);

        if (Objects.isNull(vote)) return vote;

        if (
                Objects.isNull(vote)
                        || Objects.isNull(vote.getUser())
                        || Objects.isNull(vote.getRestaurant())
                        || Objects.isNull(vote.getDateTime())) return null;

        // TODO - make decision about it. There must be some sort of alerting when inconsistend Data in repository!
//        Objects.requireNonNull(vote.getUser(), "Vote 'user' property must not be null. Inconsistent Data in repository!");
//        Objects.requireNonNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null. Inconsistent Data in repository!");
//        Objects.requireNonNull(vote.getDateTime(), "Vote 'date' property must not be null. Inconsistend Data in repository!");

        return vote;
    }

    @Override
    public List<Vote> get(Restaurant restaurant, LocalDate dateTime) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                                && Objects.nonNull(vote.getDateTime()) && dateTime.equals(vote.getDateTime().toLocalDate())
                                && Objects.nonNull(vote.getRestaurant()) && Objects.equals(vote.getRestaurant(), restaurant))
                .sorted(new Comparator<Vote>() {
                    @Override
                    public int compare(Vote o1, Vote o2) {
                        int restaurantCompare = o1.getRestaurant().getName().compareTo(o2.getRestaurant().getName());
                        if (restaurantCompare != 0) return restaurantCompare;
                        int userCompare = o1.getUser().getName().compareTo(o2.getUser().getName());
                        return userCompare;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Vote get(LocalDate dateTime, User user) {

        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser()) && Objects.equals(user, vote.getUser())
                                && Objects.nonNull(vote.getDateTime()) && dateTime.equals(vote.getDateTime().toLocalDate()))
                .findFirst()
                .orElse(null);
    }

    // TODO - fix this
    @Override
    public Map<Restaurant, Integer> getVotesCount(LocalDate date, List<Restaurant> restaurants) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                                && Objects.nonNull(vote.getDateTime()) && date.equals(vote.getDateTime().toLocalDate())
                                && Objects.nonNull(vote.getRestaurant())
                                && restaurants.contains(vote.getRestaurant()))
                .collect(groupingBy(Vote::getRestaurant, summingInt((ToIntFunction<Object>) value -> 1)));
    }
}
