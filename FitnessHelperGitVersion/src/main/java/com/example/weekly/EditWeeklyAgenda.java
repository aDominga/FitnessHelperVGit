package com.example.weekly;

import com.example.recipes.Meals;

import java.util.List;
import java.util.Scanner;

public class EditWeeklyAgenda {

    public void editTask(WeeklyAgenda weeklyAgenda, Scanner scanner, Meals meals, String username) {


        //Grab the list of days and tasks from the WeeklyAgenda
        String[] daysOfWeek = weeklyAgenda.getDaysOfWeek();
        String[][] tasks = weeklyAgenda.getTasks();

        //Display days and week
        System.out.println("\n Which day of the week would you like to edit?");
        for (int i = 0; i < daysOfWeek.length; i++) {
            System.out.println((i + 1) + ". " + daysOfWeek[i]);
        }

        //Ask to select day
        System.out.print("Enter the number of the day you want to edit: ");
        int dayIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        //Checks if valid
        if (dayIndex < 0 || dayIndex >= daysOfWeek.length) {
            System.out.println("Invalid choice. Please choose a valid number.");
            return;
        }

        //Display the tasks
        System.out.println("\nTasks:  " + daysOfWeek[dayIndex] + ":");
        
        //iterates through each task in the day to display replacing nulls with "Empty Task"
        for (int i = 0; i < tasks[dayIndex].length; i++) {
            System.out.println((i + 1) + ". " + (tasks[dayIndex][i] != null ? tasks[dayIndex][i] : "Empty Task"));
        }

        //Ask to select task
        System.out.print("Enter the number of the task you want to edit: ");
        int taskIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        //Checks if valid
        if (taskIndex < 0 || taskIndex >= tasks[dayIndex].length) {
            System.out.println("Invalid choice. Please choose a valide number.");
            return;
        }

        //Ask for a workout or meal
        System.out.println("\n ??? What would you like to add ???");
        System.out.println(" ^^^ 1. Add a Workout ^^^ ");
        System.out.println(" vvv 2. Add a Saved Meal vvv ");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();


        //If workout
        if (choice == 1) {
            //CalBurned logic
            System.out.print("How many hours did you workout? (round to nearest hours): ");
            int workoutHours = scanner.nextInt();


            System.out.print("How much do you weigh? (round to nearest number in lbs): ");
            int weight = scanner.nextInt();
            scanner.nextLine();

            //calculates cals burned
            double caloriesBurned = weight * 2.08 * workoutHours;
            double calsBurnedApprox = Math.round(caloriesBurned*100.0)/100.0;
            String workoutTask = "Workout - Calories Burned: " + calsBurnedApprox + " *note that this is a very broad estimate";

            //updates with calories burned
            weeklyAgenda.updateTask(dayIndex, taskIndex, workoutTask);
            System.out.println("Workout added successfully!");



            //If Meal
        } else if (choice == 2) {

            //shows presaved meals
            meals.displaySavedMeals();
            System.out.print("\nEnter the number of the meal to add: ");

            //selects meal off of #
            int mealChoice = scanner.nextInt();
            scanner.nextLine();


            //For grabbing selected meal
            List<String> savedMeals = meals.getMealNames();
            if (mealChoice > 0 && mealChoice <= savedMeals.size()) {
                String selectedMeal = savedMeals.get(mealChoice - 1);

                //Change task to selected meal
                weeklyAgenda.updateTask(dayIndex, taskIndex, "Meal: " + selectedMeal);
                System.out.println("Meal added to weekly planner successfully!");
            } else {
                System.out.println("Invalid meal choice. No changes were made.");
            }
        } else {
            System.out.println("Invalid choice. No changes were made.");
        }
    }
}
