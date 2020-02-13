/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.dao;

import com.dbserver.dbservertestserver.model.User;
import com.dbserver.dbservertestserver.model.VoteRestaurantCount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tiago
 */
@Repository
public interface UserDAO extends PagingAndSortingRepository<User, Integer> {
   @Query("SELECT user FROM User user WHERE user.id NOT IN (SELECT user.id FROM Voting voting JOIN voting.votes vote JOIN vote.user user WHERE voting.start_date = CURRENT_DATE AND voting.closeVoting = false)")
    public List<User> findNotVotedUsers();
    
    @Query("SELECT user FROM Voting voting JOIN voting.votes vote JOIN vote.user user WHERE voting.start_date = CURRENT_DATE AND voting.closeVoting = false AND user.id = :id")
    public Optional<User> findUserVote(@Param("id") Integer id);
}
