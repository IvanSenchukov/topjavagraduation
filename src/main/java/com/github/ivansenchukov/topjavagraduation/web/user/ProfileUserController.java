package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    // todo - handle email validation exception (now it's 500 and we need 400)
    @PatchMapping(value = "/email")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmail(
            @RequestBody UpdateEmailRequestTO updateEmailRequestTO
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, updateEmailRequestTO.password);

        userToUpdate.setEmail(updateEmailRequestTO.email);
        super.update(userToUpdate, userId);

        // todo - reset user authentication here
        // todo -  add to documentation -> reset user authentication here
    }


    //todo - make documentation
    @PatchMapping(value = "/name")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateName(
            @RequestBody UpdateNameRequestTO updateNameRequestTO
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, updateNameRequestTO.password);

        userToUpdate.setName(updateNameRequestTO.name);
        super.update(userToUpdate, userId);
    }

    //todo - make documentation
    @PatchMapping(value = "/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @RequestBody UpdatePasswordRequestTO updatePasswordRequestTO
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, updatePasswordRequestTO.oldPassword);

        userToUpdate.setPassword(updatePasswordRequestTO.newPassword);
        super.update(userToUpdate, userId);

        // todo - reset user authentication here
        // todo -  add to documentation -> reset user authentication here
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

    //<editor-fold desc="Nested Transport Objects">

    static class UpdateEmailRequestTO {
        public String email;
        public String password;
    }

    static class UpdateNameRequestTO {
        public String name;
        public String password;
    }

    static class UpdatePasswordRequestTO {
        public String oldPassword;
        public String newPassword;
    }

    //</editor-fold>

}