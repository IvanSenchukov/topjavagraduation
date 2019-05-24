package com.github.ivansenchukov.topjavagraduation.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
@NamedQueries({
        @NamedQuery(name = Vote.GET_BY_RESTAURANT_AND_DATE, query = "SELECT v FROM Vote v WHERE v.restaurant=:restaurant AND cast(v.dateTime as date)=:date ORDER BY v.restaurant, v.user"),
        @NamedQuery(name = Vote.GET_BY_RESTAURANT_ID_AND_DATE, query = "SELECT v FROM Vote v WHERE v.restaurant.id=:restaurantId AND cast(v.dateTime as date)=:date ORDER BY v.restaurant, v.user"),
        @NamedQuery(name = Vote.GET_BY_USER_AND_DATE, query = "SELECT v FROM Vote v WHERE v.user=:user AND cast(v.dateTime as date)=:date ORDER BY v.restaurant, v.user"),
        @NamedQuery(name = Vote.GET_BY_USER_ID_AND_DATE, query = "SELECT v FROM Vote v WHERE v.user.id=:userId AND cast(v.dateTime as date)=:date ORDER BY v.restaurant, v.user"),
        @NamedQuery(name = Vote.GET_BY_USER_ID, query = "SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.restaurant, v.user"),
        @NamedQuery(name = Vote.DELETE, query = "DELETE FROM Vote v WHERE v.id=:id"),
//        @NamedQuery(name = Vote.GET_VOTES_COUNT, query = "SELECT v FROM Vote v WHERE cast(v.dateTime as date)=:date ORDER BY v.restaurant"), // TODO - implement this
})
public class Vote extends AbstractBaseEntity {

    public static final String GET_BY_RESTAURANT_AND_DATE = "Vote.getByRestaurantAndDate";
    public static final String GET_BY_RESTAURANT_ID_AND_DATE = "Vote.getByRestaurantIdAndDate";
    public static final String GET_BY_USER_AND_DATE = "Vote.getByUserAndDate";
    public static final String GET_BY_USER_ID_AND_DATE = "Vote.getByUserIdAndDate";
    public static final String GET_BY_USER_ID = "Vote.getByUserId";
    public static final String GET_VOTES_COUNT = "Vote.getVotesCount";
    public static final String DELETE = "Vote.delete";

    //<editor-fold desc="Fields">
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    // todo - change this to "LocalDate" only
    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;
    //</editor-fold>


    //<editor-fold desc="Constructors">
    public Vote() {
    }

    public Vote(Vote prototype) {
        this(prototype.getId(), prototype.getUser(), prototype.getRestaurant(), prototype.getDateTime());
    }

    public Vote(User user, Restaurant restaurant, LocalDateTime dateTime) {
        this.user = user;
        this.dateTime = dateTime;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDateTime dateTime) {
        super(id);
        this.user = user;
        this.dateTime = dateTime;
        this.restaurant = restaurant;
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    //</editor-fold>+
}
