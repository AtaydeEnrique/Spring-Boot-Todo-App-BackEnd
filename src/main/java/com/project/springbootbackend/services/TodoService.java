package com.project.springbootbackend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.springbootbackend.models.TodoModel;

@Service
public class TodoService {
    
    @Autowired
    static final ArrayList<TodoModel> todos = new ArrayList<TodoModel>(); 
    private static Long currentId = 1L;


    public static ArrayList<TodoModel> getAllTodos(String sortBy, String filterBy, String direction, int offset) {
        ArrayList<TodoModel> processedTodos = new ArrayList<TodoModel>(todos);
        String[] filters = filterBy.split(",");
        // Sort by methods ------------- Start
        if(sortBy.equals("priority")){
            if(direction.equals("desc")){
                processedTodos.sort(Comparator.comparing(a -> a.getPriority()));
            } else if(direction.equals("asc")){
                processedTodos.sort(Collections.reverseOrder(Comparator.comparing(a -> a.getPriority())));
            }
        } else if( sortBy.equals("date")){
            if(direction.equals("desc")){
                processedTodos.sort(Comparator.comparing(
                    a -> a.getdueDate() == null ? 
                    null :
                    a.getdueDate() , Comparator.nullsLast(Comparator.naturalOrder())));
            } else if(direction.equals("asc")){
                processedTodos.sort(Comparator.comparing(
                    a -> a.getdueDate() == null ? 
                    null :
                    a.getdueDate() , Comparator.nullsLast(Comparator.reverseOrder())));
            }
        }
        // Sort by methods ------------- End 
        // Filtering methods ----------- Start
        // Filter name if any filter
        var result = processedTodos;
        System.out.println(filters.length);
        if(filters.length > 0){
        if(!filters[0].isEmpty() && !filters[0].equals("NoF")){
            result =  (ArrayList<TodoModel>)  processedTodos.stream()
                    .filter(a -> a.getName().contains(filters[0]))
                    .collect(Collectors.toList());
        }
        // Filter priority if any filter
        if(!filters[1].isEmpty() && !filters[1].equals("NoF")){
           result =  (ArrayList<TodoModel>) processedTodos.stream()
                    .filter(a -> a.getPriority() == Integer.parseInt(filters[1]))
                    .collect(Collectors.toList());
        }
        // Filter completed if any filter
        if(!filters[2].isEmpty() && !filters[2].equals("NoF")){
            result =  (ArrayList<TodoModel>) processedTodos.stream()
                    .filter(a -> a.getCompleted() == Boolean.parseBoolean(filters[2]))
                    .collect(Collectors.toList());
         }}
        // // Filtering methods ----------- End
        
        return result;
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
