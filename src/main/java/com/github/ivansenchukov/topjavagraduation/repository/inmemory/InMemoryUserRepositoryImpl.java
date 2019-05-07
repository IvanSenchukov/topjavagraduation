package com.github.ivansenchukov.topjavagraduation.repository.inmemory;

import com.github.ivansenchukov.topjavagraduation.UserTestData;
import com.github.ivansenchukov.topjavagraduation.exception.DublicateException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.ivansenchukov.topjavagraduation.UserTestData.ADMIN;
import static com.github.ivansenchukov.topjavagraduation.UserTestData.USER;

public class InMemoryUserRepositoryImpl extends InMemoryBaseRepositoryImpl<User> implements UserRepository {


    public InMemoryUserRepositoryImpl() {
        entryMap.clear();
        entryMap.put(UserTestData.ADMIN_ID, UserTestData.ADMIN);
        entryMap.put(UserTestData.USER_ID, UserTestData.USER);
    }

    @Override
    public User save(User entry) {
        if(entry.isNew() && checkEmailCollision(entry)) {
            throw new DublicateException(String.format("User with email %s is already exists", entry.getEmail()));
        }
        return super.save(entry);
    }

    @Override
    public List<User> getAll() {
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");
        return getCollection().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    private boolean checkEmailCollision(User entry) {
        User existed = getByEmail(entry.getEmail());
        return Objects.nonNull(existed);
    }
}
