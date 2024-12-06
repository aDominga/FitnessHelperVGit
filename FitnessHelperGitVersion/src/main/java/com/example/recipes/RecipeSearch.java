package com.example.recipes;



import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//Accessing recipe search API
public class RecipeSearch {
    private static final String APP_ID = "";
    private static final String APP_KEY = "";
    private static final String BASE_URL = "https://api.edamam.com/search";

    private OkHttpClient client;

    public RecipeSearch() {
        this.client = new OkHttpClient();
    }






    //Method to search recipes and save
    public void searchAndSaveRecipes(String searchTerm, Scanner scanner, Meals meals, String username) {
        //Creates API url with the search stuff
        String url = BASE_URL + "?q=" + searchTerm.replace(" ", "+") + "&app_id=" + APP_ID + "&app_key=" + APP_KEY;


        //Creates new reques object to build url
        Request request = new Request.Builder().url(url).build();


        //Sends the http request and receive response
        try (Response response = client.newCall(request).execute()) {
            //Checks if it's successful 
            if (response.isSuccessful() && response.body() != null) {
                //Reads json as string
                String responseBody = response.body().string();

                //uses objects as paramters for display and save
                displayAndSaveRecipes(responseBody, scanner, meals, username);
            } else {
                System.out.println("Request failed throwing status code: " + response.code());
            }
        
            //catches error 
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }



    //method to display and save recipes 
    private void displayAndSaveRecipes(String responseBody, Scanner scanner, Meals meals, String username) {
        
        //Parses body to json
        JSONObject jsonResponse = new JSONObject(responseBody);
        //hits(list for individual objects for recipes)
        JSONArray hits = jsonResponse.getJSONArray("hits");

        //checks if their are any recipes
        if (hits.length() == 0) {
            System.out.println("No recipes were found.");
            return;
        }




        System.out.println("Here are the top 12 Recipes: ");
        
        //List to store recipe objects
        List<JSONObject> recipes = new ArrayList<>();

        //checks for the first twelve or if there is less 
        for (int i = 0; i < Math.min(12, hits.length()); i++) {

            //grabs recipt from hit(recipe list)
            JSONObject recipe = hits.getJSONObject(i).getJSONObject("recipe");
            //adds it to the Recipe List
            recipes.add(recipe);

            //numbers them
            System.out.println((i + 1) + ". " + recipe.getString("label"));
        }

        //sets up scanner stuff 
        System.out.print("\nEnter the number of the recipe you want to save (if you want to skip hit 0): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 


    
        if (choice > 0 && choice <= recipes.size()) {
            //Grabs the chosen recipe and the name of it 
            JSONObject selectedRecipe = recipes.get(choice - 1);
            String recipeName = selectedRecipe.getString("label");

            //Grabs ingredients from recipe
            List<String> ingredients = new ArrayList<>();
            JSONArray ingredientLines = selectedRecipe.getJSONArray("ingredientLines");
            for (int i = 0; i < ingredientLines.length(); i++) {
                ingredients.add(ingredientLines.getString(i));
            }

            //save the recipe and ingredients to db
            meals.addMeal(username, recipeName, ingredients);
        } else {
            System.out.println("Skipping...   no recipes saved");
        }
    }
}
