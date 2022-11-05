package com.project.springbootbackend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoModel {

    // Variables
    private Long id;
    private String name;
    private Integer priority;
    private LocalDateTime startDate;
    private Boolean completed;

    // Constructor
    public TodoModel(Long id, String name, Integer priority, LocalDateTime startDate, Boolean completed) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.completed = completed;
        this.startDate = startDate;
        try {
            validate();
        } catch (Exception e) {
            e.printStackTrace();
        } // Validate variables before constructing
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

    public LocalDateTime getstartDate() {
        return startDate;
    }

    public void startDate(LocalDateTime startDate) {
        this.startDate = startDate;
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

        // Ensure our id and name is not null
        ensureNotNull(id, "Id is null", errors);
        ensureNotNull(name, "Name is null", errors);
        
        // Check if our todo name is not empty.
        if (!hasContent(name)) {
          errors.add("Name has no content.");
        }

        // We limit the ranges of start and end of our dateTime varible.
        boolean passes = ensureNotNull(startDate, "Date purchased is null.", errors);
        if (passes) {
          LocalDateTime START = LocalDateTime.of(1900, 1, 1, 00,00,00);
          LocalDateTime END = LocalDateTime.of(2099, 12, 31, 23,59,59);
          if (startDate.isBefore(START) || startDate.isAfter(END)) {
            errors.add("Date purchased is outside the normal range: " + START + ".." + END);
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
        return (string != null && string.trim().length() > 0);
      }
}