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
public class UserControllerTest extends DbservertestServerApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private Users userController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testListUsersController() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPostSaveUserController() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .content("{ 'id': 0, 'name': 'Tiago Albuquerque' }")
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testUserVoteRestaurantController() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1/restaurants/ChIJM-VnhRp4GZURmyqnpIJSKNA/vote")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
