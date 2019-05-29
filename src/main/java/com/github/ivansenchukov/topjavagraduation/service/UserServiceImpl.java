package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.AuthorizedUser;
import com.github.ivansenchukov.topjavagraduation.exception.DuplicateException;
import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import com.github.ivansenchukov.topjavagraduation.to.UserTo;
import com.github.ivansenchukov.topjavagraduation.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNotFound;
import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNotFoundWithId;

//https://stackoverflow.com/questions/6586727/spring-error-beannotofrequiredtypeexception/6586802
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");

        // TODO - remove this workaround for duplicates check after solving it on the Repository layer
        //<editor-fold desc="Duplicate by email workaround">
        User presentUser = repository.getByEmail(user.getEmail());
        if (Objects.nonNull(presentUser))
            throw new DuplicateException(String.format("User with email %s is already exists", user.getEmail()));
        //</editor-fold>

        return repository.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.getId());
        repository.save(UserUtil.updateFromTo(user, userTo));
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(user);  // !! need only for JDBC implementation
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " not found");
        }
        return new AuthorizedUser(user);
    }
}
