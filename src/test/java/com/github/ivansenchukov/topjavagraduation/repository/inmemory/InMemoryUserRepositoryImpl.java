package com.github.ivansenchukov.topjavagraduation.repository.inmemory;

import com.github.ivansenchukov.topjavagraduation.repository.inmemory.testdata.UserTestData;
import com.github.ivansenchukov.topjavagraduation.exception.DuplicateException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl extends InMemoryBaseRepositoryImpl<User> implements UserRepository {

    public InMemoryUserRepositoryImpl() {
        refreshRepository();
    }


    public void refreshRepository() {
        entryMap.clear();
        entryMap.put(UserTestData.ADMIN_ID, UserTestData.ADMIN);
        entryMap.put(UserTestData.USER_FIRST_ID, UserTestData.USER_FIRST);
        entryMap.put(UserTestData.USER_SECOND_ID, UserTestData.USER_SECOND);
    }

    @Override
    public User save(User entry) {
        if (entry.isNew() && checkEmailCollision(entry)) {
            throw new DuplicateException(String.format("User with email %s is already exists", entry.getEmail()));
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
