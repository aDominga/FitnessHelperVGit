package com.example.login;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Scanner;

public class Login {


    //Collections for users
    private MongoCollection<Document> usersCollection;

    public Login(MongoDatabase db) {
        this.usersCollection = db.getCollection("Users");
    }


    //For loging in
    public String promptLogin(Scanner scanner) {

        System.out.print("Please Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Please Enter your password: ");
        String password = scanner.nextLine();


        //makes query to search for user with username and password
        Document query = new Document("username", username).append("password", password);

        //searches for user in collection
        Document user = usersCollection.find(query).first();

        if (user != null) {
            //successfully logged in
            return username; 
        } else {
            //unsuccessfull 
            return null; 
        }
    }


    //For signing up
    public void promptSignUp(Scanner scanner) {

        System.out.print("Enter a new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a new password: ");
        String password = scanner.nextLine();

        //checks if user already exists based on username
        Document existingUser = usersCollection.find(new Document("username", username)).first();
        if (existingUser != null) {
            System.out.println("Unfortunately this username already exists. Please choose a different username.");
            return;
        }

        //if not appends new user to collection
        Document newUser = new Document("username", username).append("password", password);
        usersCollection.insertOne(newUser);

        System.out.println("Your'e all signed up, you can now login with your credentials");
    }
}
