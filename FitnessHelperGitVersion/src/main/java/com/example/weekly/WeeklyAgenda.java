package com.example.weekly;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

public class WeeklyAgenda {

    //2D array for tasks
    private String[][] tasks; 
    private String[] daysOfWeek;

    //MongoDB collection
    private MongoCollection<Document> agendasCollection; 

    public WeeklyAgenda(MongoCollection<Document> agendasCollection) {
        this.agendasCollection = agendasCollection;

        //creates weekly agenda
        this.daysOfWeek = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        //creates 7 rows of three tasks(cols) for each day
        this.tasks = new String[7][3]; 

        //fill with empty tasks
        for (int i = 0; i < tasks.length; i++) {
            for (int j = 0; j < tasks[i].length; j++) {
                tasks[i][j] = "Empty Task";
            }
        }
    }



    public void loadUserAgenda(String username) {

        //Query the database for agenda 
        Document userAgenda = agendasCollection.find(Filters.eq("username", username)).first();
    
        if (userAgenda != null) {


            //Returns stored tasks
            List<List<String>> storedTasks = (List<List<String>>) userAgenda.get("tasks");
    
            //Load tasks into the array
            if (storedTasks != null) {
                for (int i = 0; i < storedTasks.size(); i++) {
                    for (int j = 0; j < storedTasks.get(i).size(); j++) {
                        tasks[i][j] = storedTasks.get(i).get(j); 
                    }
                }
                System.out.println("Agenda loaded successfully FOR " + username);
            } else {
                System.out.println("No tasks found in the database. Using default empty tasks.");
            }
        } else {
            System.out.println("No existing agenda found FOR " + username + ". Using default empty tasks.");
        }
    }
    


    public String[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public String[][] getTasks() {
        return tasks;
    }


    //for updating task
    public void updateTask(int dayIndex, int taskIndex, String newTask) {


        //goes through 2d array to update [day/row][task/col]
        if (dayIndex >= 0 && dayIndex < tasks.length && taskIndex >= 0 && taskIndex < tasks[dayIndex].length) {
            tasks[dayIndex][taskIndex] = newTask; 
        }
    }

    //to display entire planner
    public void displayWeeklyPlanner() {
        System.out.println("\nWeekly Planner:");

        //iterates through entire 2d array and displays it
        for (int i = 0; i < daysOfWeek.length; i++) {
            System.out.println(daysOfWeek[i] + ":");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.println("  Slot " + (j + 1) + ": " + tasks[i][j]);
            }
        }
    }


    //to save into db
    public void saveUserAgenda(String username) {
        
        //Convert tasks array to be able to store in db
        List<List<String>> storedTasks = new java.util.ArrayList<>();
        for (String[] dayTasks : tasks) {
            storedTasks.add(java.util.Arrays.asList(dayTasks));
        }


        //Creates a doc with username and tasks
        Document userAgenda = new Document("username", username).append("tasks", storedTasks);

        //save or update. if not inserts if it can't find it 
        agendasCollection.replaceOne(Filters.eq("username", username), userAgenda, new com.mongodb.client.model.ReplaceOptions().upsert(true));
        System.out.println("Agenda updated successfully FOR " + username);
    }
}
