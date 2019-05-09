package com.github.ivansenchukov.topjavagraduation.repository;

import com.github.ivansenchukov.topjavagraduation.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    // false if not found
    boolean delete(int id);
}
