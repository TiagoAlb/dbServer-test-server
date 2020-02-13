/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.controller;

import com.dbserver.dbservertestserver.dao.RestaurantDAO;
import com.dbserver.dbservertestserver.dao.UserDAO;
import com.dbserver.dbservertestserver.dao.VoteDAO;
import com.dbserver.dbservertestserver.dao.VoteRestaurantCountDAO;
import com.dbserver.dbservertestserver.dao.VotingDAO;
import com.dbserver.dbservertestserver.model.Restaurant;
import com.dbserver.dbservertestserver.model.User;
import com.dbserver.dbservertestserver.model.Vote;
import com.dbserver.dbservertestserver.model.VoteRestaurantCount;
import com.dbserver.dbservertestserver.model.Voting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tiago
 */
@RestController
@RequestMapping(path = "/api")
public class Users {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private VotingDAO votingDAO;
    @Autowired
    private VoteDAO voteDAO;
    @Autowired
    private VoteRestaurantCountDAO voteRestaurantCountDAO;
    @Autowired
    private RestaurantDAO restaurantDAO;

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) throws Exception {
        user.setId(0);
        return userDAO.save(user);
    }

    @RequestMapping(path = "/users/{userID}/restaurants/{restaurantID}/vote", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Vote vote(@PathVariable int userID, @PathVariable String restaurantID) throws JSONException, NotFoundException, Exception {
        List<Voting> votinsToday = votingDAO.findByStartDateAndCloseVoting();
        Voting today_voting = null;
        
        if(votinsToday.size() > 0)
           today_voting = votingDAO.findByStartDateAndCloseVoting().get(0);
        
        Optional<User> user = userDAO.findById(userID);

        Restaurants restaurantsController = new Restaurants();
        Restaurant restaurantGoogle = restaurantsController.getDetailsRestaurant(restaurantID);
        Vote vote = new Vote();
        
        if (user.isPresent()) {
            if (restaurantGoogle.getPlace_id() != null && !restaurantGoogle.getPlace_id().equals("")) {
                if(!userDAO.findUserVote(userID).isPresent()) {               
                    vote.setUser(user.get());

                    Optional<Restaurant> restaurant = restaurantDAO.findByPlaceId(restaurantID);

                    if (restaurant.isPresent()) {
                        Integer votingId = votingDAO.findVoteRestaurantWeek(restaurant.get().getId());
                        if(votingId > 0)
                            throw new Exception("Este restaurante já ganhou a votação nesta semana!");
                        else {
                            restaurantGoogle.setId(restaurant.get().getId());
                            vote.setRestaurant(restaurantDAO.save(restaurantGoogle));
                        }
                    } else {
                        restaurantGoogle.setId(0);
                        vote.setRestaurant(restaurantDAO.save(restaurantGoogle));
                    }

                    if (today_voting != null) {
                        today_voting.getVotes().add(voteDAO.save(vote));             
                    } else {
                        List<Vote> votes = new ArrayList<>();
                        today_voting = new Voting();
                        today_voting.setId(0);

                        votes.add(voteDAO.save(vote));
                        today_voting.setVotes(votes);
                    }
                } else throw new NotFoundException("O usuário já realizou seu voto!");
            } else throw new NotFoundException("Restaurante não encontrado!");
        } else throw new NotFoundException("Usuário não encontrado!");

        Voting votingSaved = votingDAO.save(today_voting);
        
        Optional<VoteRestaurantCount> votesCount = voteRestaurantCountDAO.findByVotingAndRestaurantId(votingSaved.getId(), vote.getRestaurant().getId());
        VoteRestaurantCount votesCountSaved = new VoteRestaurantCount();
        
        if(votesCount.isPresent()) {
            votesCountSaved = votesCount.get();
            votesCountSaved.setCountVotes(votesCountSaved.getCountVotes()+1);
        } else {
            votesCountSaved.setCountVotes(1);
            votesCountSaved.setRestaurant(vote.getRestaurant());
            votesCountSaved.setVoting(votingSaved);
        }
        
        voteRestaurantCountDAO.save(votesCountSaved);
        
        return vote;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<User> list() {
        return userDAO.findNotVotedUsers();
    }
}
