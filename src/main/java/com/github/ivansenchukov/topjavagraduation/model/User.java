package com.github.ivansenchukov.topjavagraduation.model;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class User extends AbstractBaseEntity {

    //<editor-fold desc="Fields">
    private String name;

    private String email;

    private String password;

    private boolean enabled = true;

    private Date registered = new Date();

    private Set<Role> roles;
    //</editor-fold>


    //<editor-fold desc="Constructors">
    public User() {
    }

    public User(User prototype) {
        this(
                prototype.getId(),
                prototype.getName(),
                prototype.getEmail(),
                prototype.getPassword(),
                prototype.isEnabled(),
                prototype.getRegistered(),
                prototype.getRoles()
        );
    }

    public User(
            Integer id,
            String name,
            String email,
            String password,
            boolean enabled,
            Date registered,
            Role role,
            Role... roles
    ) {
        this(id, name, email, password, enabled, registered, EnumSet.of(role, roles));
    }

    public User(
            Integer id,
            String name,
            String email,
            String password,
            boolean enabled,
            Date registered,
            Collection<Role> roles
    ) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }
    //</editor-fold>

}
