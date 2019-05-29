package com.github.ivansenchukov.topjavagraduation.repository.datajpa;

import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaUserRepositoryImpl implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository UserDataJpaRepository;

    @Autowired
    public DataJpaUserRepositoryImpl(CrudUserRepository UserDataJpaRepository) {
        this.UserDataJpaRepository = UserDataJpaRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        return UserDataJpaRepository.save(user);
    }

    @Override
    public User get(int id) {
        return UserDataJpaRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return UserDataJpaRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return UserDataJpaRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return UserDataJpaRepository.delete(id) != 0;
    }
}
