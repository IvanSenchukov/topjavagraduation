package com.github.ivansenchukov.topjavagraduation.repository;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VoteRepository {

    Vote save(Vote vote);

    // NULL if not found
    Vote get(int id);

    // NULL if not found
    List<Vote> get(Restaurant restaurant, LocalDate date);

    // NULL if not found
    Vote get(LocalDate date, User user);

    // false if not found
    boolean delete(int id);

    // map<Restaurant, 0> if votes is absent
    Map<Restaurant, Integer> getVotesCount(LocalDate date, List<Restaurant> restaurants);
}
