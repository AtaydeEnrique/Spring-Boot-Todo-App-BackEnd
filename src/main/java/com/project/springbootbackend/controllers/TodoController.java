package com.project.springbootbackend.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.springbootbackend.models.TodoModel;
import com.project.springbootbackend.services.TodoService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(path = "todos")
public class TodoController {   
    
    @GetMapping()
    ArrayList<TodoModel> todoList(@RequestParam(required = false, defaultValue = "") String sortBy, 
    @RequestParam(required = false, defaultValue = "") String direction, 
    @RequestParam(required = false, defaultValue = "0") Integer offset) {
        
        return TodoService.getAllTodos(sortBy,direction,offset);
    }

    @PostMapping()
    TodoModel newTodo(@RequestBody TodoModel newTodo ) throws Exception {
        try{
            return TodoService.addTodo(newTodo.getName(), newTodo.getPriority(), newTodo.getdueDate());
        }catch(Exception e){
            throw e;
        }
    }

    @PutMapping(path = "{id}")
    TodoModel replaceTodo(@RequestBody TodoModel newTodo, @PathVariable Long id) throws Exception {
        System.out.println(newTodo);
        try{
            return TodoService.updateTodo(id, newTodo);
        }catch(Exception e){
            throw e;
        }
  }

    @DeleteMapping(path = "{id}")
    void deleteTodo(@PathVariable Long id) {
        TodoService.deleteTodo(id);
    }



}
