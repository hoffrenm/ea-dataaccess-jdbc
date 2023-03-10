package org.example.Repositories;

import org.example.Models.Customer;
import org.example.Models.CustomerCountry;
import org.example.Models.CustomerGenre;
import org.example.Models.CustomerSpender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CustomerRepository that handles database operations associated
 * with Customer class.
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final String url;
    private final String username;
    private final String password;

    /**
     * Creates a new CustomerRepository with given database name,
     * username and password.
     *
     * @param url url of database
     * @param username database user username
     * @param password database user password
     */
    public CustomerRepositoryImpl(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a new Customer object from ResultSet. By default, contains
     * id, first name, last name, county, postal code, phone and email.
     *
     * @param result ResultSet object that is returned by SQL query
     * @return Customer object
     * @throws SQLException
     */
    private static Customer createCustomerFromResult(ResultSet result) throws SQLException {
        Customer customer = new Customer(
                result.getInt("customer_id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("country"),
                result.getString("postal_code"),
                result.getString("phone"),
                result.getString("email")
        );
        return customer;
    }

    // Assignment part 2.1 Read all the customers in the database

    /**
     * Returns all customers from the database.
     *
     * @return list of all customers
     */
    @Override
    public List<Customer> findAll() {

        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement("SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Customer customer = createCustomerFromResult(result);
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database, error: " + e.getMessage());
        }

        return customers;
    }


    // Assignment part 2.2 Read a specific customer from the database (by Id)

    /**
     * Reads a specific customer from the database identified by unique id
     *
     * @param id unique id
     * @return Customer object if id exists, otherwise null
     */
    @Override
    public Customer findById(Integer id) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer WHERE customer_id = ?";
        Customer customer = null;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                customer = new Customer(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("country"),
                        result.getString("postal_code"),
                        result.getString("phone"),
                        result.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    // Assignment part 2.3 Read a specific customer by name

    /**
     * Reads a customer from database by trying to match
     * input string to first name or last name.
     *
     * @param name string to match
     * @return Customer object if name matches, otherwise null
     */
    public Customer findByName(String name) {
        Customer customer = null;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement("SELECT customer_id, first_name, last_name, country, postal_code, phone, email " +
                    "FROM customer WHERE first_name LIKE ? or last_name LIKE ?");
            statement.setString(1, name);
            statement.setString(2, name);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                customer = createCustomerFromResult(result);
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database, error: " + e.getMessage());
        }
        return customer;
    }

    // Assignment part 2.4 Return a page of customers from the database

    /**
     * Reads a page of customers from database. First parameter indicates number
     * of results and second parameter indicates page number.
     * Offsets query by limit * pageNum.
     *
     * @param limit number of customers
     * @param pageNum page number
     * @return list of customers on certain page
     */
    public List<Customer> customerPage(int limit, int pageNum) {
        int offset = limit * pageNum;
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer LIMIT ? OFFSET ?";
        List<Customer> customerPage = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                customerPage.add(
                        new Customer(
                                result.getInt("customer_id"),
                                result.getString("first_name"),
                                result.getString("last_name"),
                                result.getString("country"),
                                result.getString("postal_code"),
                                result.getString("phone"),
                                result.getString("email")
                        ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerPage;
    }

    //Assignment part 2.5 Add a new customer to the database

    /**
     * Inserts a new customer to database.
     *
     * @param customer Customer object
     * @return 1 for successful operation, 0 otherwise
     */
    public int insert(Customer customer) {
        int rowsAffected = 0;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            PreparedStatement statement = conn.prepareStatement("INSERT INTO customer (first_name, last_name, country, postal_code, phone, email) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getCountry());
            statement.setString(4, customer.getPostalCode());
            statement.setString(5, customer.getPhone());
            statement.setString(6, customer.getEmail());

            rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("New customer has been added");
            } else {
                System.out.println("Could not add new customer");
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database, error: " + e.getMessage());
        }
        return rowsAffected;
    }

    // Assignment part 2.6 Update an existing customer

    /**
     * Updates a single customer record by customer's id
     *
     * @param customer to be updated
     * @return 1 or higher in case update worked, 0 in case it did not
     */

    public int update(Customer customer) {
        String sql = "UPDATE customer SET first_name = ?, last_name = ?, country = ?, postal_code = ?, phone = ?, email = ? WHERE customer_id = ?";
        int result = 0;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getCountry());
            statement.setString(4, customer.getPostalCode());
            statement.setString(5, customer.getPhone());
            statement.setString(6, customer.getEmail());
            statement.setInt(7, customer.getId());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Assignment part 2.7 Return the country with the most customers

    /**
     * Returns a country that has the most customers.
     *
     * @return the country with the most customers
     * @throws SQLException in case the database connection fails
     */

    public CustomerCountry countryWithMostCustomers() throws SQLException {

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement("SELECT country FROM customer GROUP BY country ORDER BY COUNT(customer_id) DESC LIMIT 1");
            ResultSet result = statement.executeQuery();
            result.next();

            return new CustomerCountry(result.getString("country"));

        } catch (SQLException e) {
            System.out.println("Could not connect to database, error: " + e.getMessage());
            throw e;
        }
    }


    // Assignment part 2.8 Return the customer who is the highest spender

    /**
     * Returns id, first name, last name and the total sum of the customer that 
     * has the highest total in the invoices.
     *
     * Assignment part 2.8
     * @return the customer who is the highest spender
     */
    public CustomerSpender highestSpender() {
        String sql = "SELECT customer.customer_id, customer.first_name, customer.last_name, SUM(invoice.total) as total FROM customer JOIN invoice ON customer.customer_id=invoice.customer_id GROUP BY customer.customer_id ORDER BY total DESC LIMIT 1";
        CustomerSpender customer = null;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                customer = new CustomerSpender(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getDouble("total")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    // Assignment part 2.9: For a given customer, return their most popular genre

    /**
     * Returns customer's most popular genre
     *
     * @param customer_id the customer id
     * @return the name of the most popular genre for the customer and the count of tracks
     * @throws SQLException in case the database connection fails
     */

    public CustomerGenre mostPopularGenre(Integer customer_id) throws SQLException {

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement("SELECT genre.name, count FROM genre\n" +
                    "JOIN (\n" + "SELECT genre_id, COUNT(*) as count FROM invoice_line\n" + "JOIN track ON track.track_id = invoice_line.track_id\n" +
                    "JOIN invoice ON invoice.invoice_id = invoice_line.invoice_id WHERE invoice.customer_id = " + customer_id + "\n" +
                    "GROUP BY genre_id\n" + ") AS customer_tracks ON genre.genre_id = customer_tracks.genre_id\n" + "ORDER BY customer_tracks.count DESC LIMIT 2;");
            ResultSet result = statement.executeQuery();

            result.next();
            return new CustomerGenre(
                    result.getString("name"),
                    result.getInt("count")
            );

        } catch (SQLException e) {
            System.out.println("Could not connect to database, error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Default delete methods
     * @param object
     * @return
     */

    @Override
    public int delete(Customer object) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

}
