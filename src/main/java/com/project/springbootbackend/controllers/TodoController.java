package com.project.springbootbackend.controllers;

import org.springframework.stereotype.Controller;

import java.net.HttpURLConnection;
import java.net.URL;

import com.project.springbootbackend.models.TodoModel;

@Controller
public class TodoController {
    
    URL url = new URL("http://example.com");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
}
