package com.example.grocery;

import java.util.Scanner;

public class EditGroceryList {

    public void editList(GroceryList groceryList, String username, Scanner scanner) {
        while (true) {
            
            //displays options
            System.out.println("\n *** Edit Grocery List: *** ");
            System.out.println("1. + Add a item + ");
            System.out.println("2. - Remove a item -");
            System.out.println("3. O View grocery list O");
            System.out.println("4. X Exit X");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:

                    //For adding an item
                    System.out.print("Enter the item you want to add: ");
                    String itemToAdd = scanner.nextLine();

                    //Add item to grocery list based on user
                    groceryList.addItem(username, itemToAdd);
                    break;

                case 2:
                
                    //For removing an item
                    System.out.print("Enter the item you want to remove: ");
                    String itemToRemove = scanner.nextLine();

                    //Remove item from grocery list based on user
                    groceryList.removeItem(username, itemToRemove);
                    break;

                case 3:
                
                    //Display
                    groceryList.displayList();
                    break;

                case 4:


                    //Exits grocery list editor
                    System.out.println("Now leaving the grocery list editor.");
                    return;

                default:
                    System.out.println("Invalid choice. Please type a valid number");
            }
        }
    }
}
