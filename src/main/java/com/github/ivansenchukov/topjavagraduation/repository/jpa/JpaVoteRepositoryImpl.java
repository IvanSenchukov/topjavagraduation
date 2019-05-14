package com.github.ivansenchukov.topjavagraduation.repository.jpa;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class JpaVoteRepositoryImpl implements VoteRepository {

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

    // todo - implement this
    @Override
    public List<Vote> get(Restaurant restaurant, LocalDate date) {
        throw new UnsupportedOperationException();
    }

    // todo - implement this
    @Override
    public Vote get(LocalDate date, User user) {
        throw new UnsupportedOperationException();
    }

    // todo - implement this
    @Override
    public Map<Restaurant, Integer> getVotesCount(LocalDate date, List<Restaurant> restaurants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int id) {
        Query query = em.createQuery("DELETE FROM Dish d WHERE d.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }
}
