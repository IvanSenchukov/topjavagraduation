package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Role;
import com.github.ivansenchukov.topjavagraduation.model.User;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNotFound;
import static com.github.ivansenchukov.topjavagraduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    // TODO - implement this!!!
    @Value("${stoptime}")
    public static String stoptime;

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
    public List<Vote> get(Restaurant restaurant, LocalDate date) {
        return repository.get(restaurant, date);
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
     *  only user with role "USER" can make votes.
     *
     *  If vote is absent - create new one
     *  If vote is present - look at time
     *   - If time before 11:00 - update vote
     *   - If time after 11:00 - throw RestrictedOperationException
     *
     * @param vote - vote, that user wants to make. vote properties 'user', 'restaurant' and 'date' must not be null
     * @return
     * @throws RestrictedOperationException - in case that vote is present and update request is coming after stoptime
     */
    public Vote makeVote(Vote vote) throws RestrictedOperationException {
        Assert.notNull(vote, "vote must not be null");
        Assert.notNull(vote.getUser(), "Vote 'user' property must not be null");
        Assert.notNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null");
        Assert.notNull(vote.getDate(), "Vote 'date' property must not be null");

        // This fetch is also a check for correct user is making update
        Vote presentVote = repository.get(vote.getDate().toLocalDate(), vote.getUser());
        if (Objects.isNull(presentVote)) {
            return saveNewVote(vote);
        } else {
            return updatePresentVote(vote, presentVote);
        }
    }

    private Vote saveNewVote(Vote vote) {
        return repository.save(vote);
    }

    private Vote updatePresentVote(Vote vote, Vote presentVote) throws RestrictedOperationException {
        checkStoptime(vote, vote.getDate(), "You can't change your choice after 11:00!");
        vote.setId(presentVote.getId());
        vote = repository.save(vote);
        return checkNotFoundWithId(vote, presentVote.getId());
    }

    /**
     * Delete user's vote, in case he make request for it before stoptime(11:00)
     * Otherwise - RestrictedOperationException
     *
     * User can delete only his own votes
     * If another user tries to delete vote - throws RestrictedOperationException
     *
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
        if (!CollectionUtils.contains(user.getRoles().iterator(), Role.ROLE_ADMIN)){
            checkStoptime(presentVote, dateTime, "You can't delete your vote after 11:00!");
        }
    }


    private void checkStoptime(Vote vote, LocalDateTime date, String errorMessage) throws RestrictedOperationException {
        if (date.isAfter(LocalDateTime.of(vote.getDate().toLocalDate(), LocalTime.of(11, 00)))) { // TODO - set stoptime here from PropertyPlaceholder
            throw new RestrictedOperationException(errorMessage);
        }
    }

    private void checkUserAuthorization(User user, Vote presentVote) throws RestrictedOperationException {
        if (!CollectionUtils.contains(user.getRoles().iterator(), Role.ROLE_ADMIN) && !Objects.equals(presentVote.getUser(), user)) throw new RestrictedOperationException("You can only delete your own votes!");
    }
}
