package com.example.recipes;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Meals {

     //List to store saved meals as docs form mongodb
    private List<Document> savedMeals;
    
    //MongoDB collection
    private MongoCollection<Document> mealsCollection; 


    public Meals(MongoCollection<Document> mealsCollection) {
        this.mealsCollection = mealsCollection;
        this.savedMeals = new ArrayList<>();
    }

    //Add a meal to the list and save it to the database
    public void addMeal(String username, String mealName, List<String> ingredients) {
        Document meal = new Document("name", mealName).append("ingredients", ingredients);
        savedMeals.add(meal);
        saveMealsToDatabase(username);
        System.out.println("Current meal saved: " + mealName);
    }

    //Retrieve saved meals for a specific user from the database
    public void loadMealsFromDatabase(String username) {

        //looks for meals using username as a key
        Document userMeals = mealsCollection.find(new Document("username", username)).first();
        if (userMeals != null) {
            
            //grabs saved meals using meals key
            savedMeals = (List<Document>) userMeals.get("meals");
        } else {
            //or creates new list for meals
            savedMeals = new ArrayList<>();
        }
    }

    //Save the current list of meals to the database
    private void saveMealsToDatabase(String username) {

        //creates doc with username and saved meals
        Document userMeals = new Document("username", username).append("meals", savedMeals);

        //save or update, or if not found insert
        //also filters by username
        mealsCollection.replaceOne(new Document("username", username), userMeals, new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }

    //Display saved meals
    public void displaySavedMeals() {
        System.out.println("\n *** Saved Meals: *** ");
        if (savedMeals.isEmpty()) {
            System.out.println("No meals saved.... :(");
        } else {

            //iterates through savedMeals then displays number and name
            //iterates through ingredeints of meal to display ingredients line by line
            for (int i = 0; i < savedMeals.size(); i++) {
                Document meal = savedMeals.get(i);
                System.out.println((i + 1) + ". " + meal.getString("name"));
                System.out.println("   Ingredients:");
                List<String> ingredients = (List<String>) meal.get("ingredients");
                for (String ingredient : ingredients) {
                    System.out.println("   - " + ingredient);
                }
            }
        }
    }

    //Get a list of saved meal names
    public List<String> getMealNames() {

        //creates a list to store meal names
        List<String> mealNames = new ArrayList<>();

        //goes through saved meals and grabs the name
        for (Document meal : savedMeals) {
            mealNames.add(meal.getString("name"));
        }

        //return said names
        return mealNames;
    }
}
