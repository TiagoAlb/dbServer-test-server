/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.dao;

import com.dbserver.dbservertestserver.model.Restaurant;
import com.dbserver.dbservertestserver.model.Vote;
import com.dbserver.dbservertestserver.model.Voting;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tiago
 */
@Repository
public interface VoteDAO extends PagingAndSortingRepository<Vote, Integer> {

    @Query("SELECT restaurant FROM Voting voting JOIN voting.votes vote JOIN vote.restaurant restaurant WHERE voting.start_date = CURRENT_DATE GROUP BY restaurant.id")
    public List<Restaurant> findRestaurantsByStart_DateVote();

    @Query("SELECT COUNT(vote) FROM Voting voting JOIN voting.votes vote JOIN vote.restaurant restaurant WHERE voting.id = :idVoting AND restaurant.id = :idRestaurant")
    public Integer findCountRestaurantsByVotingAndRestaurant(@Param("idVoting") Integer idVoting, @Param("idRestaurant") Integer idRestaurant);
}
