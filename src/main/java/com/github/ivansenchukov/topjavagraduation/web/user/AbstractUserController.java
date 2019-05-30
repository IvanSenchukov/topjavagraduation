package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import com.github.ivansenchukov.topjavagraduation.to.UserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.assureIdConsistent;
import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;


    //<editor-fold desc="GET">

    /**
     *
     * @return - list of all users
     */
    public List<User> getAll() {
        log.info("get all users");
        return service.getAll();
    }

    /**
     *
     * @param id    - ID of wanted User
     * @return      - User by ID
     */
    public User get(int id) {
        log.info("get user with id |{}|", id);
        return service.get(id);
    }

    /**
     *
     * @param email     - email of wanted user
     * @return          - User by Email
     */
    public User getByMail(String email) {
        log.info("get user by email |{}|", email);
        return service.getByEmail(email);
    }
    //</editor-fold>


    //<editor-fold desc="CREATE">

    /**
     * Create new User
     *
     * @param user  - prototype User to create (ID must be NULL)
     * @return      - created User
     */
    public User create(User user) {
        log.info("create user |{}|", user);
        checkNew(user);
        return service.create(user);
    }
    //</editor-fold>


    //<editor-fold desc="UPDATE">

    /**
     * Update User
     *
     * @param user  - prototype User to update
     * @param id    - ID of User for update
     */
    public void update(User user, int id) {
        log.info("update user |{}| with id=|{}|", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    /**
     * Update User by User Transport object
     *
     * @param userTo    - Transport Object of prototype User to update
     * @param id        - ID of User for update
     */
    public void update(UserTo userTo, int id) {
        log.info("update user |{}| with id=|{}|", userTo, id);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }

    // todo - make implementation for it or delete
    // todo - and then make changes to all code - disabled users must not be authorized
    // todo - make documentation
    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable user with id=|{}|" : "disable user with id=|{}|", id);
        service.enable(id, enabled);
    }
    //</editor-fold>


    //<editor-fold desc="DELETE">

    /**
     * Delete User by given ID
     *
     * @param id - ID of User to delete
     */
    public void delete(int id) {
        log.info("delete user with id=|{}|", id);
        service.delete(id);
    }
    //</editor-fold>
}