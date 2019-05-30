package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// todo - should add example values for Swagger in the future
@Api(description = "Endpoint for admin to work with Users")
@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController extends AbstractUserController {

    public static final String REST_URL = WebUtil.ADMIN_URL + "/users";

    @ApiOperation(value = "Returns list of all Users")
    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @ApiOperation(value = "Returns User by given ID")
    @Override
    @GetMapping("/{id}")
    public User get(
            @ApiParam(required = true, value = "ID of wanted User")
            @PathVariable int id) {
        return super.get(id);
    }

    @ApiOperation(value = "Returns User by given EMail")
    @Override
    @GetMapping("/by")
    public User getByMail(
            @ApiParam(required = true, value = "EMail of wanted User")
            @RequestParam String email) {
        return super.getByMail(email);
    }

    @ApiOperation(value = "Create new User")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(
            @ApiParam(required = true, value = "prototype User to create (ID must be NULL)")
            @RequestBody
                    User user) {
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Update User")
    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(
            @ApiParam(required = true, value = "Prototype User to update")
            @RequestBody
                    User user,
            @ApiParam(required = true, value = "ID of User for update")
            @PathVariable
                    int id) {
        super.update(user, id);
    }

    @ApiOperation(value = "Delete User by ID")
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @ApiParam(required = true, value = "ID of User to delete")
            @PathVariable int id) {
        super.delete(id);
    }
}