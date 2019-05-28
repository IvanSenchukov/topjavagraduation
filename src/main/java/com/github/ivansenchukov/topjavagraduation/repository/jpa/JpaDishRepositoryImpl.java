package com.github.ivansenchukov.topjavagraduation.repository.jpa;

import com.github.ivansenchukov.topjavagraduation.model.Dish;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.RestaurantRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JpaDishRepositoryImpl implements DishRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Dish save(Dish dish) {
        if (dish.isNew()) {
            em.persist(dish);
            return dish;
        } else {
            return em.merge(dish);
        }
    }

    @Override
    public Dish get(int id) {
        return em.find(Dish.class, id);
    }

    @Override
    public List<Dish> getByRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return em.createNamedQuery(Dish.GET_BY_RESTAURANT_AND_DATE)
                .setParameter("restaurant", restaurant)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Dish> getByRestaurantIdAndDate(Integer restaurantId, LocalDate date) {
        return em.createNamedQuery(Dish.GET_BY_RESTAURANT_ID_AND_DATE)
                .setParameter("restaurantId", restaurantId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Dish.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }
}
