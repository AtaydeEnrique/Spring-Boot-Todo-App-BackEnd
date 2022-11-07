package com.project.springbootbackend.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.springbootbackend.models.TodoModel;

@Service
public class TodoService {
    
    @Autowired
    static final ArrayList<TodoModel> todos = new ArrayList<TodoModel>();
    static final Map<String, Object> info = new HashMap<>();
    static final Map<String, Double> averages = new HashMap<>(Map.of(
        "totalAv", 0.0,
        "lowAv", 0.0, 
        "medAv", 0.0,
        "hiAv", 0.0
    ));
    

    
    private static Long currentId = 1L;


    public static ArrayList<TodoModel> getAllTodos(String sortBy, String filterBy, String direction, int offset) {
        String[] filters = filterBy.split(",");
        ArrayList<TodoModel> reference = new ArrayList<>(todos);
        var result = reference;
        /*  This copies (by reference) the elements in the original list to the new list
        This will let us filter/sort our database without modifying it*/

        // Filtering methods ----------- Start

        // Filter name if any filter
        if(filters.length > 0){
            if(!filters[0].isEmpty() && !filters[0].equals("NoF")){
                result =  (ArrayList<TodoModel>)  result.stream()
                        .filter(a -> a.getName().toLowerCase().contains(filters[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
            // Filter priority if any filter
            if(!filters[1].isEmpty() && !filters[1].equals("NoF")){
                result = (ArrayList<TodoModel>) result.stream()
                        .filter(a -> a.getPriority() == Integer.parseInt(filters[1]))
                        .collect(Collectors.toList());
            }
            // Filter completed if any filter
            if(!filters[2].isEmpty() && !filters[2].equals("NoF")){
                result =  (ArrayList<TodoModel>) result.stream()
                        .filter(a -> a.getCompleted() == Boolean.parseBoolean(filters[2]))
                        .collect(Collectors.toList());
             }}
        // Filtering methods ----------- End

        // Sort by methods ------------- Start
        if(sortBy.equals("priority")){
            if(direction.equals("desc")){
                result.sort(Comparator.comparing(a -> a.getPriority()));
            } else if(direction.equals("asc")){
                result =  (ArrayList<TodoModel>) todos.stream().sorted(Collections.reverseOrder(Comparator.comparing(a -> a.getPriority()))).collect(Collectors.toList());
            }
        } else if( sortBy.equals("date")){
            if(direction.equals("desc")){
                result.sort(Comparator.comparing(
                    a -> a.getdueDate() == null ? 
                    null :
                    a.getdueDate() , Comparator.nullsLast(Comparator.naturalOrder())));
            } else if(direction.equals("asc")){
                result.sort(Comparator.comparing(
                    a -> a.getdueDate() == null ? 
                    null :
                    a.getdueDate() , Comparator.nullsLast(Comparator.reverseOrder())));
            }
        }
        // Sort by methods ------------- End 
        
        // Pagination
        int start = offset * 10;
        int end = ((result.size() < 10 * (offset + 1)) ? result.size() : 10 * (offset + 1));
        
        if(result.size() == 0){
            return result;
        }else{
            info.put("totalTodos", result.size());
            info.put("totalPages", (result.size() < 10 ? 1 :(int) (Math.ceil((double)result.size()/10))));
            return new ArrayList<TodoModel>(result.subList(start, end));
        }
    }

    public static TodoModel addTodo( String name, Integer priority, LocalDateTime dueDate) throws Exception{
        TodoModel newTodo = new TodoModel(currentId, name, priority, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS), dueDate, null, false);
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
            }
        }
        return newTodo;
    }

    public static void updateTodoDone(Long id){
        for (TodoModel todo : todos) {
            if (todo.getId().equals(id)) {
                var done = todo.getCompleted();
                if (!done){
                    todo.setCompleted(true);
                    todo.setCompletedDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
                }
            }
        }
        calculateAverages();
    }

    public static void updateTodoUndone(Long id){
        for (TodoModel todo : todos) {
            if (todo.getId().equals(id)) {
                var done = todo.getCompleted();
                if (done){
                    todo.setCompleted(false);
                    todo.setCompletedDate(null);
                }
            }
        }
        calculateAverages();
    }

    public static void deleteTodo(Long id){
        todos.removeIf(curr -> curr.getId().equals(id));
    }

    public static Object getInfo(){
        info.put("averages", averages);
        return info;
    }

    public static void calculateAverages() {
        int totalLow = 0;
        int totalMed = 0;
        int totalHi = 0;
        int totalAv = 0;
        double toSum;

        averages.put("hiAv",0.0);
        averages.put("medAv",0.0);
        averages.put("lowAv",0.0);
        averages.put("totalAv",0.0);

        for (TodoModel todo : todos) {
            switch(todo.getPriority()){
                case 1:
                    if(todo.getCompletedDate() != null){
                        toSum = ChronoUnit.SECONDS.between(todo.getCreatedDate(), todo.getCompletedDate());
                        totalHi++;
                        totalAv++;
                        averages.put("totalAv", averages.get("totalAv") + toSum);
                        averages.put("hiAv", averages.get("hiAv") + toSum);
                    }
                    
                    break;
                case 2:
                    if(todo.getCompletedDate() != null){
                        toSum = ChronoUnit.SECONDS.between(todo.getCreatedDate(), todo.getCompletedDate());
                        totalMed++;
                        totalAv++;
                        averages.put("totalAv", averages.get("totalAv") + toSum);
                        averages.put("medAv", averages.get("medAv") + toSum);
                    }
                    
                    break;
                case 3:
                    if(todo.getCompletedDate() != null){
                        toSum = ChronoUnit.SECONDS.between(todo.getCreatedDate(), todo.getCompletedDate());
                        totalLow++;
                        totalAv++;
                        averages.put("totalAv", averages.get("totalAv") + toSum);
                        averages.put("lowAv", averages.get("lowAv") + toSum);

                    }
                    
                    break;
            }
            if(averages.get("totalAv") != 0.0){
                averages.put("totalAv", averages.get("totalAv") / totalAv);
            }
            if(averages.get("hiAv") != 0.0){
                averages.put("hiAv", averages.get("hiAv") / totalHi);
            }
            if(averages.get("medAv") != 0.0){
                averages.put("medAv", averages.get("medAv") / totalMed);
            }
            if(averages.get("lowAv") != 0.0){
                averages.put("lowAv", averages.get("lowAv") / totalLow);
            }
            
        }
    }

    public static void generateTest() throws Exception{
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), LocalDateTime.parse(String.format("201%s-12-30T19:34:50.63", ThreadLocalRandom.current().nextInt(0, 9 + 1))));
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), LocalDateTime.parse(String.format("201%s-12-30T19:34:50.63", ThreadLocalRandom.current().nextInt(0, 9 + 1))));
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), null);
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), LocalDateTime.parse(String.format("201%s-12-30T19:34:50.63", ThreadLocalRandom.current().nextInt(0, 9 + 1))));
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), LocalDateTime.parse(String.format("201%s-12-30T19:34:50.63", ThreadLocalRandom.current().nextInt(0, 9 + 1))));
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), null);
        addTodo(String.format("Test %s ", currentId), ThreadLocalRandom.current().nextInt(1, 3 + 1), LocalDateTime.parse(String.format("201%s-12-30T19:34:50.63", ThreadLocalRandom.current().nextInt(0, 9 + 1))));

    }

    

    
}
