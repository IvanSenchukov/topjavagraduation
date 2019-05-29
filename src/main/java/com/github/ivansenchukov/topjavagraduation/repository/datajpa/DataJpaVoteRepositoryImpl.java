package com.github.ivansenchukov.topjavagraduation.repository.datajpa;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class DataJpaVoteRepositoryImpl implements VoteRepository {

    private static final Logger log = LoggerFactory.getLogger(DataJpaVoteRepositoryImpl.class);

    private final CrudVoteRepository voteDataJpaRepository;

    @Autowired
    public DataJpaVoteRepositoryImpl(CrudVoteRepository voteDataJpaRepository) {
        this.voteDataJpaRepository = voteDataJpaRepository;
    }

    @Override
    public Vote save(Vote vote) {
        return voteDataJpaRepository.save(vote);
    }

    @Override
    public Vote get(int id) {
        return voteDataJpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vote> getByRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return voteDataJpaRepository.getByRestaurantAndDate(restaurant, date);
    }

    @Override
    public List<Vote> getByRestaurantAndDate(Integer restaurantId, LocalDate date) {
        return voteDataJpaRepository.getByRestaurantIdAndDate(restaurantId, date);
    }

    @Override
    public Vote getByUserAndDate(User user, LocalDate date) {
        Vote vote = null;

        try {

            vote = voteDataJpaRepository.getByUserAndDate(user, date);

        } catch (NoResultException e) {
            log.warn("No result found for user=|{}| and date=|{}|", user.toString(), date.toString());
        }

        return vote;
    }

    @Override
    public Vote getByUserAndDate(Integer userId, LocalDate date) {
        Vote vote = null;

        try {

            vote = voteDataJpaRepository.getByUserIdAndDate(userId, date);

        } catch (NoResultException e) {
            log.warn("No result found for userId=|{}| and date=|{}|", userId.toString(), date.toString());
        }

        return vote;
    }

    @Override
    public List<Vote> getByUser(Integer userId) {
        List<Vote> votes = null;

        try {

            votes = voteDataJpaRepository.getByUserId(userId);

        } catch (NoResultException e) {
            log.warn("No result found for userId=|{}|", userId.toString());
        }

        return votes;
    }

    // todo - implement this
    @Override
    public Map<Restaurant, Integer> getVotesCount(LocalDate date, List<Restaurant> restaurants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int id) {
        return voteDataJpaRepository.delete(id) != 0;
    }
}
