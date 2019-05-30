package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.to.UserTo;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.github.ivansenchukov.topjavagraduation.util.SecurityUtil.authUserId;

// todo - should add example values for Swagger in the future
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
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    //todo - make documentation
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo) {
        super.update(userTo, authUserId());
    }
}