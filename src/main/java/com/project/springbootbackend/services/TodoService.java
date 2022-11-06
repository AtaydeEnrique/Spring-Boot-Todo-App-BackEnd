package com.project.springbootbackend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.springbootbackend.models.TodoModel;

@Service
public class TodoService {
    
    @Autowired
    static final ArrayList<TodoModel> todos = new ArrayList<>(); 
    private static Long currentId = 1L;


    public static ArrayList<TodoModel> getAllTodos() {
        return todos;
    }

    public static TodoModel addTodo( String name, Integer priority, LocalDateTime dueDate) throws Exception{
        TodoModel newTodo = new TodoModel(currentId, name, priority, dueDate, false);
        currentId++;
        todos.add(newTodo);
        return newTodo;
    }

    public static TodoModel updateTodo(Long id, TodoModel newTodo){

        for (TodoModel todo : todos) {
            if (todo.getId().equals(id)) {
                todo.setName(newTodo.getName());
                todo.setPriority(newTodo.getPriority());
                todo.setdueDate(newTodo.getdueDate());
                todo.setCompleted(newTodo.getCompleted());
            }
        }
        
        return newTodo;
    }

    public static void deleteTodo(Long id){
        todos.removeIf(curr -> curr.getId().equals(id));
    }
}
