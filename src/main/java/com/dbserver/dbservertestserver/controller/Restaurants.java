/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.controller;

import com.dbserver.dbservertestserver.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
    @Autowired
    private ObjectMapper objectMapper;
    
    @RequestMapping(path = "/restaurants", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JsonNode> list() throws IOException, JSONException {
        String googleKey = "AIzaSyCECNy_clrtfjPtZqV9r3DFYeN2f7Se-r8";
        URL url = new URL("https://maps.googleapis.com/maps/api/"
                + "place/nearbysearch/json?"
                + "type=restaurant"
                + "&radius=10000"
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
                    JSONObject objJson = new JSONObject(jsonArray.get(i).toString());
                    
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(objJson.getString("id"));
                    restaurant.setName(objJson.getString("name"));
                    
                    if(!objJson.has("opening_hours") && !objJson.isNull("opening_hours"))
                        restaurant.setOpen_now(objJson.getJSONObject("opening_hours").getBoolean("open_now"));
                    
                    String photo_reference = "";
                    
                    if(objJson.getJSONArray("photos").length() > 0)
                        photo_reference = objJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                    
                    restaurant.setUrlImage("https://maps.googleapis.com/maps/api/place/photo"
                            + "?maxwidth=1600"
                            + "&photoreference="+photo_reference
                            + "&key="+googleKey);
                    restaurant.setVicinity(objJson.getString("vicinity"));
                    restaurant.setVotes(0);
                    
                    restaurantsArray.put(new JSONObject(objectMapper.writeValueAsString(restaurant)));
                }
                
                jsonNode = objectMapper.readTree(restaurantsArray.toString()); 
        }
        
        return new ResponseEntity<JsonNode>(jsonNode, HttpStatus.OK);
    }
    
}
