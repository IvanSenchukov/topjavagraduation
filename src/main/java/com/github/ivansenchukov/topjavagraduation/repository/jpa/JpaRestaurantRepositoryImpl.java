package com.github.ivansenchukov.topjavagraduation.repository.jpa;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.RestaurantRepository;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class JpaRestaurantRepositoryImpl implements RestaurantRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Restaurant save(Restaurant restaurant) {
        if (restaurant.isNew()) {
            em.persist(restaurant);
            return restaurant;
        } else {
            return em.merge(restaurant);
        }
    }

    @Override
    public Restaurant get(int id) {
        return em.find(Restaurant.class, id);
    }

    @Override
    public List<Restaurant> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int id) {
        Query query = em.createQuery("DELETE FROM Restaurant r WHERE r.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }
}
