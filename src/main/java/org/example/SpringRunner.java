package org.example;

import org.example.Models.Customer;
import org.example.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringRunner implements ApplicationRunner {

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringRunner.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //Get all customers
        printCustomerList(customerRepository.getAllCustomers());

        // Get customer by id
        System.out.println("Customer found by id: " + customerRepository.findById(1));

        //Get customer by name
        System.out.println("Customer found by name: " + customerRepository.findByName("Taylor"));

        // Get page of customers by limit and page number
        System.out.println(customerRepository.customerPage(8, 2));

        //Add a new customer to the database
        Customer customerRay = new Customer(null, "Ray", "Miller", "Portugal", "899898", "90990909090", "h@abc.com");
        customerRepository.insertCustomer(customerRay);

        // Get existing customer and update information
        Customer customer = customerRepository.findById(1);
        customer.setFirstName("Levis");
        System.out.println("Updated customer information: " + customerRepository.update(customer));

        //Get the country with the most customers
        System.out.println("Country with most customers: " + customerRepository.countryWithMostCustomers().getName());

        // Get customer who is the highest spender
        System.out.println("Highest spending customer: " + customerRepository.highestSpender());

        //Get most popular genre for a given customer
        System.out.println("Most popular genre: " + customerRepository.mostPopularGenre(3));
    }

    private void printCustomerList(List<Customer> allCustomers) {
        System.out.println("Total number of customer retrieved:"+ allCustomers.size());
        for(Customer customer : allCustomers){
            System.out.println("Customer " + customer.getId()+ " " +customer.getFirstName() +" " + customer.getLastName() +" " + customer.getCountry() +" " +customer.getPostalCode() +" " + customer.getPhone() +" " + customer.getEmail());
        }
    }
}
