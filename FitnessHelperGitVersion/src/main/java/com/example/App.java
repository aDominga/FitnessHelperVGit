package com.example;

import com.example.grocery.GroceryList;
import com.example.grocery.EditGroceryList;
import com.example.login.Login;
import com.example.recipes.Meals;
import com.example.recipes.RecipeSearch;
import com.example.weekly.EditWeeklyAgenda;
import com.example.weekly.WeeklyAgenda;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        //creates mongo db connection
        MongoClient client = MongoClients.create("");

        //gets ProjectDB database
        MongoDatabase db = client.getDatabase("ProjectDB");

        //gets the agenda,grocery, and meals collection
        MongoCollection<Document> agendasCollection = db.getCollection("UserAgendas");
        MongoCollection<Document> groceryCollection = db.getCollection("GroceryLists");
        MongoCollection<Document> mealsCollection = db.getCollection("Meals");


        //initializes the tools
        Scanner scanner = new Scanner(System.in);
        Login loginHandler = new Login(db);
        WeeklyAgenda weeklyAgenda = new WeeklyAgenda(agendasCollection);
        EditWeeklyAgenda editWeeklyAgenda = new EditWeeklyAgenda();
        RecipeSearch recipeSearch = new RecipeSearch();
        GroceryList groceryList = new GroceryList(groceryCollection);
        EditGroceryList editGroceryList = new EditGroceryList();
        Meals meals = new Meals(mealsCollection);

        String loggedInUser = null;


        //keeps the main menu running
        while (true) {

            //checks if the user is logged in
            if (loggedInUser != null) {


                //Loads the data based on user login
                weeklyAgenda.loadUserAgenda(loggedInUser);
                groceryList.loadList(loggedInUser);
                meals.loadMealsFromDatabase(loggedInUser);

                //keeps cycling to provide user options
                while (true) {
                    System.out.println("\n WWWW User Options: WWW");
                    System.out.println("1. Look at Weekly Planner");
                    System.out.println("2. Edit Weekly Planner");
                    System.out.println("3. Search for Recipes");
                    System.out.println("4. Look at Saved Meals");
                    System.out.println("5. Look at Grocery List");
                    System.out.println("6. Edit Grocery List");
                    System.out.println("7. Logout");


                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); 


                    switch (choice) {

                        case 1:

                            //Display by calling displayWeeklyPlanner
                            weeklyAgenda.displayWeeklyPlanner();
                            break;

                        case 2:

                            //Calls editTask to Edit planner if user inputs 2
                            editWeeklyAgenda.editTask(weeklyAgenda, scanner, meals, loggedInUser);

                            //save into the agenda based by calling saveUserAgenda
                            weeklyAgenda.saveUserAgenda(loggedInUser);
                            break;

                        case 3:


                            //Search recipes and calls searchandsave to save meal with paramters
                            System.out.print("Enter a recipe search term(ex: chicken or eggs): ");
                            String searchTerm = scanner.nextLine();
                            recipeSearch.searchAndSaveRecipes(searchTerm, scanner, meals, loggedInUser);
                            break;

                        case 4:
                            
                            //calls displaySavedMeals to show saved meals
                            meals.displaySavedMeals();
                            break;

                        case 5:


                            //calls display list to show grocery list
                            groceryList.displayList();
                            break;

                        case 6:


                            //calls editList to edit grocery list with paramters
                            editGroceryList.editList(groceryList, loggedInUser, scanner);
                            break;

                        case 7:

                            System.out.println("Logging out...");

                            //setting this to null so doesn't loop login and use can signup 
                            //or login with differenct credentials
                            loggedInUser = null;

                            //Logs user out by breaking while loop
                            break;

                        default:
                            System.out.println("Invalid choice. Please choose a valid option.");
                    }
                    

                    //if user inputs 7 in the user menu it kills submenu
                    if (choice == 7) break;
                }


            //if not while keeps running displaying options
            } else {
                System.out.println("\n Welcome to my Application! :)");
                System.out.println("1. Login");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");


                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();


                if (choice == 1) {

                    //if 1 uses loginHandler to login user
                    loggedInUser = loginHandler.promptLogin(scanner);
                } else if (choice == 2) {

                    //if 2 uses loginHandler to signup user
                    loginHandler.promptSignUp(scanner);
                } else if (choice == 3) {

                    //else kills app
                    System.out.println("Thanks for using my application Bye Bye!");
                    scanner.close();
                    client.close();
                    return;
                }
            }
        }
    }
}
