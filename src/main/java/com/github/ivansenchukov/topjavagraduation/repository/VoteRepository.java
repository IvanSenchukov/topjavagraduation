package com.github.ivansenchukov.topjavagraduation.repository;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface VoteRepository {

    Vote save(Vote vote);

    //<editor-fold desc="GET">
    // NULL if not found
    Vote get(int id);

    // Empty list if not found
    // TODO - test it!
    List<Vote> getByRestaurantAndDate(Restaurant restaurant, LocalDate date);

    // Empty list if not found
    List<Vote> getByRestaurantAndDate(Integer restaurantId, LocalDate date);

    // NULL if not found
    Vote getByUserAndDate(User user, LocalDate date);

    // NULL if not found
    Vote getByUserAndDate(Integer userId, LocalDate date);
    //</editor-fold>

    List<Vote> getByUser(Integer userId);

    // false if not found
    boolean delete(int id);
}
