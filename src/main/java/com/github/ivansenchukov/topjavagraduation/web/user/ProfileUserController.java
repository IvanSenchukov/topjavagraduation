package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.github.ivansenchukov.topjavagraduation.util.SecurityUtil.authUserId;

// todo - should add example values for Swagger in the future
@Api(description = "Endpoint for user to work with his own profile")
@RestController
@RequestMapping(ProfileUserController.REST_URL)
public class ProfileUserController extends AbstractUserController {

    public static final String REST_URL = WebUtil.COMMON_URL + "/profile/";

    @ApiOperation(value = "Returns user profile")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    // todo - handle email validation exception (now it's 500 and we need 400)
    @ApiOperation(value = "Change User email. For this operation you need to type a password! Reset User authentication!")
    @PatchMapping(value = "/email")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void updateEmail(
            @ApiParam(required = true, value = "EMail and Password.")
            @RequestBody
                    UpdateEmailRequestTO updateEmailRequestTO
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, updateEmailRequestTO.password);

        userToUpdate.setEmail(updateEmailRequestTO.eMail);
        super.update(userToUpdate, userId);

        // todo - reset user authentication here
    }


    @ApiOperation(value = "Change User name. For this operation you need to type a password!")
    @PatchMapping(value = "/name")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void updateName(
            @ApiParam(required = true, value = "Name and Password.")
            @RequestBody
                    UpdateNameRequestTO updateNameRequestTO
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, updateNameRequestTO.password);

        userToUpdate.setName(updateNameRequestTO.name);
        super.update(userToUpdate, userId);
    }

    @ApiOperation(value = "Change User password. For this operation you need to type an old password! Reset user authentication!")
    @PatchMapping(value = "/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(
            @ApiParam(required = true, value = "New and Old and passwords.")
            @RequestBody UpdatePasswordRequestTO updatePasswordRequestTO
    ) {

        int userId = authUserId();

        User userToUpdate = service.get(userId);
        checkPassword(userToUpdate, updatePasswordRequestTO.oldPassword);

        userToUpdate.setPassword(updatePasswordRequestTO.newPassword);
        super.update(userToUpdate, userId);

        // todo - reset user authentication here
    }

    @ApiOperation(value = "Delete user account")
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
        public String eMail;
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