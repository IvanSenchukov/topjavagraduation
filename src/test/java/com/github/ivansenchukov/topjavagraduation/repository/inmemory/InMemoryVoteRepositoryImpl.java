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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.VoteTestData.*;

@Repository
public class InMemoryVoteRepositoryImpl extends InMemoryBaseRepositoryImpl<Vote> implements VoteRepository {

    public InMemoryVoteRepositoryImpl() {
        refreshTestRepository();
    }


    public void refreshTestRepository() {
        entryMap.clear();
        entryMap.put(FIRST_USER_VOTE_ID, FIRST_USER_VOTE);
        entryMap.put(SECOND_USER_VOTE_ID, SECOND_USER_VOTE);
        entryMap.put(YESTERDAY_FIRST_USER_VOTE_ID, YESTERDAY_FIRST_USER_VOTE);
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

        if (
                Objects.isNull(vote)
                        || Objects.isNull(vote.getUser())
                        || Objects.isNull(vote.getRestaurant())
                        || Objects.isNull(vote.getDate())) return null;
        return vote;
    }

    @Override
    public List<Vote> getByRestaurantAndDate(Restaurant restaurant, LocalDate dateTime) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                                && Objects.nonNull(vote.getDate()) && dateTime.equals(vote.getDate())
                                && Objects.nonNull(vote.getRestaurant()) && Objects.equals(vote.getRestaurant(), restaurant))
                .sorted((o1, o2) -> {
                    int restaurantCompare = o1.getRestaurant().getName().compareTo(o2.getRestaurant().getName());
                    if (restaurantCompare != 0) return restaurantCompare;
                    return o1.getUser().getName().compareTo(o2.getUser().getName());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Vote> getByRestaurantAndDate(Integer restaurantId, LocalDate dateTime) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                                && Objects.nonNull(vote.getDate()) && dateTime.equals(vote.getDate())
                                && Objects.nonNull(vote.getRestaurant()) && Objects.equals(vote.getRestaurant().getId(), restaurantId))
                .sorted(new Comparator<Vote>() {
                    @Override
                    public int compare(Vote o1, Vote o2) {
                        int restaurantCompare = o1.getRestaurant().getName().compareTo(o2.getRestaurant().getName());
                        if (restaurantCompare != 0) return restaurantCompare;
                        return o1.getUser().getName().compareTo(o2.getUser().getName());
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Vote> getByDate(LocalDate dateTime) {
        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser())
                                && Objects.nonNull(vote.getDate()) && Objects.nonNull(vote.getRestaurant())
                                && dateTime.equals(vote.getDate())
                )
                .sorted((o1, o2) -> {
                    if (Objects.isNull(o1) || Objects.isNull(o1.getDate())) return 1;
                    if (Objects.isNull(o2) || Objects.isNull(o2.getDate())) return -1;
                    return o1.getDate().compareTo(o2.getDate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Vote getByUserAndDate(User user, LocalDate dateTime) {

        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser()) && Objects.equals(user, vote.getUser())
                                && Objects.nonNull(vote.getDate()) && dateTime.equals(vote.getDate()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Vote getByUserAndDate(Integer userId, LocalDate dateTime) {

        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser()) && Objects.equals(userId, vote.getUser().getId())
                                && Objects.nonNull(vote.getDate()) && dateTime.equals(vote.getDate()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Vote> getByUser(Integer userId) {

        return getCollection().stream()
                .filter(vote ->
                        Objects.nonNull(vote.getUser()) && Objects.nonNull(vote.getDate()) && Objects.nonNull(vote.getRestaurant())
                                && Objects.nonNull(vote.getUser().getId())
                                && Objects.equals(userId, vote.getUser().getId()))
                .sorted((o1, o2) -> {
                    if (Objects.isNull(o1) || Objects.isNull(o1.getDate())) return 1;
                    if (Objects.isNull(o2) || Objects.isNull(o2.getDate())) return -1;
                    return o1.getDate().compareTo(o2.getDate());
                })
                .collect(Collectors.toList());

    }
}
