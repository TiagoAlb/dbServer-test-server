/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.dao;

import com.dbserver.dbservertestserver.model.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tiago
 */
public interface RestaurantDAO extends PagingAndSortingRepository<Restaurant, Integer> {

    @Query("SELECT restaurant FROM Restaurant restaurant WHERE restaurant.place_id = :place_id")
    public Optional<Restaurant> findByPlaceId(@Param("place_id") String place_id);
}
