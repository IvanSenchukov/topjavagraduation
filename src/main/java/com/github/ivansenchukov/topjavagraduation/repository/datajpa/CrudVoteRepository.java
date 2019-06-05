package com.github.ivansenchukov.topjavagraduation.repository.datajpa;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Override
    @Transactional
    Vote save(Vote vote);

    @Override
    Optional<Vote> findById(Integer id);

    @Query("SELECT v FROM Vote v WHERE v.restaurant=:restaurant AND v.date=:date ORDER BY v.restaurant, v.user")
    List<Vote> getByRestaurantAndDate(@Param("restaurant") Restaurant restaurant, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=:restaurantId AND v.date=:date ORDER BY v.restaurant, v.user")
    List<Vote> getByRestaurantIdAndDate(@Param("restaurantId") Integer restaurantId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.date=:date ORDER BY v.restaurant, v.user")
    List<Vote> getByDate(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user=:user AND v.date=:date ORDER BY v.restaurant, v.user")
    Vote getByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date=:date ORDER BY v.restaurant, v.user")
    Vote getByUserIdAndDate(@Param("userId") Integer userId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date")
    List<Vote> getByUserId(@Param("userId") Integer userId);
}
