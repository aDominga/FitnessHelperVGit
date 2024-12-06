package com.example.grocery;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class GroceryList {


    //List to store gorceries
    private List<String> groceries; 

    //MongoCollection for groceries
    private MongoCollection<Document> groceryCollection; 

    public GroceryList(MongoCollection<Document> groceryCollection) {
        this.groceryCollection = groceryCollection;
        this.groceries = new ArrayList<>();
    }

    //For addind items
    public void addItem(String username, String item) {
        
        //adds to list
        groceries.add(item);
        System.out.println("added the item: " + item);

        //calls save list with corresponding user
        saveList(username); 
    }

    //For removing items
    public void removeItem(String username, String item) {
        if (groceries.remove(item)) {
            System.out.println("Removed the item: " + item);
            
            //save changes to db
            saveList(username); 
        } else {
            System.out.println("This item was not found: " + item);
        }
    }

    //Display grocer list
    public void displayList() {
        System.out.println("\n *** Current Grocery List: ***");
        if (groceries.isEmpty()) {
            System.out.println("Your grocery list is currently empty.");
        } else {

            //iterates through list and displays
            for (int i = 0; i < groceries.size(); i++) {
                System.out.println((i + 1) + ". " + groceries.get(i));
            }
        }
    }

    //Load grocery list based on the user
    public void loadList(String username) {
        Document userList = groceryCollection.find(new Document("username", username)).first();
        if (userList != null) {

            //grabs groceries using gorceries key
            groceries = (List<String>) userList.get("groceries");
        } else {

             //make empty list if none exists
            groceries = new ArrayList<>();
        }
    }

    //Save the grocery list to db based on user
    private void saveList(String username) {
        
        //Creates a doc with username and groceries
        Document userList = new Document("username", username).append("groceries", groceries);

        //save or update. if not inserts if it can't find it 
        groceryCollection.replaceOne(new Document("username", username), userList, new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }
}
