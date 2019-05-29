package com.github.ivansenchukov.topjavagraduation.repository.datajpa;

import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaRestaurantRepositoryImpl implements RestaurantRepository {
    private static final Sort SORT_RESTAURANT_BY_NAME = new Sort(Sort.Direction.ASC, "name");

    final
    CrudRestaurantRepository restaurantDataJpaRepository;

    @Autowired
    public DataJpaRestaurantRepositoryImpl(CrudRestaurantRepository restaurantDataJpaRepository) {
        this.restaurantDataJpaRepository = restaurantDataJpaRepository;
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return restaurantDataJpaRepository.save(restaurant);
    }

    @Override
    public Restaurant get(int id) {
        return restaurantDataJpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantDataJpaRepository.findAll(SORT_RESTAURANT_BY_NAME);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return restaurantDataJpaRepository.delete(id) != 0;
    }
}
