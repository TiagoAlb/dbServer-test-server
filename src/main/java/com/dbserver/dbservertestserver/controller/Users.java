/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.controller;

import com.dbserver.dbservertestserver.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @RequestMapping(path = "/users", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) throws Exception {
        String path = System.getProperty("user.home") + "\\documents\\system_data\\";
        String fileName = "users_data.json";
        
        JSONArray jsonArray = new JSONArray(analyzeFile(path, fileName));
        
        user.setId(jsonArray.length()+1);
        jsonArray.put(new JSONObject(objectMapper.writeValueAsString(user)));
        
        alterFile(jsonArray.toString(), path + fileName);
        
        return user;
    }
    
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JsonNode> list() throws JSONException, IOException  {
        String path = System.getProperty("user.home") + "\\documents\\system_data\\";
        String fileName = "users_data.json";
        
        JsonNode jsonNode = objectMapper.readTree(analyzeFile(path, fileName));
 
        return new ResponseEntity<JsonNode>(jsonNode, HttpStatus.OK);
    }
    
    public String analyzeFile(String fileDirectory, String fileName) {
        StringBuilder sb = new StringBuilder();
        
        createFile(fileDirectory, fileName);
        
        fileDirectory = fileDirectory + fileName; 
        
        try (Stream<String> lines = Files.lines(Paths.get(fileDirectory))){
            lines.forEach((line)->{
                sb.append(line.trim());
            });
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
	} finally {
            return sb.toString();
        }
    }
    
    public void createFile(String fileDirectory, String fileName) {
        try {
            File file = new File(fileDirectory);
            String [] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return file.isDirectory() && name.equals(fileName);
                }
            });
            
            if(!file.isDirectory())
                file.mkdirs();
            
            if(files.length==0)
                if(new File(fileDirectory + fileName).createNewFile()) {
                    FileWriter writer = new FileWriter(fileDirectory + fileName);
                    writer.write("[]");
                    writer.close();
                }

        } catch (IOException e) {
            e.printStackTrace();
	}
    }
    
    public boolean alterFile(String jsonArray, String file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray);
            writer.close();
            
            return true;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
