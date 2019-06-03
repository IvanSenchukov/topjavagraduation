package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.github.ivansenchukov.topjavagraduation.util.SecurityUtil.authUserId;

// todo - should add example values for Swagger in the future
// todo - make tests for new methods
@RestController
@RequestMapping(ProfileUserController.REST_URL)
public class ProfileUserController extends AbstractUserController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/profile";

    //todo - make documentation
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    //todo - make documentation
    @PatchMapping(value = "/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmail(
            @RequestParam String email,
            @RequestParam String password  /*todo - not good - rebuild this*/
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, password);

        userToUpdate.setEmail(email);
        super.update(userToUpdate, userId);
    }

    //todo - make documentation
    @PatchMapping(value = "/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateName(
            @RequestParam String name,
            @RequestParam String password /*todo - not good - rebuild this*/
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, password);

        userToUpdate.setName(name);
        super.update(userToUpdate, userId);
    }

    //todo - make documentation
    @PatchMapping(value = "/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @RequestParam String newPassword,  /*todo - not good - rebuild this*/
            @RequestParam String oldPassword   /*todo - not good - rebuild this*/
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, oldPassword);

        userToUpdate.setPassword(newPassword);
        super.update(userToUpdate, userId);
    }

    //todo - make documentation
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }


    private void checkPassword(User user, String password) {
        if (!Objects.equals(user.getPassword(), password)) {
            throw new RestrictedOperationException("Wrong Password!");
        }
    }
}