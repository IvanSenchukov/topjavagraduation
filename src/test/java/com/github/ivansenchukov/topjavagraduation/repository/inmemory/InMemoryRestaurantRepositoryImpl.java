package com.github.ivansenchukov.topjavagraduation.repository.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.RestaurantTestData;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.repository.RestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryRestaurantRepositoryImpl extends InMemoryBaseRepositoryImpl<Restaurant> implements RestaurantRepository {

    public InMemoryRestaurantRepositoryImpl() {
        refreshRepository();
    }

    public void refreshRepository() {
        entryMap.clear();
        entryMap.put(RestaurantTestData.VABI_VOBBLE_ID, RestaurantTestData.VABI_VOBBLE);
        entryMap.put(RestaurantTestData.MCDONNELS_ID, RestaurantTestData.MCDONNELS);
    }


    @Override
    public List<Restaurant> getAll() {
        return getCollection().stream()
                .sorted(Comparator.comparing(Restaurant::getName))
                .collect(Collectors.toList());
    }
}
