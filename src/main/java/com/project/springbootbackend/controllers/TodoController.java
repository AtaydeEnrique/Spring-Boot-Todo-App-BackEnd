package com.project.springbootbackend.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;

    }

    @GetMapping()
    ArrayList<Object> todoList(@RequestParam(required = false, defaultValue = "") String sortBy,
    @RequestParam(required = false, defaultValue = ",,") String filterBy,  
    @RequestParam(required = false, defaultValue = "") String direction, 
    @RequestParam(required = false, defaultValue = "0") Integer offset) {
        ArrayList<Object> res = new ArrayList<Object>();
        res.add(todoService.getAllTodos(sortBy, filterBy, direction,offset));
        res.add(todoService.getInfo());
        return res;
    }

    @PostMapping()
    ResponseEntity<?> newTodo(@RequestBody TodoModel newTodo ) throws Exception {
        try{
            todoService.addTodo(newTodo.getName(), newTodo.getPriority(), newTodo.getdueDate());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getSuppressed()[0].getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "{id}")
    ResponseEntity<?> updateTodo(@RequestBody TodoModel newTodo, @PathVariable Long id) throws Exception {
        try{
            todoService.updateTodo(id, newTodo);
            return new ResponseEntity<>("Updated", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "{id}/done")
    ResponseEntity<?> updateTodoDone(@PathVariable Long id) throws Exception {
        try{
            todoService.updateTodoDone(id);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "{id}/undone")
    ResponseEntity<?> updateTodoUnone(@PathVariable Long id) throws Exception {
        try{
            todoService.updateTodoUndone(id);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "{id}")
    ResponseEntity<?> deleteTodo(@PathVariable Long id) throws Exception {
        try{
            todoService.deleteTodo(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(path = "generate")
    void generate() throws Exception{
        todoService.generateTest();
    }

    
}
