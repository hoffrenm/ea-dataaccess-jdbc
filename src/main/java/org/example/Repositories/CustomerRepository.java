package org.example.Repositories;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class CustomerRepository extends Generic {

    private String url = "jdbc:postgresql://localhost:5432/chinook";
    private String username = "postgres";
    private String password = "postgres";

    public void test() {
        try(Connection conn = DriverManager.getConnection(url, username,password);) {
            System.out.println("Connected to Postgres...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //opening a database connection etc?
    //jdbc template?

    //1. Find all customers


    //2. Find a specific customer

    //etc!
}
