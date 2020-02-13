/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver;

import com.dbserver.dbservertestserver.controller.Restaurants;
import com.dbserver.dbservertestserver.controller.Users;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Tiago
 */
public class RestaurantControllerTest extends DbservertestServerApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private Restaurants restaurantController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void testListRestaurantsController() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testListVotingRestaurantsController() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/voting")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testVotingEndController() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/restaurants/voting/end")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
