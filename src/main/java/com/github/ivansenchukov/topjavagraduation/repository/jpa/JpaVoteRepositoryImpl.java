package com.github.ivansenchukov.topjavagraduation.repository.jpa;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class JpaVoteRepositoryImpl implements VoteRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaVoteRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Vote save(Vote vote) {
        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            return em.merge(vote);
        }
    }

    @Override
    public Vote get(int id) {
        return em.find(Vote.class, id);
    }

    @Override
    public List<Vote> get(Restaurant restaurant, LocalDate date) {
        return em.createNamedQuery(Vote.GET_BY_RESTAURANT_AND_DATE)
                .setParameter("restaurant", restaurant)
                .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .getResultList();
    }

    @Override
    public Vote get(LocalDate date, User user) {
        Vote vote = null;

        try {
            vote = (Vote) em.createNamedQuery(Vote.GET_BY_DATE_AND_USER)
                    .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.warn("No result found for user=|{}| and date=|{}|", user.toString(), date.toString());
        }

        return vote;
    }

    // todo - implement this
    @Override
    public Map<Restaurant, Integer> getVotesCount(LocalDate date, List<Restaurant> restaurants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Vote.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }
}
