package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Role;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private VoteRepository repository;

    private LocalTime stopVotingTime;

    @Autowired
    public VoteService(VoteRepository repository, LocalTime stopVotingTime) {
        this.repository = repository;
        this.stopVotingTime = stopVotingTime;
    }


    //<editor-fold desc="Getters and Setters">
    public LocalTime getStopVotingTime() {
        return stopVotingTime;
    }
    //</editor-fold>


    public Vote get(Integer id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    /**
     * Returns the list of votes on given restaurant and date
     * Returns empty list if no vote is present
     *
     * @param restaurant
     * @param date
     * @return
     */
    public List<Vote> getByRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return repository.getByRestaurantAndDate(restaurant, date);
    }

    /**
     * Returns the list of votes on given restaurant and date
     * Returns empty list if no vote is present
     *
     * @param restaurantId
     * @param date
     * @return
     */
    public List<Vote> getByRestaurantAndDate(Integer restaurantId, LocalDate date) {
        return repository.getByRestaurantAndDate(restaurantId, date);
    }

    public List<Vote> getByUser(Integer userId) {
        return repository.getByUser(userId);
    }

    /**
     * Returns number of votes, that given restaurants have on a given date
     * Returns list of given restaurants and zeroes if no vote is present
     *
     * @param date
     * @param restaurants
     * @return
     */
    public Map<Restaurant, Integer> getVotesCount(LocalDate date, Restaurant... restaurants) {
        return repository.getVotesCount(date, List.of(restaurants));
    }


    /**
     * only user with role "USER" can make votes.
     * <p>
     * If vote is absent - create new one
     * If vote is present - look at time
     * - If time before stoptime - update vote
     * - If time after stoptime - throw RestrictedOperationException
     *
     * @param vote - vote, that user wants to make. vote properties 'user', 'restaurant' and 'date' must not be null
     * @return
     * @throws RestrictedOperationException - in case that vote is present and update request is coming after stoptime
     */
    public Vote makeVote(Vote vote, LocalDateTime votingTime) throws RestrictedOperationException {
        Assert.notNull(vote, "vote must not be null");
        Assert.notNull(vote.getUser(), "Vote 'user' property must not be null");
        Assert.notNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null");
        Assert.notNull(vote.getDate(), "Vote 'date' property must not be null");

        if (!CollectionUtils.contains(vote.getUser().getRoles().iterator(), Role.ROLE_USER)) {
            throw new RestrictedOperationException("Only users with role 'USER' can make votes!");
        }

        // This fetch is also a check for correct user is making update
        Vote presentVote = repository.getByUserAndDate(vote.getUser(), vote.getDate());
        if (Objects.isNull(presentVote)) {
            return saveNewVote(vote);
        } else {
            return updatePresentVote(vote, presentVote, votingTime);
        }
    }

    private Vote saveNewVote(Vote vote) {
        return repository.save(vote);
    }

    private Vote updatePresentVote(Vote vote, Vote presentVote, LocalDateTime votingTime) throws RestrictedOperationException {
        checkStoptime(presentVote, votingTime, String.format("You can't change your choice after %s", stopVotingTime.toString()));
        vote.setId(presentVote.getId());
        vote = repository.save(vote);
        return checkNotFoundWithId(vote, presentVote.getId());
    }

    /**
     * Delete user's vote, in case he make request for it before stoptime
     * Otherwise - RestrictedOperationException
     * <p>
     * User can delete only his own votes
     * If another user tries to delete vote - throws RestrictedOperationException
     * <p>
     * Admins can delete any user vote at any time
     *
     * @param id
     * @param user
     * @param dateTime
     * @throws NotFoundException
     * @throws RestrictedOperationException - in case that delete request is coming after stoptime, user mismatch
     */
    public void delete(int id, User user, LocalDateTime dateTime) throws NotFoundException, RestrictedOperationException {

        Vote presentVote = repository.get(id);
        if (Objects.nonNull(presentVote)) {
            checkUserAuthorizationAndStoptime(user, dateTime, presentVote);
            checkNotFoundWithId(repository.delete(id), id);
        } else {
            checkNotFoundWithId(repository.delete(id), id);
        }
    }

    private void checkUserAuthorizationAndStoptime(User user, LocalDateTime dateTime, Vote presentVote) throws RestrictedOperationException {
        checkUserAuthorization(user, presentVote);
        if (!CollectionUtils.contains(user.getRoles().iterator(), Role.ROLE_ADMIN)) {
            checkStoptime(presentVote, dateTime, String.format("You can't delete your vote after %s!", stopVotingTime.toString()));
        }
    }


    /**
     *  User can change his vote only before stoptime at his voting date
     *
     * @param presentVote - vote, that has been made by user in the past
     * @param votingDate - date and time of current user request
     * @param errorMessage
     * @throws RestrictedOperationException - if voting date and time is after current vote's date and stoptime
     */
    private void checkStoptime(Vote presentVote, LocalDateTime votingDate, String errorMessage) throws RestrictedOperationException {
        if (votingDate.isAfter(presentVote.getDate().atTime(stopVotingTime))) {
            throw new RestrictedOperationException(errorMessage);
        }
    }

    private void checkUserAuthorization(User user, Vote presentVote) throws RestrictedOperationException {
        if (!CollectionUtils.contains(user.getRoles().iterator(), Role.ROLE_ADMIN) && !Objects.equals(presentVote.getUser(), user))
            throw new RestrictedOperationException("You can only delete your own votes!");
    }
}
