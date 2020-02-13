/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.dao;

import com.dbserver.dbservertestserver.model.Voting;
import java.sql.Date;
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
public interface VotingDAO extends PagingAndSortingRepository<Voting, Integer> {

    @Query("SELECT voting FROM Voting voting WHERE voting.start_date = CURRENT_DATE ORDER BY voting.id DESC")
    public List<Voting> findByStartDate();

    @Query("SELECT voting FROM Voting voting WHERE voting.start_date = CURRENT_DATE AND voting.closeVoting = false")
    public List<Voting> findByStartDateAndCloseVoting();

    @Query(value = "SELECT voting.id FROM vote_restaurant_count JOIN voting ON voting.id = vote_restaurant_count.voting_id WHERE WEEKDAY(voting.start_date) = WEEKDAY(current_date()) AND restaurant_id = :id", nativeQuery = true)
    public Integer findVoteRestaurantWeek(@Param("id") Integer id);
}
