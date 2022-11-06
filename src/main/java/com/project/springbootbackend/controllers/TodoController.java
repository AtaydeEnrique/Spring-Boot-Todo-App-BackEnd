package com.project.springbootbackend.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    
    @GetMapping()
    ArrayList<TodoModel> all() {
        return TodoService.getAllTodos();
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
