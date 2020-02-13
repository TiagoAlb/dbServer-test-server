/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.dao;

import com.dbserver.dbservertestserver.model.VoteRestaurantCount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tiago
 */
public interface VoteRestaurantCountDAO extends PagingAndSortingRepository<VoteRestaurantCount, Integer> {

    @Query("SELECT votecount FROM VoteRestaurantCount votecount JOIN votecount.voting voting JOIN votecount.restaurant restaurant WHERE voting.start_date = CURRENT_DATE AND voting.closeVoting = false ORDER BY voting.id, votecount.countVotes DESC")
    public List<VoteRestaurantCount> findRestaurantsVoteCount();

    @Query("SELECT votecount FROM VoteRestaurantCount votecount JOIN votecount.voting voting JOIN votecount.restaurant restaurant WHERE voting.id = :votingID AND restaurant.id = :restaurantID")
    public Optional<VoteRestaurantCount> findByVotingAndRestaurantId(@Param("votingID") Integer votingID, @Param("restaurantID") Integer restaurantID);

    @Query("SELECT votecount FROM VoteRestaurantCount votecount JOIN votecount.voting voting JOIN votecount.restaurant restaurant WHERE voting.id = :id")
    public Optional<VoteRestaurantCount> findWinnerRestaurant(@Param("id") Integer id);
}
