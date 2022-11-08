package com.project.springbootbackend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TodoModel {

    // Variables
    private Long id;
    private String name;
    private Integer priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;
    private Boolean completed;

    public TodoModel(){}
    // Constructor
    public TodoModel(Long id, String name, Integer priority, 
                    LocalDateTime createdDate, LocalDateTime dueDate, 
                    LocalDateTime completedDate, Boolean completed) throws Exception {
                      
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        this.completed = completed;
        
        try{
        validate();}catch(Exception e){
          e.printStackTrace();
        }
    } 

    // Variable getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDateTime getdueDate() {
        return dueDate;
    }

    public void setdueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedDate() {
      return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
      this.createdDate = createdDate;
    }

    public LocalDateTime getCompletedDate() {
      return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
      this.completedDate = completedDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    /** ----------- Validation ---------------
    Java Practices: Validation Belongs to Model Object */

    private void validate() throws Exception {
        // Initialize array with all our errors if any.
        List<String> errors = new ArrayList<>();

        // Ensure our name is not null
        ensureNotNull(name, "Name is null", errors);
        
        // Check if our todo name is not empty and length between 1 and 120.
        if (!hasContent(name)) {
          errors.add("Name has no content.");
        }

        // Priority 1 = High, 2 = Medium, 3 = Low, value limiter
        if (priority < 1 || priority > 3 || priority == null){
          errors.add("Priority must be an Integer in range from 1 to 3");
        }


        // We limit the ranges of of our completed Time varible.
        LocalDateTime START = LocalDateTime.of(1900, 1, 1, 00,00,00);
        LocalDateTime END = LocalDateTime.of(2099, 12, 31, 23,59,59);
        if (dueDate != null){
          if (dueDate.isBefore(START) || dueDate.isAfter(END)) {
            errors.add("Date is outside the normal range: " + START + ".." + END);
          }
        }

        // We limit the completed date to be always after created date.
        LocalDateTime FINISH = LocalDateTime.of(2099, 12, 31, 23,59,59);
        if (completedDate != null){
          if (completedDate.isBefore(createdDate)) {
            errors.add("Compled Date cant be before your creation date");
          }
          if(completedDate.isAfter(FINISH)){
            errors.add("Completition date cant be outside range: " + FINISH);
          }
        }
          
        // Check if our errors array is empty, if false, throw the exceptions.
        if (!errors.isEmpty()) {
          Exception ex = new Exception();
          for(String error: errors) {
            ex.addSuppressed(new Exception(error));
          }
          throw ex;
        }
    }

    /*  Custom validation methods
      Returns true only if the field passes the test, and is NOT null. */
      private boolean ensureNotNull(Object field, String errorMsg, List<String> errors) {
        boolean result = true;
        if (field == null) {
          errors.add(errorMsg);
          result = false;
        }
        return result;
      }

      //  Returns true only if a string has content.
      private boolean hasContent(String string) {
        return (string != null && string.trim().length() > 0 && string.trim().length() < 120);
      }

}