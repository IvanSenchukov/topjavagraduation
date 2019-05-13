package com.github.ivansenchukov.topjavagraduation.service;

import com.github.ivansenchukov.topjavagraduation.exception.NotFoundException;
import com.github.ivansenchukov.topjavagraduation.model.Restaurant;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    // TODO - make documentation
    // TODO - if vote is absent - create. if present and time before 11:00 - update, if present and time after 11:00 - exception;
    public Vote makeVote(Vote vote) {
        Assert.notNull(vote, "vote must not be null");
        Assert.notNull(vote.getUser(), "Vote 'user' property must not be null");
        Assert.notNull(vote.getRestaurant(), "Vote 'restaurant' property must not be null");
        Assert.notNull(vote.getDate(), "Vote 'date' property must not be null");

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

    private Vote updatePresentVote(Vote vote, Vote presentVote) {
        Assert.isTrue(vote.getDate().isBefore(LocalDateTime.of(vote.getDate().toLocalDate(), LocalTime.of(11, 00))), "You are unable to change your choice after 11:00 AM!");
        vote.setId(presentVote.getId());
        vote = repository.save(vote);
        return checkNotFoundWithId(vote, presentVote.getId());
    }


    public Vote get(Integer id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Vote> get(Restaurant restaurant, LocalDate date) {
        return repository.get(restaurant, date);
    }

    public Map<Restaurant, Integer> getVotesCount(LocalDate date, Restaurant... restaurants) {
        return repository.getVotesCount(date, List.of(restaurants));
    }

    // TODO - maybe we should not use this method. Maybe should - only for user that makes this vote and only before 11:00
    // TODO - documentate this
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
