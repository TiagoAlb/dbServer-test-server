/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.controller;

import com.dbserver.dbservertestserver.dao.VoteRestaurantCountDAO;
import com.dbserver.dbservertestserver.dao.VotingDAO;
import com.dbserver.dbservertestserver.model.Restaurant;
import com.dbserver.dbservertestserver.model.VoteRestaurantCount;
import com.dbserver.dbservertestserver.model.Voting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class Restaurants {
    public static final String googleKey = "AIzaSyCECNy_clrtfjPtZqV9r3DFYeN2f7Se-r8";
    
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VoteRestaurantCountDAO voteRestaurantCountDAO;
    @Autowired
    private VotingDAO votingDAO;
    
    @RequestMapping(path = "/restaurants", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JsonNode> list() throws IOException, JSONException {
        return new ResponseEntity<>(getRestaurantsGoogle(), HttpStatus.OK);
    }
    @RequestMapping(path = "/restaurants/voting", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<VoteRestaurantCount> listRestaurantsVoting() {
        return voteRestaurantCountDAO.findRestaurantsVoteCount();
    }
    
    @RequestMapping(path = "/restaurants/voting/end", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Optional<VoteRestaurantCount> votingEnd() throws JSONException, IOException, NotFoundException {
        Voting voting = null; 
        List<Voting> votinsToday = votingDAO.findByStartDate();
        
        if(votinsToday.size() > 0)
            voting = votinsToday.get(0);
                
        Optional<VoteRestaurantCount> winner;
        
        if(voting != null) {
            winner = voteRestaurantCountDAO.findWinnerRestaurant(voting.getId());
            
            if(winner.isPresent()) {
                voting.setClose(true);
                voting.setWinner(winner.get().getRestaurant());
                votingDAO.save(voting);
            } else throw new NotFoundException("Erro ao buscar contagem de votos!");
        } else throw new NotFoundException("Votação não encontrada!");
        
        return winner;
    }
    
    public JsonNode getRestaurantsGoogle() throws IOException, JSONException {
        URL url = new URL("https://maps.googleapis.com/maps/api/"
                + "place/nearbysearch/json?"
                + "type=restaurant"
                + "&radius=2000"
                + "&location=-30.0596914,-51.173819"
                + "&key="+googleKey);
        
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET"); 
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        http.setRequestProperty("Accept", "application/json");
        http.connect();
        
        JsonNode jsonNode;
        
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(http.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                JSONArray jsonArray = new JSONArray(objectMapper.readTree(response.toString()).get("results").toString());
                JSONArray restaurantsArray = new JSONArray();
              
                for(int i=0; i < jsonArray.length(); i++) {
                    restaurantsArray.put(
                            new JSONObject(
                            objectMapper.writeValueAsString(
                            getDetailsRestaurant(new JSONObject(jsonArray.get(i).toString()).getString("place_id")))));
                }
                
                jsonNode = objectMapper.readTree(restaurantsArray.toString()); 
        }
        return jsonNode;
    }
    
    public Restaurant getObjectRestaurant(JSONObject object) throws JSONException, IOException {
        Restaurant restaurant = new Restaurant();
        if(object.length() > 0) {
            restaurant.setPlace_id(object.getString("place_id"));
            restaurant.setName(object.getString("name"));

            if(object.has("formatted_phone_number") && !object.isNull("formatted_phone_number"))
                restaurant.setFormatted_phone_number(object.getString("formatted_phone_number"));

            if(object.has("opening_hours") && !object.isNull("opening_hours"))
                restaurant.setOpen_now(object.getJSONObject("opening_hours").getBoolean("open_now"));

            String photo_reference = "";

            if(object.has("photos") && !object.isNull("photos"))
                photo_reference = object.getJSONArray("photos").getJSONObject(0).getString("photo_reference");

            restaurant.setUrlImage("https://maps.googleapis.com/maps/api/place/photo"
                + "?maxwidth=1600"
                + "&photoreference="+photo_reference
                + "&key="+googleKey);
            restaurant.setVicinity(object.getString("vicinity"));
        }
        
        return restaurant;
    }
    
    public Restaurant getDetailsRestaurant(String place_id) throws IOException, JSONException {
        URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json"
                + "?fields=place_id,name,formatted_phone_number,photo,opening_hours,vicinity"
                + "&place_id="+place_id
                + "&key="+googleKey);
        
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET"); 
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        http.setRequestProperty("Accept", "application/json");
        http.connect();
        
        JSONObject jsonObject = null;
        ObjectMapper objectMapperDetails = new ObjectMapper();
 
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(http.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                if(!response.toString().equals("") && objectMapperDetails.readTree(response.toString()).get("result")!=null)
                    jsonObject = new JSONObject(objectMapperDetails.readTree(response.toString()).get("result").toString());
                else
                    jsonObject = new JSONObject("{}");
        }       
        
        return getObjectRestaurant(jsonObject);
    }
}
