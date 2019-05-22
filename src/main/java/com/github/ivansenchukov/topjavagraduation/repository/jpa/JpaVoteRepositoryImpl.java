package com.github.ivansenchukov.topjavagraduation.repository.jpa;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    public List<Vote> getByRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return em.createNamedQuery(Vote.GET_BY_RESTAURANT_AND_DATE)
                .setParameter("restaurant", restaurant)
                .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .getResultList();
    }

    @Override
    public List<Vote> getByRestaurantAndDate(Integer restaurantId, LocalDate date) {
        return em.createNamedQuery(Vote.GET_BY_RESTAURANT_ID_AND_DATE)
                .setParameter("restaurantId", restaurantId)
                .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .getResultList();
    }

    @Override
    public Vote getByUserAndDate(User user, LocalDate date) {
        Vote vote = null;

        try {
            vote = (Vote) em.createNamedQuery(Vote.GET_BY_USER_AND_DATE)
                    .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.warn("No result found for user=|{}| and date=|{}|", user.toString(), date.toString());
        }

        return vote;
    }

    @Override
    public Vote getByUserAndDate(Integer userId, LocalDate date) {
        Vote vote = null;

        try {
            vote = (Vote) em.createNamedQuery(Vote.GET_BY_USER_AND_DATE)
                    .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .setParameter("user", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.warn("No result found for userId=|{}| and date=|{}|", userId.toString(), date.toString());
        }

        return vote;
    }

    @Override
    public List<Vote> getByUser(Integer userId) {
        List<Vote> votes = null;

        try {
            votes = em.createNamedQuery(Vote.GET_BY_USER_ID)
                    .setParameter("userId", userId)
                    .getResultList();
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
        return em.createNamedQuery(Vote.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }
}
