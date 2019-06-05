package com.github.ivansenchukov.topjavagraduation.web.user;


import com.github.ivansenchukov.topjavagraduation.model.Role;
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
import java.util.Date;
import java.util.List;

// todo - should add example values for Swagger in the future
// todo - build tests for new methods
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
    @PostMapping
    public ResponseEntity<User> createWithLocation(
            @ApiParam(required = true, value = "Representation of new User")
            @RequestBody
                    CreateNewUserRequestTO requestTO
    ) {

        String name     = requestTO.name;
        String email    = requestTO.eMail;
        String password = requestTO.password;

        User newUser = new User(null, name, email, password, true, new Date(), Role.ROLE_USER);
        User created = super.create(newUser);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Set Enabled")
    @PatchMapping(value = "/enable/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void setEnabled(
            @ApiParam(required = true, value = "Enable/Disable user")
            @RequestParam
                    boolean enabled,
            @ApiParam(required = true, value = "ID of User for update")
            @PathVariable
                    int id) {

        service.enable(id, enabled);
    }

    @ApiOperation(value = "Update User Roles")
    @PatchMapping(value = "/roles/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateUserRoles(
            @ApiParam(required = true, value = "Roles to give User")
            @RequestBody
                    List<Role> roles,
            @ApiParam(required = true, value = "ID of User for update")
            @PathVariable
                    int id) {

        User userToEnable = service.get(id);
        userToEnable.setRoles(roles);
        super.update(userToEnable, id);
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

    //<editor-fold desc="Request Transfer Objects">
    public static class CreateNewUserRequestTO {
        public String name;
        public String eMail;
        public String password;
    }
    //</editor-fold>
}